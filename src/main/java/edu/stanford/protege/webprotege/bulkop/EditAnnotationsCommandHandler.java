package edu.stanford.protege.webprotege.bulkop;

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
public class EditAnnotationsCommandHandler implements CommandHandler<EditAnnotationsAction, EditAnnotationsResult> {

    private final ActionExecutor executor;

    public EditAnnotationsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return EditAnnotationsAction.CHANNEL;
    }

    @Override
    public Class<EditAnnotationsAction> getRequestClass() {
        return EditAnnotationsAction.class;
    }

    @Override
    public Mono<EditAnnotationsResult> handleRequest(EditAnnotationsAction request, ExecutionContext executionContext) {
        return Mono.just(executor.execute(request, executionContext));
    }
}