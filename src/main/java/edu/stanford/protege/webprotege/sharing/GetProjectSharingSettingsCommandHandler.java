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
 * 2021-08-20
 */
@WebProtegeHandler
public class GetProjectSharingSettingsCommandHandler implements CommandHandler<GetProjectSharingSettingsAction, GetProjectSharingSettingsResult> {

    private final ActionExecutor executor;

    public GetProjectSharingSettingsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return GetProjectSharingSettingsAction.CHANNEL;
    }

    @Override
    public Class<GetProjectSharingSettingsAction> getRequestClass() {
        return GetProjectSharingSettingsAction.class;
    }

    @Override
    public Mono<GetProjectSharingSettingsResult> handleRequest(GetProjectSharingSettingsAction request,
                                                               ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}