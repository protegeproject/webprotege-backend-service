package edu.stanford.protege.webprotege.tag;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-08-21
 */
@WebProtegeHandler
public class SetProjectTagsCommandHandler implements CommandHandler<SetProjectTagsAction, SetProjectTagsResult> {

    private final ActionExecutor executor;

    public SetProjectTagsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return SetProjectTagsAction.CHANNEL;
    }

    @Override
    public Class<SetProjectTagsAction> getRequestClass() {
        return SetProjectTagsAction.class;
    }

    @Override
    public Mono<SetProjectTagsResult> handleRequest(SetProjectTagsAction request, ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}