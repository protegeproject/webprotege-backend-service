package edu.stanford.protege.webprotege.match;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.hierarchy.ClassHierarchyProvider;
import edu.stanford.protege.webprotege.index.IndividualsByTypeIndex;
import edu.stanford.protege.webprotege.index.ProjectSignatureIndex;
import edu.stanford.protege.webprotege.individuals.InstanceRetrievalMode;
import edu.stanford.protege.webprotege.criteria.*;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-10-07
 */
public class HierarchyPositionMatchingEngineImpl implements HierarchyPositionMatchingEngine {

    @Nonnull
    private final ClassHierarchyProvider classHierarchyProvider;

    @Nonnull
    private final IndividualsByTypeIndex individualsByTypeIndex;

    @Nonnull
    private final ProjectSignatureIndex projectSignatureIndex;

    @Inject
    public HierarchyPositionMatchingEngineImpl(@Nonnull ClassHierarchyProvider classHierarchyProvider,
                                               @Nonnull IndividualsByTypeIndex individualsByTypeIndex,
                                               @Nonnull ProjectSignatureIndex projectSignatureIndex) {
        this.classHierarchyProvider = checkNotNull(classHierarchyProvider);
        this.individualsByTypeIndex = checkNotNull(individualsByTypeIndex);
        this.projectSignatureIndex = checkNotNull(projectSignatureIndex);
    }

    @Nonnull
    @Override
    public Stream<OWLEntity> getMatchingEntities(@Nonnull HierarchyPositionCriteria criteria) {
        var criteriaProcessor = new CriteriaProcessor();
        return criteria.accept(criteriaProcessor).stream();
    }


    private class CriteriaProcessor implements HierarchyPositionCriteriaVisitor<Collection<OWLEntity>> {

        @Override
        public Collection<OWLEntity> visit(CompositeHierarchyPositionCriteria criteria) {
            List<HierarchyPositionCriteria> subCriteria = criteria.getCriteria();
            if (criteria.getMatchType().equals(MultiMatchType.ALL)) {
                return getMatchIntersection(subCriteria);

            }
            else {
                return getMatchUnion(subCriteria);

            }
        }

        @Override
        public Collection<OWLEntity> visit(IsLeafClassCriteria isALeafClassCriteria) {
            return projectSignatureIndex.getSignature()
                                        .filter(OWLEntity::isOWLClass)
                                        .map(OWLEntity::asOWLClass)
                                        .filter(cls -> !cls.isOWLNothing())
                                        .filter(classHierarchyProvider::isLeaf)
                                        .collect(Collectors.toSet());
        }

        private Collection<OWLEntity> getMatchUnion(List<HierarchyPositionCriteria> subCriteria) {
            // Union of collections
            return subCriteria.stream().flatMap(sc -> sc.accept(this).stream()).collect(toImmutableSet());
        }

        private Collection<OWLEntity> getMatchIntersection(List<HierarchyPositionCriteria> subCriteria) {
            // Intersection of collections
            var subMatches = subCriteria.stream().map(sc -> sc.accept(this))
                                        // Sort by size, with smallest first, so that we do the fewest
                                        // checks that we have to do
                                        .sorted(Comparator.comparing(Collection::size)).collect(toImmutableList());
            if (subMatches.isEmpty()) {
                return Collections.emptySet();
            }
            else {
                // Start off with the smallest result
                var result = new ArrayList<>(subMatches.get(0));
                for (int i = 1; i < subMatches.size(); i++) {
                    // Only retain the matches that are also in all other results
                    var nextMatch = subMatches.get(i);
                    result.retainAll(nextMatch);
                }
                return result;
            }
        }

        @Override
        public Collection<OWLEntity> visit(NotSubClassOfCriteria notSubClassOfCriteria) {
            var subClassOfCriteria = SubClassOfCriteria.get(notSubClassOfCriteria.getTarget(),
                                                            notSubClassOfCriteria.getFilterType());
            var subClassOfMatches = visit(subClassOfCriteria);
            return projectSignatureIndex.getSignature()
                                        .filter(c -> !subClassOfMatches.contains(c))
                                        .collect(toImmutableList());
        }

        @Override
        public Collection<OWLEntity> visit(SubClassOfCriteria subClassOfCriteria) {
            OWLClass cls = subClassOfCriteria.getTarget();
            if (subClassOfCriteria.getFilterType().equals(HierarchyFilterType.DIRECT)) {
                return ImmutableSet.copyOf(classHierarchyProvider.getChildren(cls));
            }
            else {
                return ImmutableSet.copyOf(classHierarchyProvider.getDescendants(cls));
            }
        }

        @Override
        public Collection<OWLEntity> visit(InstanceOfCriteria instanceOfCriteria) {
            OWLClass typeCls = instanceOfCriteria.getTarget();
            if (instanceOfCriteria.getFilterType().equals(HierarchyFilterType.DIRECT)) {
                return individualsByTypeIndex.getIndividualsByType(typeCls, InstanceRetrievalMode.DIRECT_INSTANCES)
                                             .collect(toImmutableSet());
            }
            else {
                return individualsByTypeIndex.getIndividualsByType(typeCls, InstanceRetrievalMode.ALL_INSTANCES)
                                             .collect(toImmutableSet());
            }
        }
    }
}
