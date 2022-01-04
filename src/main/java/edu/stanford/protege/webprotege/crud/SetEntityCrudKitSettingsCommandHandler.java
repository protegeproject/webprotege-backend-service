package edu.stanford.protege.webprotege.crud;

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
public class SetEntityCrudKitSettingsCommandHandler implements CommandHandler<SetEntityCrudKitSettingsAction, SetEntityCrudKitSettingsResult> {

    private final ActionExecutor executor;

    public SetEntityCrudKitSettingsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return SetEntityCrudKitSettingsAction.CHANNEL;
    }

    @Override
    public Class<SetEntityCrudKitSettingsAction> getRequestClass() {
        return SetEntityCrudKitSettingsAction.class;
    }

    @Override
    public Mono<SetEntityCrudKitSettingsResult> handleRequest(SetEntityCrudKitSettingsAction request,
                                                              ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}