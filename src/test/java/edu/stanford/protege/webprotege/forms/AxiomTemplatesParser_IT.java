package edu.stanford.protege.webprotege.forms;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Class;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.SubClassOf;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-15
 */
public class AxiomTemplatesParser_IT {

    private AxiomTemplatesParser parser;

    private OWLClass clsA;

    private OWLClass clsB;

    @BeforeEach
    public void setUp() {
        clsA = Class(IRI.create("http://example.org/A"));
        clsB = Class(IRI.create("http://example.org/B"));
        parser = new AxiomTemplatesParser();
    }

    @Test
    public void shouldParsePlainAxiom() {
        var axioms = parser.parseAxiomTemplate(Collections.singleton("SubClassOf(<http://example.org/A> <http://example.org/B>)"));
        assertThat(axioms.size(), is(1));
        assertThat(axioms, containsInAnyOrder(SubClassOf(clsA, clsB)));
    }

    @Test
    public void shouldParseAxiomWithTempName() {
        var axioms = parser.parseAxiomTemplate(Collections.singleton("SubClassOf(<http://example.org/A> <wptmp:Entity#A>)"));
        assertThat(axioms.size(), is(1));
        assertThat(axioms, containsInAnyOrder(SubClassOf(clsA, Class(IRI.create("wptmp:Entity#A")))));
    }

    @Test
    public void shouldParseAxiomWithTempNameWithLangTag() {
        var axioms = parser.parseAxiomTemplate(Collections.singleton("SubClassOf(<http://example.org/A> <wptmp:Entity@en#A>)"));
        assertThat(axioms.size(), is(1));
        assertThat(axioms, containsInAnyOrder(SubClassOf(clsA, Class(IRI.create("wptmp:Entity@en#A")))));
    }
}
