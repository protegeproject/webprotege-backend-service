package edu.stanford.protege.webprotege.axioms;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-09-01
 */
@WebProtegeHandler
public class AddAxiomsCommandHandler implements CommandHandler<AddAxiomsRequest, AddAxiomsResponse> {

    private final ActionExecutor executor;

    public AddAxiomsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Override
    public String getChannelName() {
        return AddAxiomsRequest.CHANNEL;
    }

    @Override
    public Class<AddAxiomsRequest> getRequestClass() {
        return AddAxiomsRequest.class;
    }

    @Override
    public Mono<AddAxiomsResponse> handleRequest(AddAxiomsRequest request, ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}
