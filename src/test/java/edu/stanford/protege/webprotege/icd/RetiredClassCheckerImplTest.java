package edu.stanford.protege.webprotege.icd;

import edu.stanford.protege.webprotege.index.AnnotationAssertionAxiomsIndex;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.*;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.model.*;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
class RetiredClassCheckerImplTest {

    private OWLDataFactory dataFactory = new OWLDataFactoryImpl();

    @Mock
    private AnnotationAssertionAxiomsIndex annotationAssertionAxiomsIndex;

    @Mock
    private OWLClass clsA, clsB;

    private RetiredClassChecker retiredClassChecker;


    @BeforeEach
    public void setUp() {

        IRI subjectIRIClassA = IRI.create("http://who.int/icd#ClassA");
        IRI subjectIRIClassB = IRI.create("http://who.int/icd#ClassB");
        IRI propertyIRILabel = IRI.create(IcdConstants.LABEL_PROP);
        IRI propertyIRITitle = IRI.create(IcdConstants.TITLE_PROP);
        IRI someX = IRI.create("x");
        OWLAnnotationProperty labelProperty = dataFactory.getOWLAnnotationProperty(propertyIRILabel);
        OWLAnnotationProperty titleProperty = dataFactory.getOWLAnnotationProperty(propertyIRITitle);
        OWLLiteral literalRetired = dataFactory.getOWLLiteral("To be retired - Example Label");
        OWLLiteral literalNonRetired = dataFactory.getOWLLiteral("Example Label");



        OWLAnnotationAssertionAxiom titleAxiomClassA = dataFactory.getOWLAnnotationAssertionAxiom(titleProperty, subjectIRIClassA, someX);
        OWLAnnotationAssertionAxiom retiredLabelAxiom = dataFactory.getOWLAnnotationAssertionAxiom(labelProperty,someX, literalRetired);

        OWLAnnotationAssertionAxiom titleAxiomClassB = dataFactory.getOWLAnnotationAssertionAxiom(titleProperty, subjectIRIClassB, someX);
        OWLAnnotationAssertionAxiom nonRetiredLabelAxiom = dataFactory.getOWLAnnotationAssertionAxiom(labelProperty,someX, literalNonRetired);


        var streamOfAxiomsClsA = Stream.of(titleAxiomClassA, retiredLabelAxiom);
        var streamOfAxiomsClsB = Stream.of(titleAxiomClassB, nonRetiredLabelAxiom);

        when(clsA.getIRI()).thenReturn(subjectIRIClassA);
        when(clsB.getIRI()).thenReturn(subjectIRIClassB);


        when(annotationAssertionAxiomsIndex.getAnnotationAssertionAxioms(subjectIRIClassA)).thenReturn(streamOfAxiomsClsA);
        when(annotationAssertionAxiomsIndex.getAnnotationAssertionAxioms(someX)).thenReturn(Stream.of(retiredLabelAxiom));

        retiredClassChecker = new RetiredClassCheckerImpl(annotationAssertionAxiomsIndex);
    }

    @Test
    void givenOwlClass_whenClassHasRetiredLabel_thenReturnTrue() {
        assertTrue(retiredClassChecker.isRetired(clsA));
    }

    @Test
    void givenOwlClass_whenClassHasRetiredLabel_thenReturnFalse() {
        assertFalse(retiredClassChecker.isRetired(clsB));
    }
}