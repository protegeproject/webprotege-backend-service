package edu.stanford.protege.webprotege.mansyntax;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntologyID;
import uk.ac.manchester.cs.owl.owlapi.OWLOntologyImpl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-22
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ShellOwlOntology_TestCase {

    private ShellOwlOntology ontology;

    @Mock
    private OWLOntologyID ontologyId;

    @BeforeEach
    public void setUp() {
        ontology = ShellOwlOntology.get(ontologyId);
    }

    @Test
    public void shouldGetSuppliedOntologyId() {
        assertThat(ontology.getOntologyID(), is(ontologyId));
    }

    @Test
    public void shouldBeEqualToShellOntologyWithSameId() {
        var otherOntology = ShellOwlOntology.get(ontologyId);
        assertThat(ontology, is(equalTo(otherOntology)));
    }

    @Test
    public void shouldHaveSameHashCodeAsOtherShellOntologyWithSameOntologyId() {
        var otherOntology = ShellOwlOntology.get(ontologyId);
        assertThat(ontology.hashCode(), is(equalTo(otherOntology.hashCode())));
    }

    @Test
    public void shouldBeEqualToOwlApiImplementationWithSameOntologyId() throws Exception {
        var owlapiOntology = new OWLOntologyImpl(OWLManager.createOWLOntologyManager(), ontologyId);
        assertThat(owlapiOntology, is(equalTo(ontology)));
    }

    @Test
    public void shouldHaveSameHashCodeAsOwlApiImplementationWithSameOntologyId() throws Exception {
        var owlapiOntology = new OWLOntologyImpl(OWLManager.createOWLOntologyManager(), ontologyId);
        assertThat(owlapiOntology.hashCode(), is(equalTo(ontology.hashCode())));
    }
}
