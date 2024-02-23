package edu.stanford.protege.webprotege.forms;

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

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-01
 */
public class SetEntityFormsDataActionHandler extends AbstractProjectChangeHandler<OWLEntity, SetEntityFormsDataAction, SetEntityFormsDataResult> {

    @Nonnull
    private final EntityFormChangeListGeneratorFactory changeListGeneratorFactory;

    @Inject
    public SetEntityFormsDataActionHandler(@Nonnull AccessManager accessManager,

                                           @Nonnull HasApplyChanges applyChanges,
                                           @Nonnull EntityFormChangeListGeneratorFactory changeListGeneratorFactory) {
        super(accessManager, applyChanges);
        this.changeListGeneratorFactory = checkNotNull(changeListGeneratorFactory);
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(SetEntityFormsDataAction action) {
        return BuiltInAction.EDIT_ONTOLOGY;
    }

    @Override
    protected SetEntityFormsDataResult createActionResult(ChangeApplicationResult<OWLEntity> changeApplicationResult,
                                                          SetEntityFormsDataAction action,
                                                          ExecutionContext executionContext) {
        return new SetEntityFormsDataResult();
    }

    @Nonnull
    @Override
    public Class<SetEntityFormsDataAction> getActionClass() {
        return SetEntityFormsDataAction.class;
    }

    @Override
    protected ChangeListGenerator<OWLEntity> getChangeListGenerator(SetEntityFormsDataAction action,
                                                                    ExecutionContext executionContext) {
        var pristineFormsData = action.pristineFormsData();
        var editedFormsData = action.editedFormsData();
        FormDataUpdateSanityChecker.check(pristineFormsData, editedFormsData);
        var subject = action.entity();
        return changeListGeneratorFactory.create(action.changeRequestId(),
                                                 subject,
                                                 pristineFormsData,
                                                 editedFormsData);
    }
}
