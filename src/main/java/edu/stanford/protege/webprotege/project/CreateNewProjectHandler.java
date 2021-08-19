package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-08-19
 */
@WebProtegeHandler
public class CreateNewProjectHandler implements CommandHandler<CreateNewProjectAction, CreateNewProjectResult> {

    private final ActionExecutor executor;

    public CreateNewProjectHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
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
        return Mono.just(executor.execute(request, executionContext));
    }
}
