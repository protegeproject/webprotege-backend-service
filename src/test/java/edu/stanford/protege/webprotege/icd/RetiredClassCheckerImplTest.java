package edu.stanford.protege.webprotege.icd;

import edu.stanford.protege.webprotege.index.AnnotationAssertionAxiomsIndex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.model.*;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
class RetiredClassCheckerImplTest {

    private final OWLDataFactory dataFactory = new OWLDataFactoryImpl();

    @Mock
    private AnnotationAssertionAxiomsIndex annotationAssertionAxiomsIndex;

    @Mock
    private OWLClass clsA, clsB;

    private OWLLiteralExtractorManager owlLiteralExtractorManager;

    IRI subjectIRIClassA, subjectIRIClassB;
    Stream<OWLAnnotationAssertionAxiom> streamOfAxiomsClsA, streamOfAxiomsClsB;
    OWLAnnotationAssertionAxiom retiredLabelAxiom, nonRetiredLabelAxiom;

    IRI iriRetiredLinkProperties;
    IRI iriNonRetiredLinkProperties;

    private RetiredClassChecker retiredClassChecker;


    @BeforeEach
    public void setUp() {

        subjectIRIClassA = IRI.create("http://who.int/icd#ClassA");
        subjectIRIClassB = IRI.create("http://who.int/icd#ClassB");

        IRI propertyIRILabel = IRI.create(IcdConstants.LABEL_PROP);
        IRI propertyIRITitle = IRI.create(IcdConstants.TITLE_PROP);

        iriRetiredLinkProperties = IRI.create("iriRetiredLinkProperties");
        iriNonRetiredLinkProperties = IRI.create("iriNonRetiredLinkProperties");

        OWLAnnotationProperty labelProperty = dataFactory.getOWLAnnotationProperty(propertyIRILabel);
        OWLAnnotationProperty titleProperty = dataFactory.getOWLAnnotationProperty(propertyIRITitle);

        OWLLiteral literalRetired = dataFactory.getOWLLiteral("To be retired - Example Label");
        OWLLiteral literalNonRetired = dataFactory.getOWLLiteral("Example Label");


        OWLAnnotationAssertionAxiom titleAxiomClassA = dataFactory.getOWLAnnotationAssertionAxiom(titleProperty, subjectIRIClassA, iriRetiredLinkProperties);
        retiredLabelAxiom = dataFactory.getOWLAnnotationAssertionAxiom(labelProperty, iriRetiredLinkProperties, literalRetired);

        OWLAnnotationAssertionAxiom titleAxiomClassB = dataFactory.getOWLAnnotationAssertionAxiom(titleProperty, subjectIRIClassB, iriNonRetiredLinkProperties);
        nonRetiredLabelAxiom = dataFactory.getOWLAnnotationAssertionAxiom(labelProperty, iriNonRetiredLinkProperties, literalNonRetired);


        streamOfAxiomsClsA = Stream.of(titleAxiomClassA, retiredLabelAxiom);
        streamOfAxiomsClsB = Stream.of(titleAxiomClassB, nonRetiredLabelAxiom);

        when(clsA.getIRI()).thenReturn(subjectIRIClassA);
        when(clsB.getIRI()).thenReturn(subjectIRIClassB);

        owlLiteralExtractorManager = new OWLLiteralExtractorManager(annotationAssertionAxiomsIndex);


        retiredClassChecker = new RetiredClassCheckerImpl(owlLiteralExtractorManager);
    }

    @Test
    void givenOwlClass_whenClassHasRetiredLabel_thenReturnTrue() {
        when(annotationAssertionAxiomsIndex.getAnnotationAssertionAxioms(subjectIRIClassA)).thenReturn(streamOfAxiomsClsA);
        when(annotationAssertionAxiomsIndex.getAnnotationAssertionAxioms(iriRetiredLinkProperties)).thenReturn(Stream.of(retiredLabelAxiom));


        assertTrue(retiredClassChecker.isRetired(clsA));
        verify(annotationAssertionAxiomsIndex).getAnnotationAssertionAxioms(subjectIRIClassA);
        verify(annotationAssertionAxiomsIndex).getAnnotationAssertionAxioms(iriRetiredLinkProperties);
    }

    @Test
    void givenOwlClass_whenClassHasRetiredLabel_thenReturnFalse() {
        when(annotationAssertionAxiomsIndex.getAnnotationAssertionAxioms(subjectIRIClassB)).thenReturn(streamOfAxiomsClsB);
        when(annotationAssertionAxiomsIndex.getAnnotationAssertionAxioms(iriNonRetiredLinkProperties)).thenReturn(Stream.of(nonRetiredLabelAxiom));


        assertFalse(retiredClassChecker.isRetired(clsB));
        verify(annotationAssertionAxiomsIndex).getAnnotationAssertionAxioms(subjectIRIClassB);
        verify(annotationAssertionAxiomsIndex).getAnnotationAssertionAxioms(iriNonRetiredLinkProperties);
    }
}