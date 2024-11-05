package edu.stanford.protege.webprotege.forms.json;

import com.google.common.base.Optional;
import edu.stanford.protege.webprotege.MockingUtils;
import edu.stanford.protege.webprotege.index.ProjectAnnotationAssertionAxiomsBySubjectIndex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLLiteral;
import uk.ac.manchester.cs.owl.owlapi.OWLLiteralImplPlain;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JsonNameExtractorTest {

    private JsonNameExtractor extractor;

    @Mock
    private ProjectAnnotationAssertionAxiomsBySubjectIndex index;

    private final IRI iri = MockingUtils.mockIRI();

    @Mock
    private OWLAnnotationAssertionAxiom axiom;

    @Mock
    private OWLAnnotationProperty property;

    private final OWLLiteral literal = MockingUtils.mockLiteral();

    private final OWLAnnotationValue annotationValue = literal;


    @BeforeEach
    void setUp() {
        extractor = new JsonNameExtractor(index);
    }

    @Test
    void shouldReturnJsonNameWhenMatchingAnnotationIsPresent() {
        var jsonLiteral = "Custom JSON Name";
        when(index.getAnnotationAssertionAxioms(iri)).thenReturn(Stream.of(axiom));
        when(axiom.getProperty()).thenReturn(property);
        when(property.isLabel()).thenReturn(true);
        when(axiom.getValue()).thenReturn(new OWLLiteralImplPlain(jsonLiteral, "json"));
        var result = extractor.getJsonName(iri);
        assertThat(result).isPresent().contains(jsonLiteral);
    }

    @Test
    void shouldReturnEmptyWhenNoMatchingAnnotationIsPresent() {
        when(index.getAnnotationAssertionAxioms(iri)).thenReturn(Stream.of(axiom));
        when(axiom.getProperty()).thenReturn(property);
        when(property.isLabel()).thenReturn(false);  // Property is not a label
        var result = extractor.getJsonName(iri);
        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnEmptyWhenNoAnnotationHasJsonLanguage() {
        when(index.getAnnotationAssertionAxioms(iri)).thenReturn(Stream.of(axiom));
        when(axiom.getProperty()).thenReturn(property);
        when(property.isLabel()).thenReturn(true);
        when(axiom.getValue()).thenReturn(annotationValue);
        var result = extractor.getJsonName(iri);
        assertThat(result).isEmpty();
    }
}
