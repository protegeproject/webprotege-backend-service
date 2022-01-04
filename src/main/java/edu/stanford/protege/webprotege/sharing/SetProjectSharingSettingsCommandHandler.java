package edu.stanford.protege.webprotege.sharing;

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
public class SetProjectSharingSettingsCommandHandler implements CommandHandler<SetProjectSharingSettingsAction, SetProjectSharingSettingsResult> {

    private final ActionExecutor executor;

    public SetProjectSharingSettingsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return SetProjectSharingSettingsAction.CHANNEL;
    }

    @Override
    public Class<SetProjectSharingSettingsAction> getRequestClass() {
        return SetProjectSharingSettingsAction.class;
    }

    @Override
    public Mono<SetProjectSharingSettingsResult> handleRequest(SetProjectSharingSettingsAction request,
                                                               ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}