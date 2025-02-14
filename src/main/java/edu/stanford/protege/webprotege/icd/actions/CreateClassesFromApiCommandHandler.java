package edu.stanford.protege.webprotege.icd.actions;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.*;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-08-20
 */
@WebProtegeHandler
public class CreateClassesFromApiCommandHandler implements CommandHandler<CreateClassesFromApiAction, CreateClassesFromApiResult> {

    private final ActionExecutor executor;

    public CreateClassesFromApiCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return CreateClassesFromApiAction.CHANNEL;
    }

    @Override
    public Class<CreateClassesFromApiAction> getRequestClass() {
        return CreateClassesFromApiAction.class;
    }

    @Override
    public Mono<CreateClassesFromApiResult> handleRequest(CreateClassesFromApiAction request, ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}