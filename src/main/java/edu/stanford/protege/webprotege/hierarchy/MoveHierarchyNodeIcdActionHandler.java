package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInCapability;
import edu.stanford.protege.webprotege.bulkop.ChangeEntityParentsActionHandler;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.entity.OWLEntityData;
import edu.stanford.protege.webprotege.hierarchy.ordering.ProjectOrderedChildrenManager;
import edu.stanford.protege.webprotege.icd.LinearizationParentChecker;
import edu.stanford.protege.webprotege.icd.ReleasedClassesChecker;
import edu.stanford.protege.webprotege.icd.hierarchy.ClassHierarchyRetiredClassDetector;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.linearization.LinearizationManager;
import edu.stanford.protege.webprotege.project.chg.ChangeManager;
import edu.stanford.protege.webprotege.renderer.RenderingManager;
import org.jetbrains.annotations.NotNull;
import org.semanticweb.owlapi.model.OWLClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.protege.webprotege.access.BuiltInCapability.EDIT_ONTOLOGY;

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

    @Nonnull
    private final ProjectOrderedChildrenManager projectOrderedChildrenManager;

    @Nonnull
    private final RenderingManager renderingManager;


    @Inject
    public MoveHierarchyNodeIcdActionHandler(@Nonnull AccessManager accessManager,
                                             @Nonnull MoveEntityChangeListGeneratorFactory factory,
                                             @Nonnull ReleasedClassesChecker releasedClassesChecker,
                                             @Nonnull ClassHierarchyRetiredClassDetector retiredAncestorDetector,
                                             @Nonnull ChangeManager changeManager,
                                             @Nonnull LinearizationManager linearizationManager,
                                             @Nonnull LinearizationParentChecker linParentChecker,
                                             @Nonnull ProjectOrderedChildrenManager projectOrderedChildrenManager, @Nonnull RenderingManager renderingManager) {
        super(accessManager);
        this.factory = checkNotNull(factory);
        this.releasedClassesChecker = checkNotNull(releasedClassesChecker);
        this.retiredAncestorDetector = checkNotNull(retiredAncestorDetector);
        this.changeManager = checkNotNull(changeManager);
        this.linearizationManager = linearizationManager;
        this.linParentChecker = linParentChecker;
        this.projectOrderedChildrenManager = projectOrderedChildrenManager;
        this.renderingManager = renderingManager;
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
            isDestinationRetiredClass = retiredAncestorDetector.isRetired(destinationNode.get().getEntity().asOWLClass());


            if(isDestinationRetiredClass){
                Set<OWLClass> releasedChildren = this.releasedClassesChecker.getReleasedDescendants(classToBeMoved);
                String validationMessage = ChangeEntityParentsActionHandler.createReleasedChildrenValidationMessage(getOwlEntityDataFromOwlClasses(releasedChildren), 2,
                        getOwlEntityDataFromOwlClasses(new HashSet<>(Collections.singletonList(destinationNode.get().getEntity().asOWLClass()))));
                return new MoveHierarchyNodeIcdResult(false, isSourceClassReleased && isDestinationRetiredClass, isLinPathParent, validationMessage);
            }


            var previousParent = action.fromNodePath().getLastPredecessor();

            if (previousParent.isPresent()) {
                isLinPathParent = linParentChecker.getParentThatIsLinearizationPathParent(classToBeMoved.getIRI(),
                                Set.of(previousParent.get().getEntity().getIRI()),
                                action.projectId(), executionContext)
                        .stream().findAny().isPresent();
                if (isLinPathParent) {
                    return new MoveHierarchyNodeIcdResult(false, isDestinationRetiredClass, isLinPathParent, null);
                }
            }

        }


        var changeListGenerator = factory.create(
                action.fromNodePath(),
                action.toNodeParentPath(),
                action.dropType(),
                action.changeRequestId(),
                action.commitMessage()
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
                ).get(5, TimeUnit.SECONDS);
            } catch (TimeoutException | InterruptedException | ExecutionException e) {
                logger.error("Error merging linearizations after moving entity: {}to new parent: {}",
                        sourceNode.get().getBrowserText(),
                        destinationNode.get().getBrowserText(),
                        e);
            }
            projectOrderedChildrenManager.moveHierarchyNode(
                    action.fromNodePath().getLastPredecessor().get().getEntity(),
                    action.toNodeParentPath().getLast().get().getEntity(),
                    action.fromNodePath().getLast().get().getEntity()
            );
        }

        return new MoveHierarchyNodeIcdResult(changeResult.getSubject(), false, false, null);
    }

    private Set<OWLEntityData> getOwlEntityDataFromOwlClasses(Set<OWLClass> classes) {
        return classes.stream()
                .map(renderingManager::getRendering)
                .collect(Collectors.toSet());
    }


    @Nullable
    @Override
    protected BuiltInCapability getRequiredExecutableBuiltInAction(MoveHierarchyNodeIcdAction action) {
        return EDIT_ONTOLOGY;
    }
}
