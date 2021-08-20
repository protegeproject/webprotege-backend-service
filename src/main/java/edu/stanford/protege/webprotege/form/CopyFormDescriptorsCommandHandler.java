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
 * 2021-08-20
 */
@WebProtegeHandler
public class CopyFormDescriptorsCommandHandler implements CommandHandler<CopyFormDescriptorsAction, CopyFormDescriptorsResult> {

    private final ActionExecutor executor;

    public CopyFormDescriptorsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return CopyFormDescriptorsAction.CHANNEL;
    }

    @Override
    public Class<CopyFormDescriptorsAction> getRequestClass() {
        return CopyFormDescriptorsAction.class;
    }

    @Override
    public Mono<CopyFormDescriptorsResult> handleRequest(CopyFormDescriptorsAction request,
                                                         ExecutionContext executionContext) {
        return Mono.just(executor.execute(request, executionContext));
    }
}