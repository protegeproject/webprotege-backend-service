package edu.stanford.protege.webprotege.merge;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 02/03/15
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class OntologyDiff_TestCase {


    private OntologyDiff ontologyDiff;

    private OntologyDiff otherOntologyDiff;

    private OWLOntologyID fromOntologyId = new OWLOntologyID();

    private OWLOntologyID toOntologyId = new OWLOntologyID();

    @Mock
    private Diff<OWLAnnotation> annotationDiff;

    @Mock
    private Diff<OWLAxiom> axiomDiff;


    @BeforeEach
    public void setUp() throws Exception {
        ontologyDiff = new OntologyDiff(fromOntologyId, toOntologyId, annotationDiff, axiomDiff);
        otherOntologyDiff = new OntologyDiff(fromOntologyId, toOntologyId, annotationDiff, axiomDiff);
    }

    @Test
public void shouldThrowNullPointerExceptionIf_FromOntologyId_IsNull() {
    assertThrows(NullPointerException.class, () -> { 
        new OntologyDiff(null, toOntologyId, annotationDiff, axiomDiff);
     });
}

    @Test
public void shouldThrowNullPointerExceptionIf_ToOntologyId_IsNull() {
    assertThrows(NullPointerException.class, () -> { 
        new OntologyDiff(fromOntologyId, null, annotationDiff, axiomDiff);
     });
}

    @Test
public void shouldThrowNullPointerExceptionIf_AnnotationDiff_IsNull() {
    assertThrows(NullPointerException.class, () -> { 
        new OntologyDiff(fromOntologyId, toOntologyId, null, axiomDiff);
     });
}

    @Test
public void shouldThrowNullPointerExceptionIf_AxiomDiff_IsNull() {
    assertThrows(NullPointerException.class, () -> { 
        new OntologyDiff(fromOntologyId, toOntologyId, annotationDiff, null);
     });
}

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(ontologyDiff, is(equalTo(ontologyDiff)));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(ontologyDiff, is(not(equalTo(null))));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(ontologyDiff, is(equalTo(otherOntologyDiff)));
    }

    @Test
    public void shouldHaveSameHashCodeAsOther() {
        assertThat(ontologyDiff.hashCode(), is(otherOntologyDiff.hashCode()));
    }

    @Test
    public void shouldGenerateToString() {
        assertThat(ontologyDiff.toString(), startsWith("OntologyDiff"));
    }

    @Test
    public void shouldReturnSupplied_FromOntologyId() {
        assertThat(ontologyDiff.getFromOntologyId(), is(fromOntologyId));
    }

    @Test
    public void shouldReturnSupplied_ToOntologyId() {
        assertThat(ontologyDiff.getToOntologyId(), is(toOntologyId));
    }

    @Test
    public void shouldReturnSupplied_AnnotationDiff() {
        assertThat(ontologyDiff.getAnnotationDiff(), is(annotationDiff));
    }

    @Test
    public void shouldReturnSupplied_AxiomDiff() {
        assertThat(ontologyDiff.getAxiomDiff(), is(axiomDiff));
    }
}