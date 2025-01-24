package edu.stanford.protege.webprotege.bulkop;

import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.icd.ReleasedClassesChecker;
import edu.stanford.protege.webprotege.icd.hierarchy.ClassHierarchyRetiredClassDetector;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.linearization.LinearizationManager;
import edu.stanford.protege.webprotege.project.chg.ChangeManager;
import org.jetbrains.annotations.NotNull;
import org.semanticweb.owlapi.model.*;
import org.slf4j.*;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;

import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25 Sep 2018
 */
public class MoveToParentActionHandler extends AbstractProjectActionHandler<MoveEntitiesToParentAction, MoveEntitiesToParentResult> {


    private final Logger logger = LoggerFactory.getLogger(MoveToParentActionHandler.class);

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

    @Inject
    public MoveToParentActionHandler(@Nonnull AccessManager accessManager,
                                     @Nonnull MoveClassesChangeListGeneratorFactory factory,
                                     @Nonnull ReleasedClassesChecker releasedClassesChecker,
                                     @Nonnull ClassHierarchyRetiredClassDetector retiredAncestorDetector,
                                     @Nonnull ChangeManager changeManager,
                                     @Nonnull LinearizationManager linearizationManager) {
        super(accessManager);
        this.factory = factory;
        this.releasedClassesChecker = releasedClassesChecker;
        this.retiredAncestorDetector = retiredAncestorDetector;
        this.changeManager = changeManager;
        this.linearizationManager = linearizationManager;
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
        var isDestinationRetiredClass = false;

        var isAnyClassReleased = action.entities().stream().anyMatch(releasedClassesChecker::isReleased);

        if (isAnyClassReleased) {
            isDestinationRetiredClass = this.retiredAncestorDetector.isRetired(action.entity().asOWLClass());
            if (isDestinationRetiredClass) {
                return new MoveEntitiesToParentResult(isDestinationRetiredClass);
            }
        }

        ImmutableSet<OWLClass> clses = action.entities().stream().map(OWLEntity::asOWLClass).collect(toImmutableSet());
        var changeListGenerator = factory.create(action.changeRequestId(), clses, action.entity().asOWLClass(), action.commitMessage());
        changeManager.applyChanges(executionContext.userId(), changeListGenerator);

        clses.stream()
                .flatMap(cls -> Stream.of(linearizationManager.mergeLinearizationsFromParents(cls.getIRI(), Set.of(action.entity().getIRI()), action.projectId(), executionContext)))
                .forEach(completableFuture -> {
                    try {
                        completableFuture.get();
                    } catch (InterruptedException | ExecutionException e) {
                        logger.error("MergeLinearizationsError: " + e);
                    }
                });

        return new MoveEntitiesToParentResult(isDestinationRetiredClass);
    }

    private boolean isEntityAnOwlClass(OWLEntity entity) {
        return entity.isOWLClass();
    }

    private boolean isNotOwlClass(OWLEntity entity) {
        return !isEntityAnOwlClass(entity);
    }
}
