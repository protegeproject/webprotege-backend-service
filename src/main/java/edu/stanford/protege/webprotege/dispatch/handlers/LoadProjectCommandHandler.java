package edu.stanford.protege.webprotege.dispatch.handlers;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import edu.stanford.protege.webprotege.project.LoadProjectAction;
import edu.stanford.protege.webprotege.project.LoadProjectResult;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-11-01
 */
@WebProtegeHandler
public class LoadProjectCommandHandler implements CommandHandler<LoadProjectAction, LoadProjectResult> {

    private final ActionExecutor actionExecutor;

    public LoadProjectCommandHandler(ActionExecutor actionExecutor) {
        this.actionExecutor = actionExecutor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return LoadProjectAction.CHANNEL;
    }

    @Override
    public Class<LoadProjectAction> getRequestClass() {
        return LoadProjectAction.class;
    }

    @Override
    public Mono<LoadProjectResult> handleRequest(LoadProjectAction request, ExecutionContext executionContext) {
        return actionExecutor.executeRequest(request, executionContext);
    }
}
