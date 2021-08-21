package edu.stanford.protege.webprotege.app;

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
public class SetApplicationSettingsCommandHandler implements CommandHandler<SetApplicationSettingsAction, SetApplicationSettingsResult> {

    private final ActionExecutor executor;

    public SetApplicationSettingsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return SetApplicationSettingsAction.CHANNEL;
    }

    @Override
    public Class<SetApplicationSettingsAction> getRequestClass() {
        return SetApplicationSettingsAction.class;
    }

    @Override
    public Mono<SetApplicationSettingsResult> handleRequest(SetApplicationSettingsAction request,
                                                            ExecutionContext executionContext) {
        return Mono.just(executor.execute(request, executionContext));
    }
}