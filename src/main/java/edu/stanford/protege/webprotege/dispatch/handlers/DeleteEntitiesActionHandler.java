package edu.stanford.protege.webprotege.dispatch.handlers;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.change.ChangeApplicationResult;
import edu.stanford.protege.webprotege.change.ChangeListGenerator;
import edu.stanford.protege.webprotege.change.HasApplyChanges;
import edu.stanford.protege.webprotege.crud.DeleteEntitiesChangeListGeneratorFactory;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectChangeHandler;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import edu.stanford.protege.webprotege.entity.DeleteEntitiesAction;
import edu.stanford.protege.webprotege.entity.DeleteEntitiesResult;
import edu.stanford.protege.webprotege.event.EventList;
import edu.stanford.protege.webprotege.event.ProjectEvent;
import edu.stanford.protege.webprotege.events.EventManager;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 9 May 2017
 */
public class DeleteEntitiesActionHandler extends AbstractProjectChangeHandler<Set<OWLEntity>, DeleteEntitiesAction, DeleteEntitiesResult> {

    @Nonnull
    private final DeleteEntitiesChangeListGeneratorFactory factory;

    @Inject
    public DeleteEntitiesActionHandler(@Nonnull AccessManager accessManager,
                                       @Nonnull EventManager<ProjectEvent<?>> eventManager,
                                       @Nonnull HasApplyChanges applyChanges,
                                       @Nonnull DeleteEntitiesChangeListGeneratorFactory factory) {
        super(accessManager, eventManager, applyChanges);
        this.factory = factory;
    }

    @Nonnull
    @Override
    public Class<DeleteEntitiesAction> getActionClass() {
        return DeleteEntitiesAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(DeleteEntitiesAction action) {
        return BuiltInAction.EDIT_ONTOLOGY;
    }

    @Override
    protected ChangeListGenerator<Set<OWLEntity>> getChangeListGenerator(DeleteEntitiesAction action,
                                                                         ExecutionContext executionContext) {

        return factory.create(action.getEntities());
    }

    @Override
    protected DeleteEntitiesResult createActionResult(ChangeApplicationResult<Set<OWLEntity>> changeApplicationResult,
                                                      DeleteEntitiesAction action,
                                                      ExecutionContext executionContext,
                                                      EventList<ProjectEvent<?>> eventList) {
        return new DeleteEntitiesResult(eventList,
                                        changeApplicationResult.getSubject());
    }
}
