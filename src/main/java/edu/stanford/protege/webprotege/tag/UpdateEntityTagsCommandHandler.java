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
public class UpdateEntityTagsCommandHandler implements CommandHandler<UpdateEntityTagsAction, UpdateEntityTagsResult> {

    private final ActionExecutor executor;

    public UpdateEntityTagsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return UpdateEntityTagsAction.CHANNEL;
    }

    @Override
    public Class<UpdateEntityTagsAction> getRequestClass() {
        return UpdateEntityTagsAction.class;
    }

    @Override
    public Mono<UpdateEntityTagsResult> handleRequest(UpdateEntityTagsAction request,
                                                      ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}