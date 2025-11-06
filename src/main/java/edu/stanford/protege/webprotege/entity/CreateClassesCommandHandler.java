package edu.stanford.protege.webprotege.entity;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.authorization.BasicCapability;
import edu.stanford.protege.webprotege.authorization.Capability;
import edu.stanford.protege.webprotege.authorization.ProjectResource;
import edu.stanford.protege.webprotege.authorization.Resource;
import edu.stanford.protege.webprotege.ipc.AuthorizedCommandHandler;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import javax.annotation.Nonnull;

import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;

import static edu.stanford.protege.webprotege.access.BuiltInCapability.CREATE_CLASS;
import static edu.stanford.protege.webprotege.access.BuiltInCapability.EDIT_ONTOLOGY;
import static java.util.Arrays.asList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-08-20
 */
@WebProtegeHandler
public class CreateClassesCommandHandler implements AuthorizedCommandHandler<CreateClassesAction, CreateClassesResult> {

    private final ActionExecutor executor;

    public CreateClassesCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return CreateClassesAction.CHANNEL;
    }

    @Override
    public Class<CreateClassesAction> getRequestClass() {
        return CreateClassesAction.class;
    }

    @Override
    public Mono<CreateClassesResult> handleRequest(CreateClassesAction request, ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }

    @NotNull
    @Override
    public Resource getTargetResource(CreateClassesAction request) {
        return ProjectResource.forProject(request.projectId());
    }

    @NotNull
    @Override
    public Collection<Capability> getRequiredCapabilities() {
        return asList(CREATE_CLASS.getCapability(), EDIT_ONTOLOGY.getCapability());
    }
}