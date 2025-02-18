package edu.stanford.protege.webprotege.mansyntax;

import edu.stanford.protege.webprotege.index.ProjectOntologiesIndex;
import edu.stanford.protege.webprotege.project.DefaultOntologyIdManager;
import edu.stanford.protege.webprotege.shortform.WebProtegeOntologyIRIShortFormProvider;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 25/03/2014
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ShellOntologyChecker_TestCase {

    private static final String ONTOLOGY_SHORT_FORM = "OntologyShortForm";

    private static final String DEFAULT_ONTOLOGY_SHORT_FORM = "DefaultOntologyShortForm";

    @Mock
    protected WebProtegeOntologyIRIShortFormProvider ontologyIRIShortFormProvider;

    @Mock
    private ProjectOntologiesIndex projectOntologiesIndex;

    @Mock
    private DefaultOntologyIdManager defaultOntologyManager;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLOntologyID defaultOntologyId;

    private ShellOntologyChecker checker;

    @BeforeEach
    public void setUp() {
        checker = new ShellOntologyChecker(projectOntologiesIndex,
                                           ontologyIRIShortFormProvider,
                                           defaultOntologyManager);
        when(defaultOntologyManager.getDefaultOntologyId())
                .thenReturn(defaultOntologyId);
        when(projectOntologiesIndex.getOntologyIds())
                .thenAnswer(invocation -> Stream.of(ontologyId, defaultOntologyId));
        when(ontologyIRIShortFormProvider.getShortForm(ontologyId))
                .thenReturn(ONTOLOGY_SHORT_FORM);
        when(ontologyIRIShortFormProvider.getShortForm(defaultOntologyId))
                .thenReturn(DEFAULT_ONTOLOGY_SHORT_FORM);
    }

    /**
     * Test for legacy behaviour
     */
    @Test
    public void shouldReturnRootOntologyForNullArgument() {
        OWLOntology ont = checker.getOntology(null);
        assertThat(ont, Matchers.is(notNullValue()));
        assertThat(ont.getOntologyID(), is(equalTo(defaultOntologyId)));
    }

    @Test
    public void shouldReturnNullForUnknownShortForm() {
        OWLOntology ont = checker.getOntology("x");
        assertThat(ont, is((OWLOntology) null));
    }

    @Test
    public void shouldReturnOntologyWithGivenShortForm() {
        OWLOntology ont = checker.getOntology(ONTOLOGY_SHORT_FORM);
        assertThat(ont, Matchers.is(notNullValue()));
        assertThat(ont.getOntologyID(), is(equalTo(ontologyId)));
    }

}
