package edu.stanford.protege.webprotege.projectsettings;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.dispatch.DispatchServiceExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-08-19
 */
@WebProtegeHandler
public class GetProjectSettingsCommandHandler implements CommandHandler<GetProjectSettingsAction, GetProjectSettingsResult> {

    private final ActionExecutor executor;

    public GetProjectSettingsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return GetProjectSettingsAction.CHANNEL;
    }

    @Override
    public Class<GetProjectSettingsAction> getRequestClass() {
        return GetProjectSettingsAction.class;
    }

    @Override
    public Mono<GetProjectSettingsResult> handleRequest(GetProjectSettingsAction request,
                                                        ExecutionContext executionContext) {
        var result = executor.execute(request, executionContext);
        return Mono.just(result);
    }
}
