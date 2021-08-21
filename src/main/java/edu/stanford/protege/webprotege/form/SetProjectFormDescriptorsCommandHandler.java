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
public class SetProjectFormDescriptorsCommandHandler implements CommandHandler<SetProjectFormDescriptorsAction, SetProjectFormDescriptorsResult> {

    private final ActionExecutor executor;

    public SetProjectFormDescriptorsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return SetProjectFormDescriptorsAction.CHANNEL;
    }

    @Override
    public Class<SetProjectFormDescriptorsAction> getRequestClass() {
        return SetProjectFormDescriptorsAction.class;
    }

    @Override
    public Mono<SetProjectFormDescriptorsResult> handleRequest(SetProjectFormDescriptorsAction request,
                                                               ExecutionContext executionContext) {
        return Mono.just(executor.execute(request, executionContext));
    }
}