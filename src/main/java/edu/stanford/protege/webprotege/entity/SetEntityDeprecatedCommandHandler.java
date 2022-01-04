package edu.stanford.protege.webprotege.entity;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import javax.annotation.Nonnull;
import reactor.core.publisher.Mono;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-09-01
 */
@WebProtegeHandler
public class SetEntityDeprecatedCommandHandler implements CommandHandler<SetEntityDeprecatedRequest, SetEntityDeprecatedResponse> {

    private final ActionExecutor executor;

    public SetEntityDeprecatedCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return SetEntityDeprecatedRequest.CHANNEL;
    }

    @Override
    public Class<SetEntityDeprecatedRequest> getRequestClass() {
        return SetEntityDeprecatedRequest.class;
    }

    @Override
    public Mono<SetEntityDeprecatedResponse> handleRequest(SetEntityDeprecatedRequest request,
                                                         ExecutionContext executionContext) {
        return Mono.just(executor.execute(request, executionContext));
    }
}