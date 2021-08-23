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
public class CheckManchesterSyntaxFrameCommandHandler implements CommandHandler<CheckManchesterSyntaxFrameAction, CheckManchesterSyntaxFrameResult> {

    private final ActionExecutor executor;

    public CheckManchesterSyntaxFrameCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return CheckManchesterSyntaxFrameAction.CHANNEL;
    }

    @Override
    public Class<CheckManchesterSyntaxFrameAction> getRequestClass() {
        return CheckManchesterSyntaxFrameAction.class;
    }

    @Override
    public Mono<CheckManchesterSyntaxFrameResult> handleRequest(CheckManchesterSyntaxFrameAction request,
                                                                ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}