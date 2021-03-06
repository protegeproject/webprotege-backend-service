package edu.stanford.protege.webprotege.entity;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import javax.annotation.Nonnull;
import reactor.core.publisher.Mono;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-08-20
 */
@WebProtegeHandler
public class CreateObjectPropertiesCommandHandler implements CommandHandler<CreateObjectPropertiesAction, CreateObjectPropertiesResult> {

    private final ActionExecutor executor;

    public CreateObjectPropertiesCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return CreateObjectPropertiesAction.CHANNEL;
    }

    @Override
    public Class<CreateObjectPropertiesAction> getRequestClass() {
        return CreateObjectPropertiesAction.class;
    }

    @Override
    public Mono<CreateObjectPropertiesResult> handleRequest(CreateObjectPropertiesAction request,
                                                            ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}