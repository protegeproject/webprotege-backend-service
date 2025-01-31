package edu.stanford.protege.webprotege.bulkop;

import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.change.ChangeApplicationResult;
import edu.stanford.protege.webprotege.change.ChangeListGenerator;
import edu.stanford.protege.webprotege.change.HasApplyChanges;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectChangeHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25 Sep 2018
 */
public class MoveToParentActionHandler extends AbstractProjectChangeHandler<Boolean, MoveEntitiesToParentAction, MoveEntitiesToParentResult> {

    @Nonnull
    private final MoveClassesChangeListGeneratorFactory factory;

    @Inject
    public MoveToParentActionHandler(@Nonnull AccessManager accessManager,
                                     @Nonnull HasApplyChanges applyChanges,
                                     @Nonnull MoveClassesChangeListGeneratorFactory factory) {
        super(accessManager, applyChanges);
        this.factory = factory;
    }

    @Nonnull
    @Override
    public Class<MoveEntitiesToParentAction> getActionClass() {
        return MoveEntitiesToParentAction.class;
    }

    @Override
    protected ChangeListGenerator<Boolean> getChangeListGenerator(MoveEntitiesToParentAction action, ExecutionContext executionContext) {
        if(action.entity().isOWLClass()) {
            ImmutableSet<OWLClass> clses = action.entities().stream().map(OWLEntity::asOWLClass).collect(toImmutableSet());
            return factory.create(action.changeRequestId(), clses, action.entity().asOWLClass(), action.commitMessage());
        }
        return null;
    }

    @Override
    protected MoveEntitiesToParentResult createActionResult(ChangeApplicationResult<Boolean> changeApplicationResult,
                                                            MoveEntitiesToParentAction action,
                                                            ExecutionContext executionContext) {
        return new MoveEntitiesToParentResult();
    }
}
