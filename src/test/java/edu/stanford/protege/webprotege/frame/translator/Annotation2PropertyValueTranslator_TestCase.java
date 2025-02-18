package edu.stanford.protege.webprotege.frame.translator;

import edu.stanford.protege.webprotege.frame.PlainPropertyAnnotationValue;
import edu.stanford.protege.webprotege.frame.PlainPropertyValue;
import edu.stanford.protege.webprotege.frame.State;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLLiteral;
import uk.ac.manchester.cs.owl.owlapi.OWLAnnotationImpl;

import java.util.Collections;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-14
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class Annotation2PropertyValueTranslator_TestCase {

    private Annotation2PropertyValueTranslator translator;

    @Mock
    private OWLAnnotationProperty property;

    @Mock
    private OWLLiteral literal;

    @Mock
    private IRI iri;

    @BeforeEach
    public void setUp() {
        translator = new Annotation2PropertyValueTranslator();
    }

    @Test
    public void shouldTranslateAnnotationWithLiteralValue() {
        var annotation = new OWLAnnotationImpl(property, literal, Collections.emptySet());
        Set<PlainPropertyValue> propertyValues =  translator.translate(annotation, State.ASSERTED);
        assertThat(propertyValues, Matchers.hasItem(PlainPropertyAnnotationValue.get(property, literal, State.ASSERTED)));
    }

    @Test
    public void shouldTranslateSpecifiedState() {
        var annotation = new OWLAnnotationImpl(property, literal, Collections.emptySet());
        Set<PlainPropertyValue> propertyValues =  translator.translate(annotation, State.DERIVED);
        assertThat(propertyValues, Matchers.hasItem(PlainPropertyAnnotationValue.get(property, literal, State.DERIVED)));
    }

    @Test
    public void shouldIri() {
        var annotation = new OWLAnnotationImpl(property, iri, Collections.emptySet());
        Set<PlainPropertyValue> propertyValues =  translator.translate(annotation, State.ASSERTED);
        assertThat(propertyValues, Matchers.hasItem(PlainPropertyAnnotationValue.get(property, iri, State.ASSERTED)));
    }
}
