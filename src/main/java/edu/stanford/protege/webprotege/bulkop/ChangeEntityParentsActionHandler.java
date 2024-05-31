package edu.stanford.protege.webprotege.bulkop;

import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.hierarchy.HierarchyCycleException;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.project.chg.ChangeManager;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25 Sep 2018
 */
public class ChangeEntityParentsActionHandler extends AbstractProjectActionHandler< ChangeEntityParentsAction, ChangeEntityParentsResult> {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final ChangeManager changeManager;

    @Nonnull
    private final EditParentsChangeListGeneratorFactory factory;

    @Inject
    public ChangeEntityParentsActionHandler(@Nonnull AccessManager accessManager,
                                  @Nonnull ProjectId projectId,
                                  @Nonnull ChangeManager changeManager,
                                  @Nonnull EditParentsChangeListGeneratorFactory factory) {
        super(accessManager);
        this.projectId = projectId;
        this.changeManager = changeManager;
        this.factory = factory;
    }

    @Nonnull
    @Override
    public Class<ChangeEntityParentsAction> getActionClass() {
        return ChangeEntityParentsAction.class;
    }


    @Nonnull
    @Override
    public ChangeEntityParentsResult execute(@Nonnull ChangeEntityParentsAction action, @Nonnull ExecutionContext executionContext) {
        ImmutableSet<OWLClass> parents = action.parents().stream().map(OWLEntity::asOWLClass).collect(toImmutableSet());
        var changeListGenerator = factory.create(action.changeRequestId(), parents, action.entity().asOWLClass(), action.commitMessage());
        Set<OWLClass> classesWithCycles = new HashSet<>();
        try{
//            changeManager.applyChanges(executionContext.userId(), changeListGenerator);
        }catch (HierarchyCycleException e){
            //populate classesWithCycles
        }
        changeManager.applyChanges(executionContext.userId(), changeListGenerator);

        //put cycle classes in response.
        return new ChangeEntityParentsResult();
    }
}
