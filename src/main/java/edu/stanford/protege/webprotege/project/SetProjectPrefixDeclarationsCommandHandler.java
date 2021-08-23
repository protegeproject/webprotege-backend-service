package edu.stanford.protege.webprotege.project;

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
public class SetProjectPrefixDeclarationsCommandHandler implements CommandHandler<SetProjectPrefixDeclarationsAction, SetProjectPrefixDeclarationsResult> {

    private final ActionExecutor executor;

    public SetProjectPrefixDeclarationsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return SetProjectPrefixDeclarationsAction.CHANNEL;
    }

    @Override
    public Class<SetProjectPrefixDeclarationsAction> getRequestClass() {
        return SetProjectPrefixDeclarationsAction.class;
    }

    @Override
    public Mono<SetProjectPrefixDeclarationsResult> handleRequest(SetProjectPrefixDeclarationsAction request,
                                                                  ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}