package edu.stanford.protege.webprotege.user;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import javax.annotation.Nonnull;
import reactor.core.publisher.Mono;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-10-29
 */
@WebProtegeHandler
public class GetAuthenticatedUserDetailsCommandHandler implements CommandHandler<GetAuthenticatedUserDetailsRequest, GetAuthenticatedUserDetailsResponse> {

    private final ActionExecutor executor;

    public GetAuthenticatedUserDetailsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return GetAuthenticatedUserDetailsRequest.CHANNEL;
    }

    @Override
    public Class<GetAuthenticatedUserDetailsRequest> getRequestClass() {
        return GetAuthenticatedUserDetailsRequest.class;
    }

    @Override
    public Mono<GetAuthenticatedUserDetailsResponse> handleRequest(GetAuthenticatedUserDetailsRequest request,
                                                                   ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}
