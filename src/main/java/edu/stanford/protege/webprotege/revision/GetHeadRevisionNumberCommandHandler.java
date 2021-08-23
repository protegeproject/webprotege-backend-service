package edu.stanford.protege.webprotege.revision;

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
public class GetHeadRevisionNumberCommandHandler implements CommandHandler<GetHeadRevisionNumberAction, GetHeadRevisionNumberResult> {

    private final ActionExecutor executor;

    public GetHeadRevisionNumberCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return GetHeadRevisionNumberAction.CHANNEL;
    }

    @Override
    public Class<GetHeadRevisionNumberAction> getRequestClass() {
        return GetHeadRevisionNumberAction.class;
    }

    @Override
    public Mono<GetHeadRevisionNumberResult> handleRequest(GetHeadRevisionNumberAction request,
                                                           ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}