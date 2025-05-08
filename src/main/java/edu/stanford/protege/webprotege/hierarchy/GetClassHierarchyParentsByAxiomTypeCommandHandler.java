package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.*;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;


@WebProtegeHandler
public class GetClassHierarchyParentsByAxiomTypeCommandHandler implements CommandHandler<GetClassHierarchyParentsByAxiomTypeAction, GetClassHierarchyParentsByAxiomTypeResult> {

    private final ActionExecutor executor;

    public GetClassHierarchyParentsByAxiomTypeCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return GetClassHierarchyParentsByAxiomTypeAction.CHANNEL;
    }

    @Override
    public Class<GetClassHierarchyParentsByAxiomTypeAction> getRequestClass() {
        return GetClassHierarchyParentsByAxiomTypeAction.class;
    }

    @Override
    public Mono<GetClassHierarchyParentsByAxiomTypeResult> handleRequest(GetClassHierarchyParentsByAxiomTypeAction request,
                                                          ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}