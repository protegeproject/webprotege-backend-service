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
public class SetEntityFormDescriptorCommandHandler implements CommandHandler<SetEntityFormDescriptorAction, SetEntityFormDescriptorResult> {

    private final ActionExecutor executor;

    public SetEntityFormDescriptorCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return SetEntityFormDescriptorAction.CHANNEL;
    }

    @Override
    public Class<SetEntityFormDescriptorAction> getRequestClass() {
        return SetEntityFormDescriptorAction.class;
    }

    @Override
    public Mono<SetEntityFormDescriptorResult> handleRequest(SetEntityFormDescriptorAction request,
                                                             ExecutionContext executionContext) {
        return Mono.just(executor.execute(request, executionContext));
    }
}