package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.access.*;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.entity.EntityNode;
import edu.stanford.protege.webprotege.icd.ReleasedClassesChecker;
import edu.stanford.protege.webprotege.icd.hierarchy.ClassHierarchyRetiredClassDetector;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.project.chg.ChangeManager;
import org.jetbrains.annotations.NotNull;
import org.semanticweb.owlapi.model.OWLClass;

import javax.annotation.*;
import javax.inject.Inject;
import java.util.*;

import static edu.stanford.protege.webprotege.access.BuiltInAction.EDIT_ONTOLOGY;
import static org.glassfish.jersey.internal.guava.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 8 Dec 2017
 */
public class MoveHierarchyNodeActionHandler extends AbstractProjectActionHandler<MoveHierarchyNodeAction, MoveHierarchyNodeResult> {

    private final MoveEntityChangeListGeneratorFactory factory;

    @Nonnull
    private final ReleasedClassesChecker releasedClassesChecker;

    @Nonnull
    private final ClassHierarchyRetiredClassDetector retiredAncestorDetector;

    @Nonnull
    private final ChangeManager changeManager;

    @Inject
    public MoveHierarchyNodeActionHandler(@Nonnull AccessManager accessManager,
                                          @Nonnull MoveEntityChangeListGeneratorFactory factory,
                                          @Nonnull ReleasedClassesChecker releasedClassesChecker,
                                          @Nonnull ClassHierarchyRetiredClassDetector retiredAncestorDetector,
                                          @Nonnull ChangeManager changeManager) {
        super(accessManager);
        this.factory = checkNotNull(factory);
        this.releasedClassesChecker = checkNotNull(releasedClassesChecker);
        this.retiredAncestorDetector = checkNotNull(retiredAncestorDetector);
        this.changeManager = checkNotNull(changeManager);
    }

    @Nonnull
    @Override
    public Class<MoveHierarchyNodeAction> getActionClass() {
        return MoveHierarchyNodeAction.class;
    }

    @NotNull
    @Override
    public MoveHierarchyNodeResult execute(@NotNull MoveHierarchyNodeAction action, @NotNull ExecutionContext executionContext) {
        var areEntityNodesOfClassType = action.fromNodePath().getFirst().get().getEntity().isOWLClass();
        if (areEntityNodesOfClassType) {
            var isDestinationRetiredClass = false;

            var classesToBeMoved = getClassesFromNodePath(action.fromNodePath());
            var destinationClasses = getClassesFromNodePath(action.toNodeParentPath());

            var isAnySourceClassReleased = classesToBeMoved.stream().anyMatch(releasedClassesChecker::isReleased);


            if (isAnySourceClassReleased) {
                isDestinationRetiredClass = destinationClasses.stream().anyMatch(retiredAncestorDetector::isRetired);
                if (isDestinationRetiredClass) {
                    return new MoveHierarchyNodeResult(false, isDestinationRetiredClass);
                }
            }
        }

        var changeListGenerator = factory.create(action);
        var changeResult = changeManager.applyChanges(executionContext.userId(), changeListGenerator);

        return new MoveHierarchyNodeResult(changeResult.getSubject(), false);
    }

    private List<OWLClass> getClassesFromNodePath(Path<EntityNode> fromNodePath) {
        var owlClasses = new ArrayList<OWLClass>();
        if (fromNodePath.isEmpty()) {
            return Collections.emptyList();
        }
        fromNodePath.asList()
                .forEach(entityNode -> owlClasses.add(entityNode.getEntity().asOWLClass()));

        return owlClasses;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(MoveHierarchyNodeAction action) {
        return EDIT_ONTOLOGY;
    }
}
