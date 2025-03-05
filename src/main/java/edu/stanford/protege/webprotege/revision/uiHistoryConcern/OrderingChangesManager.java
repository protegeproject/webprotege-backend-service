package edu.stanford.protege.webprotege.revision.uiHistoryConcern;

import edu.stanford.protege.webprotege.change.ProjectChange;
import edu.stanford.protege.webprotege.common.Page;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.diff.DiffElement;
import edu.stanford.protege.webprotege.diff.OrderChange;
import edu.stanford.protege.webprotege.diff.OrderingDiffElementRenderer;
import edu.stanford.protege.webprotege.diff.ProjectOrderedChildren2DiffElementsTranslator;
import edu.stanford.protege.webprotege.hierarchy.ordering.ProjectOrderedChildren;
import edu.stanford.protege.webprotege.inject.ProjectSingleton;
import edu.stanford.protege.webprotege.revision.RevisionNumber;
import jakarta.inject.Inject;
import org.semanticweb.owlapi.model.IRI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

@ProjectSingleton
public class OrderingChangesManager {
    private static final Logger logger = LoggerFactory.getLogger(OrderingChangesManager.class);

    private final ProjectOrderedChildren2DiffElementsTranslator translator;
    private final OrderingDiffElementRenderer diffElementRenderer;

    @Inject
    public OrderingChangesManager(ProjectOrderedChildren2DiffElementsTranslator translator,
                                  OrderingDiffElementRenderer diffElementRenderer) {
        this.translator = translator;
        this.diffElementRenderer = diffElementRenderer;
    }

    public Set<ProjectChangeForEntity> getProjectChangesForEntitiesFromOrderingChange(
            IRI entityParentIri,
            Optional<ProjectOrderedChildren> initialOrderedChildrenOptional,
            ProjectOrderedChildren newOrdering,
            UserId userId
    ) {
        List<DiffElement<String, OrderChange>> diffElements =
                translator.getDiffElementsFromOrdering(initialOrderedChildrenOptional, newOrdering);

        if (diffElements.isEmpty()) {
            return Collections.emptySet();
        }

        List<DiffElement<String, String>> renderedDiffs = diffElements.stream()
                .map(diffElement -> diffElementRenderer.render(diffElement, entityParentIri))
                .toList();


        return renderedDiffs.stream()
                .map(diff -> {
                    Page<DiffElement<String, String>> page = Page.create(
                            1, 1, List.of(diff), 1
                    );
                    return ProjectChangeForEntity.create(
                            diff.getSourceDocument(),
                            ChangeType.UPDATE_ENTITY,
                            ProjectChange.get(
                                    RevisionNumber.getRevisionNumber(0),
                                    userId,
                                    System.currentTimeMillis(),
                                    diff.getLineElement(),
                                    1,
                                    page
                            ));
                })
                .collect(Collectors.toCollection(TreeSet::new));
    }
}
