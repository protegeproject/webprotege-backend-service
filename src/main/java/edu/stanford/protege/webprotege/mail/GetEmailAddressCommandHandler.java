package edu.stanford.protege.webprotege.mail;

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
public class GetEmailAddressCommandHandler implements CommandHandler<GetEmailAddressAction, GetEmailAddressResult> {

    private final ActionExecutor executor;

    public GetEmailAddressCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return GetEmailAddressAction.CHANNEL;
    }

    @Override
    public Class<GetEmailAddressAction> getRequestClass() {
        return GetEmailAddressAction.class;
    }

    @Override
    public Mono<GetEmailAddressResult> handleRequest(GetEmailAddressAction request, ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}