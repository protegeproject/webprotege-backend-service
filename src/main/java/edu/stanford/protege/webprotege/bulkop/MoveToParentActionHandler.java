package edu.stanford.protege.webprotege.bulkop;

import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.change.*;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.hierarchy.ClassHierarchyProvider;
import edu.stanford.protege.webprotege.hierarchy.ordering.ProjectOrderedChildrenManager;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.project.chg.ChangeManager;
import org.jetbrains.annotations.NotNull;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

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
    private final ProjectOrderedChildrenManager projectOrderedChildrenManager;

    @Nonnull
    private final ChangeManager changeManager;

    @Nonnull
    private final ClassHierarchyProvider classHierarchyProvider;

    @Inject
    public MoveToParentActionHandler(@Nonnull AccessManager accessManager,
                                     @Nonnull MoveClassesChangeListGeneratorFactory factory,
                                     @Nonnull ProjectOrderedChildrenManager projectOrderedChildrenManager,
                                     @Nonnull ChangeManager changeManager,
                                     @Nonnull ClassHierarchyProvider classHierarchyProvider) {
        super(accessManager);
        this.factory = factory;
        this.projectOrderedChildrenManager = projectOrderedChildrenManager;
        this.changeManager = changeManager;
        this.classHierarchyProvider = classHierarchyProvider;
    }

    @Nonnull
    @Override
    public Class<MoveEntitiesToParentAction> getActionClass() {
        return MoveEntitiesToParentAction.class;
    }

    protected ChangeListGenerator<Boolean> getChangeListGenerator(MoveEntitiesToParentAction action) {
        if (action.entity().isOWLClass()) {
            ImmutableSet<OWLClass> clses = action.entities().stream().map(OWLEntity::asOWLClass).collect(toImmutableSet());
            return factory.create(action.changeRequestId(), clses, action.entity().asOWLClass(), action.commitMessage());
        }
        return null;
    }

    @NotNull
    @Override
    public MoveEntitiesToParentResult execute(@NotNull MoveEntitiesToParentAction action, @NotNull ExecutionContext executionContext) {

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

        var changeListGenerator = getChangeListGenerator(action);
        ChangeApplicationResult<Boolean> result = changeManager.applyChanges(executionContext.userId(), changeListGenerator);

        if (result.getSubject()) {
            projectOrderedChildrenManager.moveEntitiesToParent(action.entity(), action.entities(), entitiesWithPreviousParents);
        }
        return new MoveEntitiesToParentResult();
    }
}
