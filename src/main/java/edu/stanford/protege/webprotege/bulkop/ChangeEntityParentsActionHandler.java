package edu.stanford.protege.webprotege.bulkop;

import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.DataFactory;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.change.*;
import edu.stanford.protege.webprotege.common.*;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.entity.OWLEntityData;
import edu.stanford.protege.webprotege.hierarchy.*;
import edu.stanford.protege.webprotege.hierarchy.ordering.ProjectOrderedChildrenManager;
import edu.stanford.protege.webprotege.icd.*;
import edu.stanford.protege.webprotege.icd.hierarchy.ClassHierarchyRetiredClassDetector;
import edu.stanford.protege.webprotege.ipc.EventDispatcher;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.linearization.LinearizationManager;
import edu.stanford.protege.webprotege.project.PackagedProjectChangeEvent;
import edu.stanford.protege.webprotege.project.chg.ChangeManager;
import edu.stanford.protege.webprotege.renderer.RenderingManager;
import edu.stanford.protege.webprotege.revision.*;
import org.semanticweb.owlapi.model.*;
import org.slf4j.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25 Sep 2018
 */
public class ChangeEntityParentsActionHandler extends AbstractProjectActionHandler<ChangeEntityParentsAction, ChangeEntityParentsResult> {


    private final Logger logger = LoggerFactory.getLogger(ChangeEntityParentsActionHandler.class);


    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final ChangeManager changeManager;

    @Nonnull
    private final EditParentsChangeListGeneratorFactory factory;

    @Nonnull
    private final RevisionManager revisionManager;

    @Nonnull
    private final ClassHierarchyProvider classHierarchyProvider;

    @Nonnull
    private final ClassHierarchyCycleDetector classCycleDetector;

    @Nonnull
    private final RevisionReverterChangeListGeneratorFactory reveisionReverterFactory;

    @Nonnull
    private final RenderingManager renderingManager;

    @Nonnull
    private final ReleasedClassesChecker releasedClassesChecker;

    @Nonnull
    private final ClassHierarchyRetiredClassDetector retiredAncestorDetector;

    @Nonnull
    private final LinearizationManager linearizationManager;

    @Nonnull
    private final LinearizationParentChecker linParentChecker;

    @Nonnull
    private final ProjectOrderedChildrenManager projectOrderedChildrenManager;


    @Nonnull
    private final EventDispatcher eventDispatcher;

    @Inject
    public ChangeEntityParentsActionHandler(@Nonnull AccessManager accessManager,
                                            @Nonnull ProjectId projectId,
                                            @Nonnull ChangeManager changeManager,
                                            @Nonnull EditParentsChangeListGeneratorFactory factory,
                                            @Nonnull ClassHierarchyCycleDetector classCycleDetector,
                                            @Nonnull RevisionReverterChangeListGeneratorFactory revisionRevertFactory,
                                            @Nonnull RevisionManager revisionManager,
                                            @Nonnull ClassHierarchyProvider classHierarchyProvider,
                                            @Nonnull RenderingManager renderingManager,
                                            @Nonnull ReleasedClassesChecker releasedClassesChecker,
                                            @Nonnull ClassHierarchyRetiredClassDetector retiredAncestorDetector,
                                            @Nonnull LinearizationManager linearizationManager,
                                            @Nonnull LinearizationParentChecker linParentChecker,
                                            @Nonnull ProjectOrderedChildrenManager projectOrderedChildrenManager, @Nonnull EventDispatcher eventDispatcher) {
        super(accessManager);
        this.projectId = checkNotNull(projectId);
        this.changeManager = checkNotNull(changeManager);
        this.factory = checkNotNull(factory);
        this.classCycleDetector = checkNotNull(classCycleDetector);
        this.revisionManager = checkNotNull(revisionManager);
        this.reveisionReverterFactory = checkNotNull(revisionRevertFactory);
        this.classHierarchyProvider = checkNotNull(classHierarchyProvider);
        this.renderingManager = checkNotNull(renderingManager);
        this.retiredAncestorDetector = retiredAncestorDetector;
        this.releasedClassesChecker = checkNotNull(releasedClassesChecker);
        this.linearizationManager = checkNotNull(linearizationManager);
        this.linParentChecker = linParentChecker;
        this.projectOrderedChildrenManager = projectOrderedChildrenManager;
        this.eventDispatcher = eventDispatcher;
    }

    @Nonnull
    @Override
    public Class<ChangeEntityParentsAction> getActionClass() {
        return ChangeEntityParentsAction.class;
    }


    @Nonnull
    @Override
    public ChangeEntityParentsResult execute(@Nonnull ChangeEntityParentsAction action, @Nonnull ExecutionContext executionContext) {
        Set<IRI> newParents = action.parents()
                .stream()
                .map(OWLNamedObject::getIRI)
                .collect(Collectors.toSet());
        Set<IRI> currentParents = classHierarchyProvider.getParents(action.entity())
                .stream()
                .map(OWLNamedObject::getIRI)
                .collect(Collectors.toSet());
        Set<IRI> removedParents = currentParents
                .stream()
                .filter(parent -> !newParents.contains(parent))
                .collect(Collectors.toSet());
        var parentThatisLinearizationParent = linParentChecker.getParentThatIsLinearizationPathParent(action.entity().getIRI(),
                removedParents,
                projectId,executionContext);

        if (!parentThatisLinearizationParent.isEmpty()) {
            return getResultWithParentAsLinearizationPathParent(parentThatisLinearizationParent);
        }

        var parents = action.parents().stream().collect(toImmutableSet());

        if (releasedClassesChecker.isReleased(action.entity())) {
            var classesWithRetiredAncestors = this.retiredAncestorDetector.getClassesWithRetiredAncestors(parents);

            if (isNotEmpty(classesWithRetiredAncestors)) {
                return getResultWithRetiredAncestors(classesWithRetiredAncestors);
            }
        }

        var changeListGenerator = factory.create(action.changeRequestId(), parents, action.entity().asOWLClass(), action.commitMessage());

        var result = changeManager.applyChanges(executionContext.userId(), changeListGenerator);

        var classesWithCycles = classCycleDetector.getClassesWithCycle(result.getChangeList());

        if (result.getSubject() &&
                classesWithCycles.isEmpty()) {
            var parentIris = action.parents()
                    .stream()
                    .map(OWLNamedObject::getIRI)
                    .collect(Collectors.toSet());
            try {
                linearizationManager.mergeLinearizationsFromParents(action.entity().getIRI(), parentIris, projectId, executionContext).get(5, TimeUnit.SECONDS);
            } catch (TimeoutException | InterruptedException | ExecutionException e) {
                logger.error("MergeLinearizationsError: " + e);
            }
            projectOrderedChildrenManager.changeEntityParents(action.entity().getIRI(),removedParents,newParents);
            EventId eventId = EventId.generate();
            eventDispatcher.dispatchEvent(new PackagedProjectChangeEvent(projectId, eventId, Collections.singletonList(new ParentsChangedEvent(projectId, eventId, action.entity().getIRI()))));

            return validEmptyResult();
        }

        ChangeListGenerator<Boolean> revisionReverterGenerator = getRevisionReverterChangeListGenerator(revisionManager.getCurrentRevision(), ChangeRequestId.generate());
        changeManager.applyChanges(executionContext.userId(), revisionReverterGenerator);
        EventId eventId = EventId.generate();
        eventDispatcher.dispatchEvent(new PackagedProjectChangeEvent(projectId, eventId, Collections.singletonList(new ParentsChangedEvent(projectId, eventId, action.entity().getIRI()))));
        return getResultWithCycles(classesWithCycles);
    }

    private boolean isNotEmpty(Set<?> set) {
        return !set.isEmpty();
    }


    private ChangeListGenerator<Boolean> getRevisionReverterChangeListGenerator(RevisionNumber revisionNumber, ChangeRequestId changeRequestId) {
        return reveisionReverterFactory.create(revisionNumber, changeRequestId);
    }

    private ChangeEntityParentsResult validEmptyResult() {
        return new ChangeEntityParentsResult(ImmutableSet.of(), ImmutableSet.of(), ImmutableSet.of());
    }

    private ChangeEntityParentsResult getResultWithCycles(Set<OWLClass> classes) {
        var owlEntityDataResult = getOwlEntityDataFromOwlClasses(classes);
        return new ChangeEntityParentsResult(owlEntityDataResult, ImmutableSet.of(), ImmutableSet.of());
    }

    private Set<OWLEntityData> getOwlEntityDataFromOwlClasses(Set<OWLClass> classes) {
        return classes.stream()
                .map(renderingManager::getRendering)
                .collect(Collectors.toSet());
    }

    private ChangeEntityParentsResult getResultWithRetiredAncestors(Set<OWLClass> classes) {
        var owlEntityDataResult = getOwlEntityDataFromOwlClasses(classes);
        return new ChangeEntityParentsResult(ImmutableSet.of(), owlEntityDataResult, ImmutableSet.of());
    }

    private ChangeEntityParentsResult getResultWithParentAsLinearizationPathParent(Set<IRI> parentIris) {
        Set<OWLClass> parentClasses = parentIris.stream().map(DataFactory::getOWLClass).collect(Collectors.toSet());
        var parentEntityDataResult = getOwlEntityDataFromOwlClasses(parentClasses);

        return new ChangeEntityParentsResult(ImmutableSet.of(), ImmutableSet.of(), parentEntityDataResult);
    }
}
