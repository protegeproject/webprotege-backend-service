package edu.stanford.protege.webprotege.shortform;

import com.google.common.base.Optional;
import edu.stanford.protege.webprotege.project.DefaultOntologyIdManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 25/03/2014
 */
@SuppressWarnings("Guava")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class WebProtegeOntologyIRIShortFormProvider_TestCase {


    private IRI iri;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private DefaultOntologyIdManager defaultOntologyIdManager;

    @BeforeEach
    public void setUp() throws Exception {
        iri = IRI.create("http://stuff.com/OntologyA");
        when(defaultOntologyIdManager.getDefaultOntologyId()).thenReturn(ontologyId);
        when(ontologyId.getOntologyIRI())
                .thenReturn(Optional.of(iri));
    }

    @Test
    public void shouldReturnStandardShortFormForRootOntology() {
        var sfp = new WebProtegeOntologyIRIShortFormProvider(defaultOntologyIdManager);
        var shortForm = sfp.getShortForm(ontologyId);
        assertThat(shortForm, is(equalTo("root-ontology")));
    }

    @Test
    public void shouldReturnStandardShortFormForRootOntologyIRI() {
        var sfp = new WebProtegeOntologyIRIShortFormProvider(defaultOntologyIdManager);
        var shortForm = sfp.getShortForm(iri);
        assertThat(shortForm, is(equalTo("root-ontology")));
    }
}
