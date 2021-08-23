package edu.stanford.protege.webprotege.entity;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-08-20
 */
@WebProtegeHandler
public class CreateClassesCommandHandler implements CommandHandler<CreateClassesAction, CreateClassesResult> {

    private final ActionExecutor executor;

    public CreateClassesCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
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
}