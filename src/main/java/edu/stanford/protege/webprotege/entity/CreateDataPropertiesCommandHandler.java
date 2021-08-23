package edu.stanford.protege.webprotege.entity;

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
public class CreateDataPropertiesCommandHandler implements CommandHandler<CreateDataPropertiesAction, CreateDataPropertiesResult> {

    private final ActionExecutor executor;

    public CreateDataPropertiesCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return CreateDataPropertiesAction.CHANNEL;
    }

    @Override
    public Class<CreateDataPropertiesAction> getRequestClass() {
        return CreateDataPropertiesAction.class;
    }

    @Override
    public Mono<CreateDataPropertiesResult> handleRequest(CreateDataPropertiesAction request,
                                                          ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}