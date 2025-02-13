package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.icd.LinearizationParentChecker;
import edu.stanford.protege.webprotege.icd.ReleasedClassesChecker;
import edu.stanford.protege.webprotege.icd.hierarchy.ClassHierarchyRetiredClassDetector;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.linearization.LinearizationManager;
import edu.stanford.protege.webprotege.project.chg.ChangeManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.protege.webprotege.access.BuiltInAction.EDIT_ONTOLOGY;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 8 Dec 2017
 */
public class MoveHierarchyNodeIcdActionHandler extends AbstractProjectActionHandler<MoveHierarchyNodeIcdAction, MoveHierarchyNodeIcdResult> {

    private final Logger logger = LoggerFactory.getLogger(MoveHierarchyNodeIcdActionHandler.class);

    private final MoveEntityChangeListGeneratorFactory factory;

    @Nonnull
    private final ReleasedClassesChecker releasedClassesChecker;

    @Nonnull
    private final ClassHierarchyRetiredClassDetector retiredAncestorDetector;

    @Nonnull
    private final ChangeManager changeManager;

    @Nonnull
    private final LinearizationManager linearizationManager;

    @Nonnull
    private final LinearizationParentChecker linParentChecker;


    @Inject
    public MoveHierarchyNodeIcdActionHandler(@Nonnull AccessManager accessManager,
                                             @Nonnull MoveEntityChangeListGeneratorFactory factory,
                                             @Nonnull ReleasedClassesChecker releasedClassesChecker,
                                             @Nonnull ClassHierarchyRetiredClassDetector retiredAncestorDetector,
                                             @Nonnull ChangeManager changeManager,
                                             @Nonnull LinearizationManager linearizationManager,
                                             @Nonnull LinearizationParentChecker linParentChecker) {
        super(accessManager);
        this.factory = checkNotNull(factory);
        this.releasedClassesChecker = checkNotNull(releasedClassesChecker);
        this.retiredAncestorDetector = checkNotNull(retiredAncestorDetector);
        this.changeManager = checkNotNull(changeManager);
        this.linearizationManager = linearizationManager;
        this.linParentChecker = linParentChecker;
    }

    @Nonnull
    @Override
    public Class<MoveHierarchyNodeIcdAction> getActionClass() {
        return MoveHierarchyNodeIcdAction.class;
    }

    @NotNull
    @Override
    public MoveHierarchyNodeIcdResult execute(@NotNull MoveHierarchyNodeIcdAction action, @NotNull ExecutionContext executionContext) {
        var sourceNode = action.fromNodePath().getLast();
        var destinationNode = action.toNodeParentPath().getLast();
        if (sourceNode.isPresent() &&
                sourceNode.get().getEntity().isOWLClass() &&
                destinationNode.isPresent()) {
            var isDestinationRetiredClass = false;
            boolean isLinPathParent = false;

            var classToBeMoved = sourceNode.get().getEntity();

            var isSourceClassReleased = releasedClassesChecker.isReleased(classToBeMoved);


            if (isSourceClassReleased) {
                isDestinationRetiredClass = retiredAncestorDetector.isRetired(destinationNode.get().getEntity().asOWLClass());
                return new MoveHierarchyNodeIcdResult(false, isDestinationRetiredClass, isLinPathParent);
            }

            var previousParent = action.fromNodePath().getLastPredecessor();

            if (previousParent.isPresent()) {
                isLinPathParent = linParentChecker.getParentThatIsLinearizationPathParent(classToBeMoved.getIRI(),
                                Set.of(previousParent.get().getEntity().getIRI()),
                                action.projectId())
                        .stream().findAny().isPresent();
                if (isLinPathParent) {
                    return new MoveHierarchyNodeIcdResult(false, isDestinationRetiredClass, isLinPathParent);
                }
            }

        }


        var changeListGenerator = factory.create(
                action.fromNodePath(),
                action.toNodeParentPath(),
                action.dropType(),
                action.changeRequestId()
        );
        var changeResult = changeManager.applyChanges(executionContext.userId(), changeListGenerator);

        if (changeResult.getSubject() &&
                sourceNode.isPresent() &&
                sourceNode.get().getEntity().isOWLClass() &&
                destinationNode.isPresent()) {
            try {
                linearizationManager.mergeLinearizationsFromParents(
                        sourceNode.get().getEntity().getIRI(),
                        Set.of(destinationNode.get().getEntity().getIRI()),
                        action.projectId(),
                        executionContext
                ).get();
            } catch (InterruptedException | ExecutionException e) {
                logger.error("Error merging linearizations after moving entity: {}to new parent: {}",
                        sourceNode.get().getBrowserText(),
                        destinationNode.get().getBrowserText(),
                        e);
            }
        }

        return new MoveHierarchyNodeIcdResult(changeResult.getSubject(), false, false);
    }


    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(MoveHierarchyNodeIcdAction action) {
        return EDIT_ONTOLOGY;
    }
}
