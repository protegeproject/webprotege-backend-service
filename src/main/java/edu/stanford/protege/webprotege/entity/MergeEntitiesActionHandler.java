package edu.stanford.protege.webprotege.entity;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.change.ChangeApplicationResult;
import edu.stanford.protege.webprotege.change.ChangeListGenerator;
import edu.stanford.protege.webprotege.change.HasApplyChanges;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectChangeHandler;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import edu.stanford.protege.webprotege.event.EventList;
import edu.stanford.protege.webprotege.event.ProjectEvent;
import edu.stanford.protege.webprotege.events.EventManager;
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
                                      @Nonnull EventManager<ProjectEvent<?>> eventManager,
                                      @Nonnull HasApplyChanges applyChanges,
                                      @Nonnull MergeEntitiesChangeListGeneratorFactory factory) {
        super(accessManager, eventManager, applyChanges);
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
        return factory.create(action.getSourceEntities(),
                              action.getTargetEntity(),
                              action.getTreatment(),
                              action.getCommitMessage());
    }

    @Override
    protected MergeEntitiesResult createActionResult(ChangeApplicationResult<OWLEntity> changeApplicationResult, MergeEntitiesAction action,
                                                     ExecutionContext executionContext,
                                                     EventList<ProjectEvent<?>> eventList) {

        return MergeEntitiesResult.create(eventList);
    }
}
