package edu.stanford.protege.webprotege.viz;

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
public class SetUserProjectEntityGraphSettingsCommandHandler implements CommandHandler<SetUserProjectEntityGraphSettingsAction, SetUserProjectEntityGraphSettingsResult> {

    private final ActionExecutor executor;

    public SetUserProjectEntityGraphSettingsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return SetUserProjectEntityGraphSettingsAction.CHANNEL;
    }

    @Override
    public Class<SetUserProjectEntityGraphSettingsAction> getRequestClass() {
        return SetUserProjectEntityGraphSettingsAction.class;
    }

    @Override
    public Mono<SetUserProjectEntityGraphSettingsResult> handleRequest(SetUserProjectEntityGraphSettingsAction request,
                                                                       ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}