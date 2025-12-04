package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;


@WebProtegeHandler
public class SetProjectUnderMaintenanceCommandHandler  implements CommandHandler<SetProjectUnderMaintenanceAction, SetProjectUnderMaintenanceResult> {

    private final ActionExecutor executor;

    public SetProjectUnderMaintenanceCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }


    @NotNull
    @Override
    public String getChannelName() {
        return SetProjectUnderMaintenanceAction.CHANNEL;
    }

    @Override
    public Class<SetProjectUnderMaintenanceAction> getRequestClass() {
        return SetProjectUnderMaintenanceAction.class;
    }

    @Override
    public Mono<SetProjectUnderMaintenanceResult> handleRequest(SetProjectUnderMaintenanceAction request, ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}
