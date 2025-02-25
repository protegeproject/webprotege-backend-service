package edu.stanford.protege.webprotege.crud;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.criteria.*;
import edu.stanford.protege.webprotege.match.HierarchyPositionCriteriaMatchableEntityTypesExtractor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi.model.EntityType;

import static edu.stanford.protege.webprotege.MockingUtils.mockOWLClass;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.emptyIterable;
import static org.junit.Assert.assertThat;

public class HierarchyPositionCriteriaMatchableEntityTypesExtractor_TestCase {

    private HierarchyPositionCriteriaMatchableEntityTypesExtractor extractor;

    private final SubClassOfCriteria subClassOfCriteria = SubClassOfCriteria.get(mockOWLClass(),
                                                                                 HierarchyFilterType.DIRECT);

    private final InstanceOfCriteria instanceOfCriteria = InstanceOfCriteria.get(mockOWLClass(),
                                                                                 HierarchyFilterType.DIRECT);

    @BeforeEach
    public void setUp() throws Exception {
        extractor = new HierarchyPositionCriteriaMatchableEntityTypesExtractor();
    }

    @Test
    public void shouldReturnClassTypeForSubClassOfCriteria() {
        var types = extractor.getMatchableEntityTypes(subClassOfCriteria);
        assertThat(types, containsInAnyOrder(EntityType.CLASS));
    }

    @Test
    public void shouldReturnIndividualTypeForInstanceOfCriteria() {
        var types = extractor.getMatchableEntityTypes(instanceOfCriteria);
        assertThat(types, containsInAnyOrder(EntityType.NAMED_INDIVIDUAL));
    }

    @Test
    public void shouldReturnEmptySetForSubClassOfCriteriaAndInstanceOfCriteria() {
        var criteria = CompositeHierarchyPositionCriteria.get(ImmutableList.of(instanceOfCriteria, subClassOfCriteria),
                                                              MultiMatchType.ALL);
        var types = extractor.getMatchableEntityTypes(criteria);
        assertThat(types, emptyIterable());
    }

    @Test
    public void shouldReturnClassTypeAndIndividualTypeForSubClassOfCriteriaOrInstanceOfCriteria() {
        var criteria = CompositeHierarchyPositionCriteria.get(ImmutableList.of(instanceOfCriteria, subClassOfCriteria),
                                                              MultiMatchType.ANY);
        var types = extractor.getMatchableEntityTypes(criteria);
        assertThat(types, containsInAnyOrder(EntityType.CLASS, EntityType.NAMED_INDIVIDUAL));
    }
}