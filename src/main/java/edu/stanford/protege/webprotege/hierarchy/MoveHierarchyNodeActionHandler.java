package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.access.*;
import edu.stanford.protege.webprotege.bulkop.MoveToParentActionHandler;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.entity.EntityNode;
import edu.stanford.protege.webprotege.icd.ReleasedClassesChecker;
import edu.stanford.protege.webprotege.icd.hierarchy.ClassHierarchyRetiredClassDetector;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.linearization.LinearizationManager;
import edu.stanford.protege.webprotege.project.chg.ChangeManager;
import org.jetbrains.annotations.NotNull;
import org.semanticweb.owlapi.model.OWLClass;
import org.slf4j.*;

import javax.annotation.*;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static edu.stanford.protege.webprotege.access.BuiltInAction.EDIT_ONTOLOGY;
import static org.glassfish.jersey.internal.guava.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 8 Dec 2017
 */
public class MoveHierarchyNodeActionHandler extends AbstractProjectActionHandler<MoveHierarchyNodeAction, MoveHierarchyNodeResult> {

    private final Logger logger = LoggerFactory.getLogger(MoveToParentActionHandler.class);

    private final MoveEntityChangeListGeneratorFactory factory;

    @Nonnull
    private final ReleasedClassesChecker releasedClassesChecker;

    @Nonnull
    private final ClassHierarchyRetiredClassDetector retiredAncestorDetector;

    @Nonnull
    private final ChangeManager changeManager;

    @Nonnull
    private final LinearizationManager linearizationManager;

    @Inject
    public MoveHierarchyNodeActionHandler(@Nonnull AccessManager accessManager,
                                          @Nonnull MoveEntityChangeListGeneratorFactory factory,
                                          @Nonnull ReleasedClassesChecker releasedClassesChecker,
                                          @Nonnull ClassHierarchyRetiredClassDetector retiredAncestorDetector,
                                          @Nonnull ChangeManager changeManager, @Nonnull LinearizationManager linearizationManager) {
        super(accessManager);
        this.factory = checkNotNull(factory);
        this.releasedClassesChecker = checkNotNull(releasedClassesChecker);
        this.retiredAncestorDetector = checkNotNull(retiredAncestorDetector);
        this.changeManager = checkNotNull(changeManager);
        this.linearizationManager = linearizationManager;
    }

    @Nonnull
    @Override
    public Class<MoveHierarchyNodeAction> getActionClass() {
        return MoveHierarchyNodeAction.class;
    }

    @NotNull
    @Override
    public MoveHierarchyNodeResult execute(@NotNull MoveHierarchyNodeAction action, @NotNull ExecutionContext executionContext) {
        var sourceNode = action.fromNodePath().getLast().orElseGet(null);
        var destinationNode = action.toNodeParentPath().getLast().orElseGet(null);
        if (sourceNode != null &&
                sourceNode.getEntity().isOWLClass() &&
                destinationNode != null) {
            var isDestinationRetiredClass = false;

            var classToBeMoved = sourceNode.getEntity();

            var isSourceClassReleased = releasedClassesChecker.isReleased(classToBeMoved);


            if (isSourceClassReleased) {
                isDestinationRetiredClass = retiredAncestorDetector.isRetired(destinationNode.getEntity().asOWLClass());
                if (isDestinationRetiredClass) {
                    return new MoveHierarchyNodeResult(false, isDestinationRetiredClass);
                }
            }
        }

        var changeListGenerator = factory.create(action);
        var changeResult = changeManager.applyChanges(executionContext.userId(), changeListGenerator);

        if (sourceNode != null &&
                sourceNode.getEntity().isOWLClass() &&
                destinationNode != null) {
            try {
                linearizationManager.mergeLinearizationsFromParents(sourceNode.getEntity().getIRI(), Set.of(destinationNode.getEntity().getIRI()), action.projectId(), executionContext).get();
            } catch (InterruptedException | ExecutionException e) {
                logger.error("MergeLinearizationsError: " + e);
            }
        }

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
