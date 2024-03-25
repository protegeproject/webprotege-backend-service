package edu.stanford.protege.webprotege.bulkop;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.change.ChangeApplicationResult;
import edu.stanford.protege.webprotege.change.ChangeListGenerator;
import edu.stanford.protege.webprotege.change.HasApplyChanges;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectChangeHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Sep 2018
 */
public class SetAnnotationValueActionHandler extends AbstractProjectChangeHandler<Set<OWLEntity>, SetAnnotationValueAction, SetAnnotationValueResult> {

    @Nonnull
    private final SetAnnotationValueActionChangeListGeneratorFactory factory;

    @Inject
    public SetAnnotationValueActionHandler(@Nonnull AccessManager accessManager,

                                           @Nonnull HasApplyChanges applyChanges,
                                           @Nonnull SetAnnotationValueActionChangeListGeneratorFactory factory) {
        super(accessManager, applyChanges);
        this.factory = checkNotNull(factory);
    }

    @Nonnull
    @Override
    public Class<SetAnnotationValueAction> getActionClass() {
        return SetAnnotationValueAction.class;
    }

    @Override
    protected ChangeListGenerator<Set<OWLEntity>> getChangeListGenerator(SetAnnotationValueAction action, ExecutionContext executionContext) {
        return factory.create(action.changeRequestId(), action.entities(),
                              action.property(),
                              action.value(),
                              action.commitMessage());
    }

    @Override
    protected SetAnnotationValueResult createActionResult(ChangeApplicationResult<Set<OWLEntity>> changeApplicationResult,
                                                          SetAnnotationValueAction action,
                                                          ExecutionContext executionContext) {
        return new SetAnnotationValueResult();
    }
}
