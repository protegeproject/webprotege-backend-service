package edu.stanford.protege.webprotege.perspective;

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
public class GetPerspectiveLayoutCommandHandler implements CommandHandler<GetPerspectiveLayoutAction, GetPerspectiveLayoutResult> {

    private final ActionExecutor executor;

    public GetPerspectiveLayoutCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return GetPerspectiveLayoutAction.CHANNEL;
    }

    @Override
    public Class<GetPerspectiveLayoutAction> getRequestClass() {
        return GetPerspectiveLayoutAction.class;
    }

    @Override
    public Mono<GetPerspectiveLayoutResult> handleRequest(GetPerspectiveLayoutAction request,
                                                          ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}