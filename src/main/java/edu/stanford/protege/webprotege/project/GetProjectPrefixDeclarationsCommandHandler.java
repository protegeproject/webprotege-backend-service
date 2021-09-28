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
 * 2021-08-20
 */
@WebProtegeHandler
public class GetProjectPrefixDeclarationsCommandHandler implements CommandHandler<GetProjectPrefixDeclarationsRequest, GetProjectPrefixDeclarationsResponse> {

    private final ActionExecutor executor;

    public GetProjectPrefixDeclarationsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return GetProjectPrefixDeclarationsRequest.CHANNEL;
    }

    @Override
    public Class<GetProjectPrefixDeclarationsRequest> getRequestClass() {
        return GetProjectPrefixDeclarationsRequest.class;
    }

    @Override
    public Mono<GetProjectPrefixDeclarationsResponse> handleRequest(GetProjectPrefixDeclarationsRequest request,
                                                                  ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}