package edu.stanford.protege.webprotege.mansyntax;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.util.OntologyAxiomPair;

import java.util.Collections;
import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 25/03/2014
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class OntologyAxiomPairChangeGeneratorTestCase {

    @Mock
    private OWLOntology ont;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLAxiom ax;

    private OntologyAxiomPair pair;

    private OntologyAxiomPairChangeGenerator generator;

    @BeforeEach
    public void setUp() throws Exception {
        pair = mock(OntologyAxiomPair.class);
        when(ont.getOntologyID())
                .thenReturn(ontologyId);
        when(pair.getOntology())
                .thenReturn(ont);
        when(pair.getAxiom())
                .thenReturn(ax);
        generator = new OntologyAxiomPairChangeGenerator();
    }

    @Test
    public void shouldGenerateAddAxiom() {
        Set<OntologyAxiomPair> from = Collections.emptySet();
        Set<OntologyAxiomPair> to = Collections.singleton(pair);
        var changes = generator.generateChanges(from, to);
        assertThat(changes, hasSize(1));
        var change = changes.get(0);
        assertThat(change.getOntologyId(), is(equalTo(ontologyId)));
        assertThat(change.getAxiomOrThrow(), is(equalTo(ax)));
        assertThat(change.isAddAxiom(), is(true));
    }

    @Test
    public void shouldGenerateRemoveAxiom() {
        Set<OntologyAxiomPair> from = Collections.singleton(pair);
        Set<OntologyAxiomPair> to = Collections.emptySet();
        OntologyAxiomPairChangeGenerator generator = new OntologyAxiomPairChangeGenerator();
        var changes = generator.generateChanges(from, to);
        assertThat(changes, hasSize(1));
        var change = changes.get(0);
        assertThat(change.getOntologyId(), is(equalTo(ontologyId)));
        assertThat(change.getAxiomOrThrow(), is(equalTo(ax)));
        assertThat(change.isRemoveAxiom(), is(true));
    }

    @Test
    public void shouldGenerateEmptyList() {
        Set<OntologyAxiomPair> from = Collections.singleton(pair);
        Set<OntologyAxiomPair> to = Collections.singleton(pair);
        OntologyAxiomPairChangeGenerator generator = new OntologyAxiomPairChangeGenerator();
        var changes = generator.generateChanges(from, to);
        assertThat(changes, is(empty()));
    }

}
