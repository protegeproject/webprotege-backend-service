package edu.stanford.protege.webprotege.obo;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import javax.annotation.Nonnull;
import reactor.core.publisher.Mono;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-08-21
 */
@WebProtegeHandler
public class SetOboTermDefinitionCommandHandler implements CommandHandler<SetOboTermDefinitionAction, SetOboTermDefinitionResult> {

    private final ActionExecutor executor;

    public SetOboTermDefinitionCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return SetOboTermDefinitionAction.CHANNEL;
    }

    @Override
    public Class<SetOboTermDefinitionAction> getRequestClass() {
        return SetOboTermDefinitionAction.class;
    }

    @Override
    public Mono<SetOboTermDefinitionResult> handleRequest(SetOboTermDefinitionAction request,
                                                          ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}