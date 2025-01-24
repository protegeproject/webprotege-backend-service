package edu.stanford.protege.webprotege.crud;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.criteria.CompositeHierarchyPositionCriteria;
import edu.stanford.protege.webprotege.criteria.RootCriteria;
import edu.stanford.protege.webprotege.match.HierarchyPositionCriteriaMatchableEntityTypesExtractor;
import edu.stanford.protege.webprotege.match.Matcher;
import edu.stanford.protege.webprotege.match.MatcherFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-08
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class EntityIriPrefixResolver_TestCase {

    public static final String FALLBACK_IRI_PREFIX = "FallbackIriPrefix";

    private static final String CONDITIONAL_IRI_PREFIX = "ConditionalIriPrefix";

    private EntityIriPrefixResolver resolver;

    @Mock
    private MatcherFactory matcherFactory;

    @Mock
    private RootCriteria criteria;

    @Mock
    private Matcher<OWLEntity> matcher;

    @Mock
    private OWLEntity parentEntity;

    @Mock
    private OWLEntity otherParent;

    @Mock
    private EntityCrudKitPrefixSettings prefixSettings;

    @Mock
    private ConditionalIriPrefix conditionalPrefix;

    @Mock
    private CompositeHierarchyPositionCriteria hierarchyCriteria;

    @Mock
    private EntityIriPrefixCriteriaRewriter entityIriPrefixCriteriaRewriter;

    private EntityType<?> entityType = EntityType.CLASS;

    @Mock
    private HierarchyPositionCriteriaMatchableEntityTypesExtractor leafCriteriaTypesExtractor;

    @BeforeEach
    public void setUp() {
        when(leafCriteriaTypesExtractor.getMatchableEntityTypes(any()))
                .thenReturn(ImmutableSet.of(entityType));
        resolver = new EntityIriPrefixResolver(matcherFactory, entityIriPrefixCriteriaRewriter,
                                               leafCriteriaTypesExtractor);

        when(entityIriPrefixCriteriaRewriter.rewriteCriteria(hierarchyCriteria))
                .thenReturn(criteria);

        when(prefixSettings.getConditionalIriPrefixes())
                .thenReturn(ImmutableList.of(conditionalPrefix));
        when(prefixSettings.getIRIPrefix()).thenReturn(FALLBACK_IRI_PREFIX);
        when(conditionalPrefix.getCriteria())
                .thenReturn(hierarchyCriteria);
        when(conditionalPrefix.getIriPrefix())
                .thenReturn(CONDITIONAL_IRI_PREFIX);

        when(matcherFactory.getMatcher(criteria))
                .thenReturn(matcher);
    }

    @Test
    public void shouldReturnFallbackPrefixForNonMatch() {
        var resolvedPrefix = resolver.getIriPrefix(prefixSettings, entityType, ImmutableList.of(otherParent));
        assertThat(resolvedPrefix, is(FALLBACK_IRI_PREFIX));
    }

    @Test
    public void shouldReturnFallbackPrefixForNonMatchOfEmptyParents() {
        var resolvedPrefix = resolver.getIriPrefix(prefixSettings, entityType, ImmutableList.of());
        assertThat(resolvedPrefix, is(FALLBACK_IRI_PREFIX));
    }

    @Test
    public void shouldReturnMatchedPrefixForMatch() {
        when(matcher.matches(parentEntity))
                .thenReturn(true);
        var resolvedPrefix = resolver.getIriPrefix(prefixSettings, entityType, ImmutableList.of(parentEntity));
        assertThat(resolvedPrefix, is(CONDITIONAL_IRI_PREFIX));
    }
}
