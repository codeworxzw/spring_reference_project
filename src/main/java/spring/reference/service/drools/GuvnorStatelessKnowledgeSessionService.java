package spring.reference.service.drools;

import java.util.List;

import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;

import org.drools.KnowledgeBase;
import org.drools.agent.KnowledgeAgent;
import org.drools.event.knowledgeagent.AfterChangeSetAppliedEvent;
import org.drools.event.knowledgeagent.AfterChangeSetProcessedEvent;
import org.drools.event.knowledgeagent.AfterResourceProcessedEvent;
import org.drools.event.knowledgeagent.BeforeChangeSetAppliedEvent;
import org.drools.event.knowledgeagent.BeforeChangeSetProcessedEvent;
import org.drools.event.knowledgeagent.BeforeResourceProcessedEvent;
import org.drools.event.knowledgeagent.KnowledgeAgentEventListener;
import org.drools.event.knowledgeagent.KnowledgeBaseUpdatedEvent;
import org.drools.event.knowledgeagent.ResourceCompilationFailedEvent;
import org.drools.io.Resource;
import org.drools.io.ResourceChangeScannerConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spring.reference.exception.InternalServerErrorException;
import spring.reference.meta.POI;
import spring.reference.meta.POITag;
import spring.reference.meta.TODO;
import spring.reference.meta.TODOTag;
import spring.reference.util.Logged;

@TODO(tags = { TODOTag.UNACCESSABLE, TODOTag.UNTESTED })
@Logged
@Named
public class GuvnorStatelessKnowledgeSessionService extends StatelessKnowledgeSessionService implements KnowledgeAgentEventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(LocalStatelessKnowledgeSessionService.class);

    @Inject
    private DroolsStaticService droolsStaticService;

    private KnowledgeAgent agent;
    private KnowledgeBase knowledgeBase;
    private Resource changeSet;

    private boolean droolsIsInitialized;

    private Long droolsResourceScannerInterval;
    private String guvnorUrl;
    private String guvnorUser;
    private String guvnorPassword;

    @PreDestroy
    public void preDestroy() {
        endService();
    }

    @Override
    public void afterChangeSetApplied(AfterChangeSetAppliedEvent event) {
    }

    @Override
    public void afterChangeSetProcessed(AfterChangeSetProcessedEvent event) {
    }

    @Override
    public void afterResourceProcessed(AfterResourceProcessedEvent event) {
    }

    @Override
    public void beforeChangeSetApplied(BeforeChangeSetAppliedEvent event) {
    }

    @Override
    public void beforeChangeSetProcessed(BeforeChangeSetProcessedEvent event) {
    }

    @Override
    public void beforeResourceProcessed(BeforeResourceProcessedEvent event) {
    }

    @Override
    public void knowledgeBaseUpdated(KnowledgeBaseUpdatedEvent event) {
        setDroolsIsInitialized(false);

        try {
            rebuildKnowledgeBase();
        } catch (InternalServerErrorException e) {
            LOGGER.error("Couldn't rebuild knowledgebase!", e);
        }
    }

    @Override
    public void resourceCompilationFailed(ResourceCompilationFailedEvent event) {
    }

    synchronized public void useGuvnor(Long droolsResourceScannerInterval, String guvnorUrl, String guvnorUser, String guvnorPassword)
            throws InternalServerErrorException {
        this.droolsResourceScannerInterval = droolsResourceScannerInterval;
        this.guvnorUrl = guvnorUrl;
        this.guvnorUser = guvnorUser;
        this.guvnorPassword = guvnorPassword;

        initDrools();
    }

    @Override
    public void executeInStatelessKnowledgeSession(List<Object> factList) throws InternalServerErrorException {
        makeSureDroolsIsInitialized();
        getStatelessKnowledgeSession().execute(factList);
    }

    @Override
    public void endService() {
        endSessionAndDisposeAgent();
    }

    @POI(
        tags = { POITag.BE_CAREFUL },
        value = "Applying the changeset will result in the synchronous call of DefaultKnowledgeAgentEventListener.knowledgeBaseUpdated. However this might change to an asynchronous call in the future!")
    private void initDrools() throws InternalServerErrorException {
        droolsStaticService.startResourceChangeObserverServices();

        agent = createAgent(droolsResourceScannerInterval);
        agent.addEventListener(this);

        DroolsSystemEventListener droolsSystemEventListener = droolsStaticService.createNewDroolsSystemEventListener();
        agent.setSystemEventListener(droolsSystemEventListener);

        buildChangeSet(guvnorUrl, guvnorUser, guvnorPassword);

        try {
            agent.applyChangeSet(changeSet);
        } catch (Exception e) {
            endService();
            throw new InternalServerErrorException("Couldn't apply changeset!", e);
        }

        if (droolsSystemEventListener.hasException()) {
            endService();

            String lastExceptionMessage = droolsSystemEventListener.getLastExceptionMessage();

            LOGGER.error("Drools exception occurred: {}", lastExceptionMessage);

            throw new InternalServerErrorException(lastExceptionMessage, droolsSystemEventListener.getLastException());
        }
    }

    private boolean makeSureDroolsIsInitialized() throws InternalServerErrorException {
        boolean reInitialized;

        if (!droolsIsInitialized) {
            synchronized (this) {
                if (!droolsIsInitialized) {
                    initDrools();

                    reInitialized = true;
                } else {
                    reInitialized = false;
                }
            }
        } else {
            reInitialized = false;
        }

        return reInitialized;
    }

    private void rebuildKnowledgeBase() throws InternalServerErrorException {
        boolean reInitialized = makeSureDroolsIsInitialized();

        if (!reInitialized) {
            knowledgeBase = agent.getKnowledgeBase();
            setStatelessKnowledgeSession(knowledgeBase.newStatelessKnowledgeSession());

            setLoggerForStatelessKnowledgeSession(droolsStaticService.registerConsoleLogger(getStatelessKnowledgeSession()));

            setDroolsIsInitialized(true);
        }
    }

    private KnowledgeAgent createAgent(Long droolsResourceScannerInterval) {
        ResourceChangeScannerConfiguration resourceChangeScannerConfiguration = droolsStaticService.getNewResourceChangeScannerConfiguration();
        resourceChangeScannerConfiguration.setProperty("drools.resource.scanner.interval", droolsResourceScannerInterval + "");

        droolsStaticService.configureResourceChangeScannerService(resourceChangeScannerConfiguration);

        return droolsStaticService.getNewKnowledgeAgent("KnowledgeAgent");
    }

    private void buildChangeSet(String guvnorUrl, String guvnorUser, String guvnorPassword) {
        String changeSetXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<change-set xmlns='http://drools.org/drools-5.0/change-set' xmlns:xs='http://www.w3.org/2001/XMLSchema-instance' xs:schemaLocation='http://drools.org/drools-5.0/change-set http://anonsvn.jboss.org/repos/labs/labs/jbossrules/trunk/drools-api/src/main/resources/change-set-1.0.0.xsd'>\n"
            + "<add><resource source='" + guvnorUrl + "' type='PKG' basicAuthentication='enabled' username='" + guvnorUser + "' password='" + guvnorPassword
            + "'/></add></change-set>";

        changeSet = droolsStaticService.buildChangeSet(changeSetXml);
    }

    private void endSessionAndDisposeAgent() {
        droolsIsInitialized = false;
        super.endService();
        disposeAgent();
    }

    private void disposeAgent() {
        if (agent != null) {
            agent.dispose();
            agent = null;
        }
    }

    private void setDroolsIsInitialized(boolean droolsServiceInitialized) {
        this.droolsIsInitialized = droolsServiceInitialized;
    }
}
