package spring.reference.service.drools;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.ResourceType;
import org.drools.io.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spring.reference.exception.InternalServerErrorException;
import spring.reference.util.Logged;

@Logged
@Named
public class LocalStatelessKnowledgeSessionService extends StatelessKnowledgeSessionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LocalStatelessKnowledgeSessionService.class);
    @Inject
    private DroolsStaticService droolsStaticService;

    @PostConstruct
    public void init() {
        KnowledgeBuilder knowledgeBuilder = droolsStaticService.createNewKnowledgeBuilder();
        Resource rule = droolsStaticService.createRuleResource("drools/filterDeletedPeople.drl");
        knowledgeBuilder.add(rule, ResourceType.DRL);

        if (knowledgeBuilder.hasErrors()) {
            for (KnowledgeBuilderError knowledgeBuilderError : knowledgeBuilder.getErrors()) {
                LOGGER.error(knowledgeBuilderError.getMessage());
            }
            throw new IllegalStateException("Couldn't build Drools knowledgebase!");
        }

        KnowledgeBase knowledgeBase = knowledgeBuilder.newKnowledgeBase();
        setStatelessKnowledgeSession(knowledgeBase.newStatelessKnowledgeSession());
        setLoggerForStatelessKnowledgeSession(droolsStaticService.registerConsoleLogger(getStatelessKnowledgeSession()));
    }

    @PreDestroy
    public void preDestroy() {
        endService();
    }

    @Override
    public void executeInStatelessKnowledgeSession(List<Object> factList) throws InternalServerErrorException {
        getStatelessKnowledgeSession().execute(factList);
    }
}
