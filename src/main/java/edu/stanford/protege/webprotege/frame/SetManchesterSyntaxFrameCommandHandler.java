package edu.stanford.protege.webprotege.frame;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import javax.annotation.Nonnull;
import reactor.core.publisher.Mono;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-08-21
 */
@WebProtegeHandler
public class SetManchesterSyntaxFrameCommandHandler implements CommandHandler<SetManchesterSyntaxFrameAction, SetManchesterSyntaxFrameResult> {

    private final ActionExecutor executor;

    public SetManchesterSyntaxFrameCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return SetManchesterSyntaxFrameAction.CHANNEL;
    }

    @Override
    public Class<SetManchesterSyntaxFrameAction> getRequestClass() {
        return SetManchesterSyntaxFrameAction.class;
    }

    @Override
    public Mono<SetManchesterSyntaxFrameResult> handleRequest(SetManchesterSyntaxFrameAction request,
                                                              ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}