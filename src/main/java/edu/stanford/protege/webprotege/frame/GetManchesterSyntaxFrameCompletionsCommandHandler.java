package edu.stanford.protege.webprotege.frame;

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
public class GetManchesterSyntaxFrameCompletionsCommandHandler implements CommandHandler<GetManchesterSyntaxFrameCompletionsAction, GetManchesterSyntaxFrameCompletionsResult> {

    private final ActionExecutor executor;

    public GetManchesterSyntaxFrameCompletionsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return GetManchesterSyntaxFrameCompletionsAction.CHANNEL;
    }

    @Override
    public Class<GetManchesterSyntaxFrameCompletionsAction> getRequestClass() {
        return GetManchesterSyntaxFrameCompletionsAction.class;
    }

    @Override
    public Mono<GetManchesterSyntaxFrameCompletionsResult> handleRequest(GetManchesterSyntaxFrameCompletionsAction request,
                                                                         ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}