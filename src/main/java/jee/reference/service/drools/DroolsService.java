package jee.reference.service.drools;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import spring.reference.exception.InternalServerErrorException;
import spring.reference.meta.NOTE;
import spring.reference.meta.NOTETag;
import spring.reference.meta.TODO;
import spring.reference.meta.TODOTag;
import spring.reference.util.Logged;

@Logged
@Named
public class DroolsService {
    @Inject
    private LocalStatelessKnowledgeSessionService localStatelessKnowledgeSessionService;
    @Inject
    private GuvnorStatelessKnowledgeSessionService guvnorStatelessKnowledgeSessionService;

    private StatelessKnowledgeSessionService currentStatelessKnowledgeSessionService;

    @PostConstruct
    public void init() {
        currentStatelessKnowledgeSessionService = localStatelessKnowledgeSessionService;
    }

    @TODO(tags = { TODOTag.UNACCESSABLE, TODOTag.UNTESTED })
    synchronized public void useGuvnor(Long droolsResourceScannerInterval, String guvnorUrl, String guvnorUser, String guvnorPassword)
            throws InternalServerErrorException {

        guvnorStatelessKnowledgeSessionService.useGuvnor(droolsResourceScannerInterval, guvnorUrl, guvnorUser, guvnorPassword);
        if (currentStatelessKnowledgeSessionService != null) {
            currentStatelessKnowledgeSessionService.endService();
        }
        currentStatelessKnowledgeSessionService = guvnorStatelessKnowledgeSessionService;
    }

    synchronized public void useLocal() {
        localStatelessKnowledgeSessionService.init();
        if (currentStatelessKnowledgeSessionService != null) {
            currentStatelessKnowledgeSessionService.endService();
        }
        currentStatelessKnowledgeSessionService = localStatelessKnowledgeSessionService;
    }

    @NOTE(
        tags = { NOTETag.NOT_REAL_ENVIRONMENT_READY },
        value = "In a real environment, this method should actually make sure that (and wait until) the currentStatelessKnowledgeSessionService is actually ready to be used.")
    public void executeInStatelessKnowledgeSession(List<Object> factList) throws InternalServerErrorException {
        currentStatelessKnowledgeSessionService.executeInStatelessKnowledgeSession(factList);
    }
}
