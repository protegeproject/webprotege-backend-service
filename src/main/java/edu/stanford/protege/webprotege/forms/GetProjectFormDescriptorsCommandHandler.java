package edu.stanford.protege.webprotege.forms;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-08-20
 */
@WebProtegeHandler
public class GetProjectFormDescriptorsCommandHandler implements CommandHandler<GetProjectFormDescriptorsAction, GetProjectFormDescriptorsResult> {

    private final ActionExecutor executor;

    public GetProjectFormDescriptorsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return GetProjectFormDescriptorsAction.CHANNEL;
    }

    @Override
    public Class<GetProjectFormDescriptorsAction> getRequestClass() {
        return GetProjectFormDescriptorsAction.class;
    }

    @Override
    public Mono<GetProjectFormDescriptorsResult> handleRequest(GetProjectFormDescriptorsAction request,
                                                               ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}