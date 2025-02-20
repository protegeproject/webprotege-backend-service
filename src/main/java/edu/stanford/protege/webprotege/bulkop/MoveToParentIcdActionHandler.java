package edu.stanford.protege.webprotege.bulkop;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.DataFactory;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.change.ChangeApplicationResult;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.entity.OWLEntityData;
import edu.stanford.protege.webprotege.hierarchy.ClassHierarchyProvider;
import edu.stanford.protege.webprotege.hierarchy.ordering.ProjectOrderedChildrenManager;
import edu.stanford.protege.webprotege.icd.LinearizationParentChecker;
import edu.stanford.protege.webprotege.icd.ReleasedClassesChecker;
import edu.stanford.protege.webprotege.icd.hierarchy.ClassHierarchyRetiredClassDetector;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.linearization.LinearizationManager;
import edu.stanford.protege.webprotege.project.chg.ChangeManager;
import edu.stanford.protege.webprotege.renderer.RenderingManager;
import org.jetbrains.annotations.NotNull;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLNamedObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.collect.ImmutableSet.toImmutableSet;


public class MoveToParentIcdActionHandler extends AbstractProjectActionHandler<MoveEntitiesToParentIcdAction, MoveEntitiesToParentIcdResult> {


    private final Logger logger = LoggerFactory.getLogger(MoveToParentIcdActionHandler.class);

    @Nonnull
    private final MoveClassesChangeListGeneratorFactory factory;

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
    private final RenderingManager renderingManager;

    @Nonnull
    private final ClassHierarchyProvider classHierarchyProvider;

    @Nonnull
    private final ProjectOrderedChildrenManager projectOrderedChildrenManager;


    @Inject
    public MoveToParentIcdActionHandler(@Nonnull AccessManager accessManager,
                                        @Nonnull MoveClassesChangeListGeneratorFactory factory,
                                        @Nonnull ReleasedClassesChecker releasedClassesChecker,
                                        @Nonnull ClassHierarchyRetiredClassDetector retiredAncestorDetector,
                                        @Nonnull ChangeManager changeManager,
                                        @Nonnull LinearizationManager linearizationManager,
                                        @Nonnull LinearizationParentChecker linParentChecker,
                                        @Nonnull RenderingManager renderingManager,
                                        @Nonnull ClassHierarchyProvider classHierarchyProvider,
                                        @Nonnull ProjectOrderedChildrenManager projectOrderedChildrenManager) {
        super(accessManager);
        this.factory = factory;
        this.releasedClassesChecker = releasedClassesChecker;
        this.retiredAncestorDetector = retiredAncestorDetector;
        this.changeManager = changeManager;
        this.linearizationManager = linearizationManager;
        this.linParentChecker = linParentChecker;
        this.renderingManager = renderingManager;
        this.classHierarchyProvider = classHierarchyProvider;
        this.projectOrderedChildrenManager = projectOrderedChildrenManager;
    }

    @Nonnull
    @Override
    public Class<MoveEntitiesToParentIcdAction> getActionClass() {
        return MoveEntitiesToParentIcdAction.class;
    }

    @NotNull
    @Override
    public MoveEntitiesToParentIcdResult execute(@NotNull MoveEntitiesToParentIcdAction action, @NotNull ExecutionContext executionContext) {
        var isDestinationRetiredClass = false;

        var isAnyClassReleased = action.entities().stream().anyMatch(releasedClassesChecker::isReleased);

        if (isAnyClassReleased) {
            isDestinationRetiredClass = this.retiredAncestorDetector.isRetired(action.parentEntity());
            if (isDestinationRetiredClass) {
                return new MoveEntitiesToParentIcdResult(isDestinationRetiredClass, ImmutableMap.of());
            }
        }

        Map<IRI, Set<IRI>> entitiesWithParentLinPathParent = new HashMap<>();

        action.entities()
                .forEach(entity -> {
                    Set<IRI> currentParents = classHierarchyProvider.getParents(entity)
                            .stream()
                            .map(OWLNamedObject::getIRI)
                            .filter(parent -> !parent.equals(action.parentEntity().getIRI()))
                            .collect(Collectors.toSet());
                    linParentChecker.getParentThatIsLinearizationPathParent(entity.getIRI(),
                                    currentParents,
                                    action.projectId())
                            .forEach(linPathParent -> {
                                Set<IRI> entitySet = entitiesWithParentLinPathParent.get(linPathParent);
                                if (entitySet == null) {
                                    entitiesWithParentLinPathParent.put(
                                            linPathParent,
                                            new HashSet<>(Set.of(entity.getIRI()))
                                    );
                                    return;
                                }
                                entitySet.add(entity.getIRI());
                            });

                });


        if (!entitiesWithParentLinPathParent.isEmpty()) {
            return getResultWithParentAsLinearizationPathParent(entitiesWithParentLinPathParent);
        }

        Map<String, List<String>> entitiesWithPreviousParents = action.entities()
                .stream()
                .collect(Collectors.toMap(
                                (OWLEntity::toStringID),
                                (entity -> classHierarchyProvider.getParents(entity)
                                        .stream()
                                        .map(OWLEntity::toStringID)
                                        .toList())
                        )
                );

        ImmutableSet<OWLClass> clses = action.entities().stream().collect(toImmutableSet());
        var changeListGenerator = factory.create(action.changeRequestId(), clses, action.parentEntity().asOWLClass(), action.commitMessage());
        ChangeApplicationResult<Boolean> result = changeManager.applyChanges(executionContext.userId(), changeListGenerator);

        if(result.getSubject()){
            clses.stream()
                    .flatMap(cls -> Stream.of(linearizationManager.mergeLinearizationsFromParents(cls.getIRI(), Set.of(action.parentEntity().getIRI()), action.projectId(), executionContext)))
                    .forEach(completableFuture -> {
                        try {
                            completableFuture.get();
                        } catch (InterruptedException | ExecutionException e) {
                            logger.error("MergeLinearizationsError: " + e);
                        }
                    });
            projectOrderedChildrenManager.moveEntitiesToParent(action.parentEntity(),action.entities(), entitiesWithPreviousParents);
        }

        return new MoveEntitiesToParentIcdResult(isDestinationRetiredClass, ImmutableMap.of());
    }

    private MoveEntitiesToParentIcdResult getResultWithParentAsLinearizationPathParent(Map<IRI, Set<IRI>> parentsThatAreLinPathParent) {
        ImmutableMap<String, ImmutableSet<OWLEntityData>> result = parentsThatAreLinPathParent
                .entrySet()
                .stream()
                .collect(
                        ImmutableMap.toImmutableMap(
                                entry -> renderingManager.getRendering(DataFactory.getOWLClass(entry.getKey())).getBrowserText(),
                                entry -> {
                                    Set<OWLClass> parentClasses = entry.getValue().stream().map(DataFactory::getOWLClass).collect(Collectors.toSet());
                                    var parentEntityDataResult = getOwlEntityDataFromOwlClasses(parentClasses);
                                    return ImmutableSet.copyOf(parentEntityDataResult);
                                }
                        )
                );

        return new MoveEntitiesToParentIcdResult(false, result);
    }

    private Set<OWLEntityData> getOwlEntityDataFromOwlClasses(Set<OWLClass> classes) {
        return classes.stream()
                .map(renderingManager::getRendering)
                .collect(Collectors.toSet());
    }
}
