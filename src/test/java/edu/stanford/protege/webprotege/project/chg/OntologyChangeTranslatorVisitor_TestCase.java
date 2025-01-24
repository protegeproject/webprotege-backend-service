package edu.stanford.protege.webprotege.project.chg;

import edu.stanford.protege.webprotege.change.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.model.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-29
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class OntologyChangeTranslatorVisitor_TestCase {

    private OntologyChangeTranslatorVisitor visitor;

    @Mock
    private OWLOntologyManager ontologyManager;

    @Mock
    private OWLOntologyID ontologyId, otherOntologyId;

    @Mock
    private OWLOntology ontology;

    @Mock
    private OWLAxiom axiom;

    @Mock
    private OWLAnnotation ontologyAnnotation;

    @Mock
    private OWLImportsDeclaration importsDeclaration;

    @BeforeEach
    public void setUp() {
        when(ontologyManager.getOntology(ontologyId))
                .thenReturn(ontology);

        visitor = new OntologyChangeTranslatorVisitor(ontologyManager);
    }

    @Test
    public void shouldVisitAddAxiomChangeWithKnownOntology() {
        var addAxiomChange = AddAxiomChange.of(ontologyId, axiom);
        var owlOntologyChange = visitor.visit(addAxiomChange);
        var addAxiomOwlOntologyChange = assertThatChangeIsOfClass(owlOntologyChange, AddAxiom.class);
        assertThat(addAxiomOwlOntologyChange.getOntology(), is(ontology));
        assertThat(addAxiomOwlOntologyChange.getAxiom(), is(axiom));
    }

    @Test
public void shouldVisitAddAxiomChangeWithUnknownOntology() {
    assertThrows(UnknownOWLOntologyException.class, () -> { 
        var addAxiomChange = AddAxiomChange.of(otherOntologyId, axiom);
        visitor.visit(addAxiomChange);
     });
}

    @Test
    public void shouldVisitRemoveAxiomChangeWithKnownOntology() {
        var removeAxiomChange = RemoveAxiomChange.of(ontologyId, axiom);
        var owlOntologyChange = visitor.visit(removeAxiomChange);
        var removeAxiomOwlOntologyChange = assertThatChangeIsOfClass(owlOntologyChange, RemoveAxiom.class);
        assertThat(removeAxiomOwlOntologyChange.getOntology(), is(ontology));
        assertThat(removeAxiomOwlOntologyChange.getAxiom(), is(axiom));
    }

    @Test
public void shouldVisitRemoveAxiomChangeWithUnknownOntology() {
    assertThrows(UnknownOWLOntologyException.class, () -> { 
        var removeAxiomChange = RemoveAxiomChange.of(otherOntologyId, axiom);
        visitor.visit(removeAxiomChange);
     });
}

    @Test
    public void shouldVisitAddOntologyAnnotationChangeWithKnownOntology() {
        var addOntologyAnnotationChange = AddOntologyAnnotationChange.of(ontologyId, ontologyAnnotation);
        var owlOntologyChange = visitor.visit(addOntologyAnnotationChange);
        var addOntologyAnnotationOwlOntologyChange = assertThatChangeIsOfClass(owlOntologyChange, AddOntologyAnnotation.class);
        assertThat(addOntologyAnnotationOwlOntologyChange.getOntology(), is(ontology));
        assertThat(addOntologyAnnotationOwlOntologyChange.getAnnotation(), is(ontologyAnnotation));
    }

    @Test
public void shouldVisitAddOntologyAnnotationChangeWithUnknownOntology() {
    assertThrows(UnknownOWLOntologyException.class, () -> { 
        var addOntologyAnnotationChange = AddOntologyAnnotationChange.of(otherOntologyId, ontologyAnnotation);
        visitor.visit(addOntologyAnnotationChange);
     });
}

    @Test
    public void shouldVisitRemoveOntologyAnnotationChangeWithKnownOntology() {
        var removeOntologyAnnotationChange = RemoveOntologyAnnotationChange.of(ontologyId, ontologyAnnotation);
        var owlOntologyChange = visitor.visit(removeOntologyAnnotationChange);
        var removeOntologyAnnotationOwlOntologyChange = assertThatChangeIsOfClass(owlOntologyChange, RemoveOntologyAnnotation.class);
        assertThat(removeOntologyAnnotationOwlOntologyChange.getOntology(), is(ontology));
        assertThat(removeOntologyAnnotationOwlOntologyChange.getAnnotation(), is(ontologyAnnotation));
    }
    
    

    @Test
public void shouldVisitRemoveOntologyAnnotationChangeWithUnknownOntology() {
    assertThrows(UnknownOWLOntologyException.class, () -> { 
        var removeOntologyAnnotationChange = RemoveOntologyAnnotationChange.of(otherOntologyId, ontologyAnnotation);
        visitor.visit(removeOntologyAnnotationChange);
     });
}



    @Test
    public void shouldVisitAddImportsChangeWithKnownOntology() {
        var addImportsChange = AddImportChange.of(ontologyId, importsDeclaration);
        var owlOntologyChange = visitor.visit(addImportsChange);
        var addImportsOwlOntologyChange = assertThatChangeIsOfClass(owlOntologyChange, AddImport.class);
        assertThat(addImportsOwlOntologyChange.getOntology(), is(ontology));
        assertThat(addImportsOwlOntologyChange.getImportDeclaration(), is(importsDeclaration));
    }

    @Test
public void shouldVisitAddImportsChangeWithUnknownOntology() {
    assertThrows(UnknownOWLOntologyException.class, () -> { 
        var addImportsChange = AddImportChange.of(otherOntologyId, importsDeclaration);
        visitor.visit(addImportsChange);
     });
}

    @Test
    public void shouldVisitRemoveImportsChangeWithKnownOntology() {
        var removeImportsChange = RemoveImportChange.of(ontologyId, importsDeclaration);
        var owlOntologyChange = visitor.visit(removeImportsChange);
        var removeImportsOwlOntologyChange = assertThatChangeIsOfClass(owlOntologyChange, RemoveImport.class);
        assertThat(removeImportsOwlOntologyChange.getOntology(), is(ontology));
        assertThat(removeImportsOwlOntologyChange.getImportDeclaration(), is(importsDeclaration));
    }

    @Test
public void shouldVisitRemoveImportsChangeWithUnknownOntology() {
    assertThrows(UnknownOWLOntologyException.class, () -> { 
        var removeImportsChange = RemoveImportChange.of(otherOntologyId, importsDeclaration);
        visitor.visit(removeImportsChange);
     });
}

    @Test
public void shouldThrowRuntimeExceptionForDefaultValue() {
    assertThrows(RuntimeException.class, () -> { 
        visitor.getDefaultReturnValue();
     });
}

    private <C extends OWLOntologyChange> C assertThatChangeIsOfClass(OWLOntologyChange owlOntologyChange, Class<C> cls) {
        assertThat(owlOntologyChange, is(instanceOf(cls)));
        return cls.cast(owlOntologyChange);
    }
}
