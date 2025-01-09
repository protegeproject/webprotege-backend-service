package edu.stanford.protege.webprotege.match;

import edu.stanford.protege.webprotege.index.AnnotationAssertionAxiomsIndex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.model.*;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Jun 2018
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class NonUniqueLangTagsMatcher_TestCase {

    private NonUniqueLangTagsMatcher matcher;

    @Mock
    private AnnotationAssertionAxiomsIndex hasAxioms;

    private Set<OWLAnnotationAssertionAxiom> axioms = new HashSet<>();

    @Mock
    private OWLEntity entity;

    @Mock
    private IRI iri;

    @Mock
    private OWLAnnotationAssertionAxiom axA, axB;

    @Mock
    private OWLAnnotationProperty prop, propA, propB;

    @Mock
    private OWLLiteral literalA, literalB;

    @Mock
    private Matcher<OWLAnnotationProperty> propertyMatcher;


    @BeforeEach
    public void setUp() {
        matcher = new NonUniqueLangTagsMatcher(hasAxioms, propertyMatcher);
        when(propertyMatcher.matches(any())).thenReturn(true);
        when(hasAxioms.getAnnotationAssertionAxioms(any())).thenReturn(axioms.stream());

        when(entity.getIRI()).thenReturn(iri);

        when(axA.getProperty()).thenReturn(propA);
        when(axA.getValue()).thenReturn(literalA);

        when(axB.getProperty()).thenReturn(propB);
        when(axB.getValue()).thenReturn(literalB);

        axioms.add(axA);
        axioms.add(axB);
    }

    @Test
    public void shouldNotMatchDifferentPropertiesDifferentLangs() {
        when(literalA.getLang()).thenReturn("lA");
        when(literalB.getLang()).thenReturn("lB");
        assertThat(matcher.matches(entity), is(false));
    }

    @Test
    public void shouldNotMatchSameLangDifferentProperty() {
        when(literalA.getLang()).thenReturn("lA");
        when(literalB.getLang()).thenReturn("lA");
        assertThat(matcher.matches(entity), is(false));
    }

    @Test
    public void shouldMatchSameLangSameProperty() {
        when(axA.getProperty()).thenReturn(prop);
        when(axB.getProperty()).thenReturn(prop);
        when(literalA.getLang()).thenReturn("lA");
        when(literalB.getLang()).thenReturn("lA");
        assertThat(matcher.matches(entity), is(true));
    }
}
