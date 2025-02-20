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
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-29
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class OntologyChangeTranslator_TestCase {

    @Mock
    private OntologyChangeTranslatorVisitor visitor;

    private OntologyChangeTranslator translator;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLAxiom axiom;

    @Mock
    private OWLOntologyChange ontologyChange;

    @Mock
    private OWLAnnotation ontologyAnnotation;

    @Mock
    private OWLImportsDeclaration importDeclaration;

    @BeforeEach
    public void setUp() {
        translator = new OntologyChangeTranslator(visitor);
    }

    @Test
    public void shouldTranslateAddAxiomChange() {
        var addAxiomChange = AddAxiomChange.of(ontologyId, axiom);
        when(visitor.visit(addAxiomChange))
                .thenReturn(ontologyChange);
        var owlOntologyChange = translator.toOwlOntologyChange(addAxiomChange);
        assertThat(owlOntologyChange, is(ontologyChange));
    }

    @Test
    public void shouldTranslateRemoveAxiomChange() {
        var removeAxiomChange = RemoveAxiomChange.of(ontologyId, axiom);
        when(visitor.visit(removeAxiomChange))
                .thenReturn(ontologyChange);
        var owlOntologyChange = translator.toOwlOntologyChange(removeAxiomChange);
        assertThat(owlOntologyChange, is(ontologyChange));
    }

    @Test
    public void shouldTranslateAddOntologyAnnotationChange() {
        var addOntologyAnnotationChange = AddOntologyAnnotationChange.of(ontologyId, ontologyAnnotation);
        when(visitor.visit(addOntologyAnnotationChange))
                .thenReturn(ontologyChange);
        var owlOntologyChange = translator.toOwlOntologyChange(addOntologyAnnotationChange);
        assertThat(owlOntologyChange, is(ontologyChange));
    }

    @Test
    public void shouldTranslateRemoveOntologyAnnotationChange() {
        var removeOntologyAnnotationChange = RemoveOntologyAnnotationChange.of(ontologyId, ontologyAnnotation);
        when(visitor.visit(removeOntologyAnnotationChange))
                .thenReturn(ontologyChange);
        var owlOntologyChange = translator.toOwlOntologyChange(removeOntologyAnnotationChange);
        assertThat(owlOntologyChange, is(ontologyChange));
    }

    @Test
    public void shouldTranslateAddImportChange() {
        var addImportChange = AddImportChange.of(ontologyId, importDeclaration);
        when(visitor.visit(addImportChange))
                .thenReturn(ontologyChange);
        var owlOntologyChange = translator.toOwlOntologyChange(addImportChange);
        assertThat(owlOntologyChange, is(ontologyChange));
    }

    @Test
    public void shouldTranslateRemoveImportChange() {
        var removeImportChange = RemoveImportChange.of(ontologyId, importDeclaration);
        when(visitor.visit(removeImportChange))
                .thenReturn(ontologyChange);
        var owlOntologyChange = translator.toOwlOntologyChange(removeImportChange);
        assertThat(owlOntologyChange, is(ontologyChange));
    }
}
