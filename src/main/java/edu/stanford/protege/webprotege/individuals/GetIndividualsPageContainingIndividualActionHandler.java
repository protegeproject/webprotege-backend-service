package edu.stanford.protege.webprotege.individuals;

import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.common.Page;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.entity.EntityNode;
import edu.stanford.protege.webprotege.entity.EntityNodeRenderer;
import edu.stanford.protege.webprotege.index.IndividualsIndex;
import edu.stanford.protege.webprotege.index.IndividualsQueryResult;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Sep 2018
 */
public class GetIndividualsPageContainingIndividualActionHandler extends AbstractProjectActionHandler<GetIndividualsPageContainingIndividualAction, GetIndividualsPageContainingIndividualResult> {

    @Nonnull
    private final IndividualsIndex individualsIndex;

    @Nonnull
    private final EntityNodeRenderer renderer;

    @Inject
    public GetIndividualsPageContainingIndividualActionHandler(@Nonnull AccessManager accessManager, @Nonnull IndividualsIndex individualsIndex, @Nonnull EntityNodeRenderer renderer) {
        super(accessManager);
        this.individualsIndex = checkNotNull(individualsIndex);
        this.renderer = checkNotNull(renderer);
    }

    @Nonnull
    @Override
    public Class<GetIndividualsPageContainingIndividualAction> getActionClass() {
        return GetIndividualsPageContainingIndividualAction.class;
    }

    @Nonnull
    @Override
    public GetIndividualsPageContainingIndividualResult execute(@Nonnull GetIndividualsPageContainingIndividualAction action, @Nonnull ExecutionContext executionContext) {
        IndividualsQueryResult result = individualsIndex.getIndividualsPageContaining(action.individual(),
                                                                                      action.preferredType(),
                                                                                      action.preferredMode(),
                                                                                      200);
        Page<EntityNode> entityNodesPage = result.getIndividuals().transform(renderer::render);
        ImmutableSet<EntityNode> types =
                individualsIndex
                        .getTypes(action.individual())
                        .map(renderer::render)
                        .collect(toImmutableSet());
        return new GetIndividualsPageContainingIndividualResult(action.individual(),
                                                                entityNodesPage,
                                                                renderer.render(result.getType()),
                                                                result.getMode(),
                                                                types);
    }
}
