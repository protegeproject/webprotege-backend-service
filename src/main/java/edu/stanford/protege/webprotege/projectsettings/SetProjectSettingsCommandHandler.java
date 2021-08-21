package edu.stanford.protege.webprotege.projectsettings;
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
public class SetProjectSettingsCommandHandler implements CommandHandler<SetProjectSettingsAction, SetProjectSettingsResult> {

    private final ActionExecutor executor;

    public SetProjectSettingsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return SetProjectSettingsAction.CHANNEL;
    }

    @Override
    public Class<SetProjectSettingsAction> getRequestClass() {
        return SetProjectSettingsAction.class;
    }

    @Override
    public Mono<SetProjectSettingsResult> handleRequest(SetProjectSettingsAction request,
                                             ExecutionContext executionContext) {
        return Mono.just(executor.execute(request, executionContext));
    }
}