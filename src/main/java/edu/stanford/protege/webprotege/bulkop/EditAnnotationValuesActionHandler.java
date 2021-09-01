package edu.stanford.protege.webprotege.bulkop;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.change.ChangeApplicationResult;
import edu.stanford.protege.webprotege.change.ChangeListGenerator;
import edu.stanford.protege.webprotege.change.HasApplyChanges;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectChangeHandler;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import edu.stanford.protege.webprotege.event.EventList;
import edu.stanford.protege.webprotege.common.ProjectEvent;
import edu.stanford.protege.webprotege.events.EventManager;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Sep 2018
 */

public class EditAnnotationValuesActionHandler extends AbstractProjectChangeHandler<Boolean, EditAnnotationsAction, EditAnnotationsResult> {

    @Nonnull
    private final EditAnnotationsChangeListGeneratorFactory factory;

    @Inject
    public EditAnnotationValuesActionHandler(@Nonnull AccessManager accessManager,
                                             @Nonnull EventManager<ProjectEvent> eventManager,
                                             @Nonnull HasApplyChanges applyChanges,
                                             @Nonnull EditAnnotationsChangeListGeneratorFactory factory) {
        super(accessManager, eventManager, applyChanges);
        this.factory = factory;
    }

    @Nonnull
    @Override
    public Class<EditAnnotationsAction> getActionClass() {
        return EditAnnotationsAction.class;
    }

    @Override
    protected ChangeListGenerator<Boolean> getChangeListGenerator(EditAnnotationsAction action, ExecutionContext executionContext) {
        return factory.create(action.entities(),
                              action.operation(),
                              action.property(),
                              action.lexicalValueExpression(),
                              action.lexicalValueExpressionIsRegEx(),
                              action.langTagExpression(),
                              action.newAnnotationData(),
                              action.commitMessage());
    }

    @Override
    protected EditAnnotationsResult createActionResult(ChangeApplicationResult<Boolean> changeApplicationResult, EditAnnotationsAction action, ExecutionContext executionContext, EventList<ProjectEvent> eventList) {
        return new EditAnnotationsResult();
    }
}
