package edu.stanford.protege.webprotege.index.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import edu.stanford.protege.webprotege.change.AddAxiomChange;
import edu.stanford.protege.webprotege.change.RemoveAxiomChange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.model.*;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-09-07
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AxiomMultimap_Nary_TestCase {

    private final KeyValueExtractor<Iterable<OWLIndividual>, OWLSameIndividualAxiom> extractor = this::extractIndividuals;

    private AxiomMultimapIndex<OWLIndividual, OWLSameIndividualAxiom> index;

    private Multimap<Key<OWLIndividual>, OWLSameIndividualAxiom> backingMap =
            MultimapBuilder.hashKeys()
                           .arrayListValues()
                           .build();

    @Mock
    private OWLIndividual indA, indB;

    @Mock
    private OWLSameIndividualAxiom axiom;

    @Mock
    private OWLOntologyID ontologyId;

    private Iterable<OWLIndividual> extractIndividuals(OWLSameIndividualAxiom axiom) {
        return axiom.getIndividuals();
    }

    @BeforeEach
    public void setUp() {
        index = AxiomMultimapIndex.createWithNaryKeyValueExtractor(OWLSameIndividualAxiom.class,
                                                                   extractor,
                                                                   backingMap);
        when(axiom.getIndividuals())
                .thenReturn(Set.of(indA, indB));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNpeIfOntologyIdIsNull() {
    assertThrows(NullPointerException.class, () -> { 
        index.getAxioms(indA, null);
     });
}

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNpeIfValueIsNull() {
    assertThrows(NullPointerException.class, () -> { 
        index.getAxioms(null, ontologyId);
     });
}

    @Test
    public void shouldGetEmptyStreamBeforeAnyChange() {
        var axiomsStream = index.getAxioms(indA, ontologyId);
        assertThat(axiomsStream.count(), is(0L));
    }

    @Test
    public void shouldContainAxiomInBackingMapAfterAdd() {
        var chg = AddAxiomChange.of(ontologyId, axiom);
        index.applyChanges(ImmutableList.of(chg));
        assertThat(backingMap.values(), hasItem(axiom));
    }

    @Test
    public void shouldNotContainAxiomInBackingMapAfterRemove() {
        var chg = AddAxiomChange.of(ontologyId, axiom);
        index.applyChanges(ImmutableList.of(chg));
        assertThat(backingMap.values(), hasItem(axiom));
        var remChg = RemoveAxiomChange.of(ontologyId, axiom);
        index.applyChanges(ImmutableList.of(remChg));
        assertThat(backingMap.values(), not(hasItem(axiom)));
    }

    @Test
    public void shouldAddDuplicatesByDefault() {
        var chg = AddAxiomChange.of(ontologyId, axiom);
        index.applyChanges(ImmutableList.of(chg, chg));
        assertThat(backingMap.size(), is(4));
    }

    @Test
    public void shouldNotAddDuplicatesIfFlagIsSet() {
        var chg = AddAxiomChange.of(ontologyId, axiom);
        index.setAllowDuplicates(false);
        index.applyChanges(ImmutableList.of(chg, chg));
        assertThat(backingMap.size(), is(2));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldContainAllKeyValues() {
        var chg = AddAxiomChange.of(ontologyId, axiom);
        index.applyChanges(ImmutableList.of(chg));
        assertThat(backingMap.keys(), containsInAnyOrder(Key.get(ontologyId, indA), Key.get(ontologyId, indB)));
    }

    @Test
    public void shouldNotAddAxiomsWithNullKeyValue() {
        var subClassOfAxiom = mock(OWLSubClassOfAxiom.class);
        index.applyChanges(ImmutableList.of(AddAxiomChange.of(ontologyId, subClassOfAxiom)));
        assertThat(backingMap.size(), is(0));
    }

    @Test
    public void shouldIgnoreIrrelevantChanges() {
        var otherAxiom = mock(OWLClassAssertionAxiom.class);
        index.applyChanges(ImmutableList.of(AddAxiomChange.of(ontologyId, otherAxiom)));
        assertThat(index.getAxioms(indA, ontologyId)
                        .count(), is(0L));
    }
}
