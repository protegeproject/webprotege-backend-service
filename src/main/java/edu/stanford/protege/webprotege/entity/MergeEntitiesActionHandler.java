package edu.stanford.protege.webprotege.entity;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.change.ChangeApplicationResult;
import edu.stanford.protege.webprotege.change.ChangeListGenerator;
import edu.stanford.protege.webprotege.change.HasApplyChanges;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectChangeHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.protege.webprotege.access.BuiltInAction.MERGE_ENTITIES;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 9 Mar 2018
 */
public class MergeEntitiesActionHandler extends AbstractProjectChangeHandler<OWLEntity, MergeEntitiesAction, MergeEntitiesResult> {

    @Nonnull
    private final MergeEntitiesChangeListGeneratorFactory factory;

    @Inject
    public MergeEntitiesActionHandler(@Nonnull AccessManager accessManager,

                                      @Nonnull HasApplyChanges applyChanges,
                                      @Nonnull MergeEntitiesChangeListGeneratorFactory factory) {
        super(accessManager, applyChanges);
        this.factory = checkNotNull(factory);
    }

    @Nonnull
    @Override
    public Class<MergeEntitiesAction> getActionClass() {
        return MergeEntitiesAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(MergeEntitiesAction action) {
        return MERGE_ENTITIES;
    }

    @Override
    protected ChangeListGenerator<OWLEntity> getChangeListGenerator(MergeEntitiesAction action, ExecutionContext executionContext) {
        return factory.create(action.changeRequestId(),
                              action.sourceEntities(),
                              action.targetEntity(),
                              action.treatment(),
                              action.commitMessage());
    }

    @Override
    protected MergeEntitiesResult createActionResult(ChangeApplicationResult<OWLEntity> changeApplicationResult,
                                                     MergeEntitiesAction action,
                                                     ExecutionContext executionContext) {

        return new MergeEntitiesResult();
    }
}
