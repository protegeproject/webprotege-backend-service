package edu.stanford.protege.webprotege.form;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-08-21
 */
@WebProtegeHandler
public class SetEntityFormsDataCommandHandler implements CommandHandler<SetEntityFormsDataAction, SetEntityFormsDataResult> {

    private final ActionExecutor executor;

    public SetEntityFormsDataCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return SetEntityFormsDataAction.CHANNEL;
    }

    @Override
    public Class<SetEntityFormsDataAction> getRequestClass() {
        return SetEntityFormsDataAction.class;
    }

    @Override
    public Mono<SetEntityFormsDataResult> handleRequest(SetEntityFormsDataAction request,
                                                        ExecutionContext executionContext) {
        return Mono.just(executor.execute(request, executionContext));
    }
}