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

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@ProjectSingleton
public class OrderingChangesManager {
    private static final Logger logger = LoggerFactory.getLogger(OrderingChangesManager.class);

    private final ProjectOrderedChildren2DiffElementsTranslator translator;
    private final OrderingDiffElementRenderer diffElementRenderer;

    public static final int DEFAULT_CHANGE_LIMIT = 50;

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

        var totalChanges = diffElements.size();

        List<DiffElement<String, String>> renderedDiffs = diffElements.stream()
                .limit(DEFAULT_CHANGE_LIMIT)
                .map(diffElement -> diffElementRenderer.render(diffElement, entityParentIri))
                .toList();

        int pageElements = renderedDiffs.size();
        int pageCount;
        if (pageElements == 0) {
            pageCount = 1;
        } else {
            pageCount = totalChanges / pageElements + (totalChanges % pageElements);
        }
        Page<DiffElement<String, String>> page = Page.create(
                1,
                pageCount,
                renderedDiffs,
                totalChanges
        );

        return Set.of(
                ProjectChangeForEntity.create(
                        entityParentIri.toString(),
                        ChangeType.UPDATE_ENTITY,
                        ProjectChange.get(
                                RevisionNumber.getRevisionNumber(0),
                                userId,
                                System.currentTimeMillis(),
                                getChangeSummary(entityParentIri, totalChanges),
                                totalChanges,
                                page
                        ))
        );
    }

    private String getChangeSummary(IRI entityParentIri, int totalChanges) {
        StringBuilder sb = new StringBuilder();
        sb.append("Reordered children of ")
                .append(entityParentIri)
                .append(" (")
                .append(totalChanges)
                .append(totalChanges > 1 ? " classes" : " class")
                .append(" were moved)");
        return sb.toString();
    }
}
