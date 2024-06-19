package edu.stanford.protege.webprotege.bulkop;

import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.change.HasApplyChanges;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.icd.ReleasedClassesChecker;
import edu.stanford.protege.webprotege.icd.hierarchy.ClassHierarchyRetiredClassDetector;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.project.chg.ChangeManager;
import org.jetbrains.annotations.NotNull;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25 Sep 2018
 */
public class MoveToParentActionHandler extends AbstractProjectActionHandler<MoveEntitiesToParentAction, MoveEntitiesToParentResult> {

    @Nonnull
    private final MoveClassesChangeListGeneratorFactory factory;

    @Nonnull
    private final ReleasedClassesChecker releasedClassesChecker;

    @Nonnull
    private final ClassHierarchyRetiredClassDetector retiredAncestorDetector;

    @Nonnull
    private final ChangeManager changeManager;

    @Inject
    public MoveToParentActionHandler(@Nonnull AccessManager accessManager,
                                     @Nonnull HasApplyChanges applyChanges,
                                     @Nonnull MoveClassesChangeListGeneratorFactory factory, @Nonnull ReleasedClassesChecker releasedClassesChecker, @Nonnull ClassHierarchyRetiredClassDetector retiredAncestorDetector, @Nonnull ChangeManager changeManager) {
        super(accessManager);
        this.factory = factory;
        this.releasedClassesChecker = releasedClassesChecker;
        this.retiredAncestorDetector = retiredAncestorDetector;
        this.changeManager = changeManager;
    }

    @Nonnull
    @Override
    public Class<MoveEntitiesToParentAction> getActionClass() {
        return MoveEntitiesToParentAction.class;
    }

    @NotNull
    @Override
    public MoveEntitiesToParentResult execute(@NotNull MoveEntitiesToParentAction action, @NotNull ExecutionContext executionContext) {
        if (isNotOwlClass(action.entity())) {
            return new MoveEntitiesToParentResult(false);
        }
        ImmutableSet<OWLClass> clses = action.entities().stream().map(OWLEntity::asOWLClass).collect(toImmutableSet());
        var isDestinationRetiredClass = false;
        //method if class is released check that we don't add parent that may have parents with retired name
        if (releasedClassesChecker.isReleased(action.entity())) {
            //call retired classes check manager to check forEach if it has ancestor. get ancestors for which we have retired and put them in list
            isDestinationRetiredClass = this.retiredAncestorDetector.isRetired(action.entity().asOWLClass());
            if (isDestinationRetiredClass) {
                return new MoveEntitiesToParentResult(isDestinationRetiredClass);
            }
        }

        var changeListGenerator = factory.create(action.changeRequestId(), clses, action.entity().asOWLClass(), action.commitMessage());
        changeManager.applyChanges(executionContext.userId(), changeListGenerator);

        return new MoveEntitiesToParentResult(isDestinationRetiredClass);
    }

    private boolean isEntityAnOwlClass(OWLEntity entity) {
        return entity.isOWLClass();
    }

    private boolean isNotOwlClass(OWLEntity entity) {
        return !isEntityAnOwlClass(entity);
    }
}
