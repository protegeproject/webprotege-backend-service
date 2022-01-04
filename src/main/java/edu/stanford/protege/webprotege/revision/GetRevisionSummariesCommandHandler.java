package edu.stanford.protege.webprotege.revision;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import javax.annotation.Nonnull;
import reactor.core.publisher.Mono;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-08-20
 */
@WebProtegeHandler
public class GetRevisionSummariesCommandHandler implements CommandHandler<GetRevisionSummariesAction, GetRevisionSummariesResult> {

    private final ActionExecutor executor;

    public GetRevisionSummariesCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return GetRevisionSummariesAction.CHANNEL;
    }

    @Override
    public Class<GetRevisionSummariesAction> getRequestClass() {
        return GetRevisionSummariesAction.class;
    }

    @Override
    public Mono<GetRevisionSummariesResult> handleRequest(GetRevisionSummariesAction request,
                                                          ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}