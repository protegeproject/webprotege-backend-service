package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.dispatch.ActionExecutionException;
import edu.stanford.protege.webprotege.ipc.CommandExecutionException;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import edu.stanford.protege.webprotege.permissions.PermissionDeniedException;
import javax.annotation.Nonnull;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-08-19
 */
@WebProtegeHandler
public class CreateNewProjectCommandHandler implements CommandHandler<CreateNewProjectAction, CreateNewProjectResult> {

    private final ActionExecutor executor;

    public CreateNewProjectCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return CreateNewProjectAction.CHANNEL;
    }

    @Override
    public Class<CreateNewProjectAction> getRequestClass() {
        return CreateNewProjectAction.class;
    }

    @Override
    public Mono<CreateNewProjectResult> handleRequest(CreateNewProjectAction request,
                                                      ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}
