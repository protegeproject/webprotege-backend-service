package edu.stanford.protege.webprotege.form;

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

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-01
 */
public class SetEntityFormsDataActionHandler extends AbstractProjectChangeHandler<OWLEntity, SetEntityFormsDataAction, SetEntityFormDataResult> {

    @Nonnull
    private final EntityFormChangeListGeneratorFactory changeListGeneratorFactory;

    @Inject
    public SetEntityFormsDataActionHandler(@Nonnull AccessManager accessManager,
                                           @Nonnull EventManager<ProjectEvent<?>> eventManager,
                                           @Nonnull HasApplyChanges applyChanges,
                                           @Nonnull EntityFormChangeListGeneratorFactory changeListGeneratorFactory) {
        super(accessManager, eventManager, applyChanges);
        this.changeListGeneratorFactory = checkNotNull(changeListGeneratorFactory);
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(SetEntityFormsDataAction action) {
        return BuiltInAction.EDIT_ONTOLOGY;
    }

    @Override
    protected SetEntityFormDataResult createActionResult(ChangeApplicationResult<OWLEntity> changeApplicationResult,
                                                         SetEntityFormsDataAction action,
                                                         ExecutionContext executionContext,
                                                         EventList<ProjectEvent<?>> eventList) {
        return new SetEntityFormDataResult(eventList);
    }

    @Nonnull
    @Override
    public Class<SetEntityFormsDataAction> getActionClass() {
        return SetEntityFormsDataAction.class;
    }

    @Override
    protected ChangeListGenerator<OWLEntity> getChangeListGenerator(SetEntityFormsDataAction action,
                                                                    ExecutionContext executionContext) {


        var pristineFormsData = action.getPristineFormsData();
        var editedFormsData = action.getEditedFormsData();
        FormDataUpdateSanityChecker.check(pristineFormsData, editedFormsData);
        var subject = action.getEntity();
        return changeListGeneratorFactory.create(subject,
                                                 pristineFormsData,
                                                 editedFormsData);
    }
}
