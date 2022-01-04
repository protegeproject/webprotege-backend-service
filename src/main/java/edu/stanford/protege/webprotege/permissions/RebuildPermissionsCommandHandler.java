package edu.stanford.protege.webprotege.permissions;

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
public class RebuildPermissionsCommandHandler implements CommandHandler<RebuildPermissionsAction, RebuildPermissionsResult> {

    private final ActionExecutor executor;

    public RebuildPermissionsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return RebuildPermissionsAction.CHANNEL;
    }

    @Override
    public Class<RebuildPermissionsAction> getRequestClass() {
        return RebuildPermissionsAction.class;
    }

    @Override
    public Mono<RebuildPermissionsResult> handleRequest(RebuildPermissionsAction request,
                                                        ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}