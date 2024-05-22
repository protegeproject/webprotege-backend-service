package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.authorization.ActionId;
import edu.stanford.protege.webprotege.authorization.ApplicationResource;
import edu.stanford.protege.webprotege.authorization.Resource;
import edu.stanford.protege.webprotege.ipc.*;
import edu.stanford.protege.webprotege.permissions.PermissionDeniedException;
import javax.annotation.Nonnull;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;

import static edu.stanford.protege.webprotege.access.BuiltInAction.CREATE_EMPTY_PROJECT;
import static edu.stanford.protege.webprotege.access.BuiltInAction.UPLOAD_PROJECT;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-08-19
 */
@WebProtegeHandler
public class CreateNewProjectCommandHandler implements AuthorizedCommandHandler<CreateNewProjectAction, CreateNewProjectResult> {

    private final CreateProjectSagaManager createProjectSagaManager;

    public CreateNewProjectCommandHandler(CreateProjectSagaManager createProjectSagaManager) {
        this.createProjectSagaManager = createProjectSagaManager;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return CreateNewProjectAction.CHANNEL;
    }

    @Override
    public Class<CreateNewProjectAction> getRequestClass() {
        return CreateNewProjectAction.class;
    }

    @Override
    public Mono<CreateNewProjectResult> handleRequest(CreateNewProjectAction request,
                                                      ExecutionContext executionContext) {
        var result = createProjectSagaManager.execute(request.newProjectSettings(), executionContext);
        return Mono.fromFuture(result);
    }

    @Nonnull
    @Override
    public Resource getTargetResource(CreateNewProjectAction request) {
        return ApplicationResource.get();
    }

    @Nonnull
    @Override
    public Collection<ActionId> getRequiredCapabilities() {
        return List.of(CREATE_EMPTY_PROJECT.getActionId(), UPLOAD_PROJECT.getActionId());
    }


}
