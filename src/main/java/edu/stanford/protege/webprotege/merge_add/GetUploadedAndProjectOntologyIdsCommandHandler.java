package edu.stanford.protege.webprotege.merge_add;

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
public class GetUploadedAndProjectOntologyIdsCommandHandler implements CommandHandler<GetUploadedAndProjectOntologyIdsAction, GetUploadedAndProjectOntologyIdsResult> {

    private final ActionExecutor executor;

    public GetUploadedAndProjectOntologyIdsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return GetUploadedAndProjectOntologyIdsAction.CHANNEL;
    }

    @Override
    public Class<GetUploadedAndProjectOntologyIdsAction> getRequestClass() {
        return GetUploadedAndProjectOntologyIdsAction.class;
    }

    @Override
    public Mono<GetUploadedAndProjectOntologyIdsResult> handleRequest(GetUploadedAndProjectOntologyIdsAction request,
                                                                      ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}