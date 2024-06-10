package edu.stanford.protege.webprotege.bulkop;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.change.*;
import edu.stanford.protege.webprotege.common.*;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.entity.OWLEntityData;
import edu.stanford.protege.webprotege.hierarchy.*;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.project.chg.ChangeManager;
import edu.stanford.protege.webprotege.renderer.RenderingManager;
import edu.stanford.protege.webprotege.revision.*;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25 Sep 2018
 */
public class ChangeEntityParentsActionHandler extends AbstractProjectActionHandler<ChangeEntityParentsAction, ChangeEntityParentsResult> {

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

    @Inject
    public ChangeEntityParentsActionHandler(@Nonnull AccessManager accessManager,
                                            @Nonnull ProjectId projectId,
                                            @Nonnull ChangeManager changeManager,
                                            @Nonnull EditParentsChangeListGeneratorFactory factory,
                                            @Nonnull ClassHierarchyCycleDetector classCycleDetector,
                                            @Nonnull RevisionReverterChangeListGeneratorFactory revisionRevertFactory,
                                            @Nonnull RevisionManager revisionManager,
                                            @Nonnull ClassHierarchyProvider classHierarchyProvider,
                                            @Nonnull RenderingManager renderingManager) {
        super(accessManager);
        this.projectId = checkNotNull(projectId);
        this.changeManager = checkNotNull(changeManager);
        this.factory = checkNotNull(factory);
        this.classCycleDetector = checkNotNull(classCycleDetector);
        this.revisionManager = checkNotNull(revisionManager);
        this.reveisionReverterFactory = checkNotNull(revisionRevertFactory);
        this.classHierarchyProvider = checkNotNull(classHierarchyProvider);
        this.renderingManager = checkNotNull(renderingManager);
    }

    @Nonnull
    @Override
    public Class<ChangeEntityParentsAction> getActionClass() {
        return ChangeEntityParentsAction.class;
    }


    @Nonnull
    @Override
    public ChangeEntityParentsResult execute(@Nonnull ChangeEntityParentsAction action, @Nonnull ExecutionContext executionContext) {
        var parents = action.parents().stream().map(OWLEntity::asOWLClass).collect(toImmutableSet());
        var changeListGenerator = factory.create(action.changeRequestId(), parents, action.entity().asOWLClass(), action.commitMessage());

        var result = changeManager.applyChanges(executionContext.userId(), changeListGenerator);

        var classesWithCycles = classCycleDetector.getClassesWithCycle(result.getChangeList());

        if (classesWithCycles.isEmpty()) {
            return resultWithoutCycles();
        }


        ChangeListGenerator<Boolean> revisionReverterGenerator = getRevisionReverterChangeListGenerator(revisionManager.getCurrentRevision(), ChangeRequestId.generate());
        changeManager.applyChanges(executionContext.userId(), revisionReverterGenerator);

        var resultWithCycles = getOwlEntityDataFromOwlClasses(classesWithCycles);
        return new ChangeEntityParentsResult(resultWithCycles);
    }


    private ChangeListGenerator<Boolean> getRevisionReverterChangeListGenerator(RevisionNumber revisionNumber, ChangeRequestId changeRequestId) {
        return reveisionReverterFactory.create(revisionNumber, changeRequestId);
    }

    private static ChangeEntityParentsResult resultWithoutCycles() {
        return new ChangeEntityParentsResult(Collections.EMPTY_SET);
    }

    private Set<OWLEntityData> getOwlEntityDataFromOwlClasses(Set<OWLClass> classes) {
        return classes.stream()
                .map(renderingManager::getRendering)
                .collect(Collectors.toSet());
    }
}
