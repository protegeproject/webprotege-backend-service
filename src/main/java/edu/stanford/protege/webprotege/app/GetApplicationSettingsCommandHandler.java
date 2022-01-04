package edu.stanford.protege.webprotege.app;

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
public class GetApplicationSettingsCommandHandler implements CommandHandler<GetApplicationSettingsAction, GetApplicationSettingsResult> {

    private final ActionExecutor executor;

    public GetApplicationSettingsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return GetApplicationSettingsAction.CHANNEL;
    }

    @Override
    public Class<GetApplicationSettingsAction> getRequestClass() {
        return GetApplicationSettingsAction.class;
    }

    @Override
    public Mono<GetApplicationSettingsResult> handleRequest(GetApplicationSettingsAction request,
                                                            ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}