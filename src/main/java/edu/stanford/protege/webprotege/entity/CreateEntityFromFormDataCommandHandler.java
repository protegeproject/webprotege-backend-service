package edu.stanford.protege.webprotege.entity;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.forms.CreateEntityFromFormDataAction;
import edu.stanford.protege.webprotege.forms.CreateEntityFromFormDataResult;
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
public class CreateEntityFromFormDataCommandHandler implements CommandHandler<CreateEntityFromFormDataAction, CreateEntityFromFormDataResult> {

    private final ActionExecutor executor;

    public CreateEntityFromFormDataCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return CreateEntityFromFormDataAction.CHANNEL;
    }

    @Override
    public Class<CreateEntityFromFormDataAction> getRequestClass() {
        return CreateEntityFromFormDataAction.class;
    }

    @Override
    public Mono<CreateEntityFromFormDataResult> handleRequest(CreateEntityFromFormDataAction request,
                                                              ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}