package edu.stanford.protege.webprotege.mansyntax;

import edu.stanford.protege.webprotege.index.ProjectOntologiesIndex;
import edu.stanford.protege.webprotege.project.DefaultOntologyIdManager;
import edu.stanford.protege.webprotege.shortform.DictionaryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.expression.OWLOntologyChecker;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OntologyAxiomPair;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-22
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ManchesterSyntaxFrameParser_TestCase {

    private static final String CLASS_A_SUB_CLASS_OF_B = "Class: A  SubClassOf: B";

    private static final String CLASS_A_SUB_CLASS_OF_B_IN_ONT = "Class: A  SubClassOf: [in theontology] B";

    private ManchesterSyntaxFrameParser parser;

    @Mock
    private OWLOntologyChecker ontologyChecker;

    private OWLDataFactory dataFactory = new OWLDataFactoryImpl();

    @Mock
    private DictionaryManager dictionaryManager;

    @Mock
    private DefaultOntologyIdManager defaultOntologyIdManager;

    @Mock
    private OWLOntologyID theOntologyId;

    private OWLClass clsA = dataFactory.getOWLClass(mock(IRI.class));

    private OWLClass clsB = dataFactory.getOWLClass(mock(IRI.class));

    private final OWLSubClassOfAxiom expectedAxiom = dataFactory.getOWLSubClassOfAxiom(clsA, clsB);

    @Mock
    private OWLOntologyID defaultOntologyId;

    @Mock
    private ProjectOntologiesIndex projectOntologiesIndex;

    @BeforeEach
    public void setUp() {
        dataFactory = new OWLDataFactoryImpl();

        when(ontologyChecker.getOntology("theontology"))
                .thenReturn(ShellOwlOntology.get(theOntologyId));

        when(defaultOntologyIdManager.getDefaultOntologyId())
                .thenReturn(defaultOntologyId);

        when(dictionaryManager.getEntities("A"))
                .thenAnswer(invocation -> Stream.of(clsA));
        when(dictionaryManager.getEntities("B"))
                .thenAnswer(invocation -> Stream.of(clsB));
        parser = new ManchesterSyntaxFrameParser(ontologyChecker,
                                                 dataFactory,
                                                 dictionaryManager,
                                                 defaultOntologyIdManager);
    }

    @Test
    public void shouldParseAxiomWithoutOntologyNameLocator() {
        var axiomOntologyPairs = parser.parse(CLASS_A_SUB_CLASS_OF_B, Collections::emptySet);
        var axiomOntologyPair = getSubClassOfAxiomPair(axiomOntologyPairs);
        assertThat(getOntologyId(axiomOntologyPair), is(defaultOntologyId));
        assertThat(axiomOntologyPair.getAxiom(), is(expectedAxiom));
    }

    @Test
    public void shouldParseAxiomWithOntologyNameLocator() {
        var axiomOntologyPairs = parser.parse(CLASS_A_SUB_CLASS_OF_B_IN_ONT, Collections::emptySet);
        var axiomOntologyPair = getSubClassOfAxiomPair(axiomOntologyPairs);
        assertThat(getOntologyId(axiomOntologyPair), is(theOntologyId));
        assertThat(axiomOntologyPair.getAxiom(), is(expectedAxiom));
    }


    private OWLOntologyID getOntologyId(OntologyAxiomPair axiomOntologyPair) {
        return Objects.requireNonNull(axiomOntologyPair.getOntology())
                      .getOntologyID();
    }

    private OntologyAxiomPair getSubClassOfAxiomPair(Set<OntologyAxiomPair> axiomOntologyPairs) {
        return axiomOntologyPairs.stream()
                                 .filter(pair -> pair.getAxiom() instanceof OWLSubClassOfAxiom)
                                 .findFirst()
                                 .orElseThrow();
    }

}
