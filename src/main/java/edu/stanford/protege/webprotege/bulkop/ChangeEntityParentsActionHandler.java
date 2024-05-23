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
public class ChangeEntityParentsActionHandler extends AbstractProjectChangeHandler<Boolean, ChangeEntityParentsAction, ChangeEntityParentsResult> {

    @Nonnull
    private final EditParentsChangeListGeneratorFactory factory;

    @Inject
    public ChangeEntityParentsActionHandler(@Nonnull AccessManager accessManager,
                                            @Nonnull HasApplyChanges applyChanges,
                                            @Nonnull EditParentsChangeListGeneratorFactory factory) {
        super(accessManager, applyChanges);
        this.factory = factory;
    }

    @Nonnull
    @Override
    public Class<ChangeEntityParentsAction> getActionClass() {
        return ChangeEntityParentsAction.class;
    }

    @Override
    protected ChangeListGenerator<Boolean> getChangeListGenerator(ChangeEntityParentsAction action, ExecutionContext executionContext) {
        if(action.entity().isOWLClass()) {
            ImmutableSet<OWLClass> parents = action.parents().stream().map(OWLEntity::asOWLClass).collect(toImmutableSet());
            return factory.create(action.changeRequestId(), parents, action.entity().asOWLClass(), action.commitMessage());
        }
        return null;
    }

    @Override
    protected ChangeEntityParentsResult createActionResult(ChangeApplicationResult<Boolean> changeApplicationResult,
                                                           ChangeEntityParentsAction action,
                                                            ExecutionContext executionContext) {
        return new ChangeEntityParentsResult();
    }
}
