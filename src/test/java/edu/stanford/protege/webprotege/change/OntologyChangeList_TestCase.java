package edu.stanford.protege.webprotege.change;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.mockito.Mock;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-28
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class OntologyChangeList_TestCase<R> {


    private OntologyChangeList.Builder<R> changeListBuilder;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLAxiom axiom;

    @Mock
    private R subject;

    @Mock
    private OntologyChange ontologyChange;

    @BeforeEach
    public void setUp() {
        changeListBuilder = OntologyChangeList.builder();
    }

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNpeIfResultIsNull() {
    assertThrows(NullPointerException.class, () -> { 
        changeListBuilder.build(null);
     });
}

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNpeIfAddOntologyIdIsNull() {
    assertThrows(NullPointerException.class, () -> { 
        changeListBuilder.addAxiom(null, axiom);
     });
}

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNpeIfAddAxiomIdIsNull() {
    assertThrows(NullPointerException.class, () -> { 
        changeListBuilder.addAxiom(ontologyId, null);
     });
}

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNpeIfRemoveOntologyIdIsNull() {
    assertThrows(NullPointerException.class, () -> { 
        changeListBuilder.removeAxiom(null, axiom);
     });
}

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNpeIfRemoveAxiomIdIsNull() {
    assertThrows(NullPointerException.class, () -> { 
        changeListBuilder.removeAxiom(ontologyId, null);
     });
}

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNpeIfChangeIsNull() {
    assertThrows(NullPointerException.class, () -> { 
        changeListBuilder.add(null);
     });
}

    @Test
    public void shouldBuildEmptyChangeList() {
        var changeList = changeListBuilder.build(subject);
        assertThat(changeList.getChanges(), is(empty()));
    }

    @Test
    public void shouldBuildChangeListWithSuppliedResult() {
        var changeList = changeListBuilder.build(subject);
        assertThat(changeList.getResult(), is(subject));
    }

    @Test
    public void shouldBuildAddAxiom() {
        changeListBuilder.addAxiom(ontologyId, axiom);
        assertChangeListContains(AddAxiomChange.of(ontologyId, axiom));
    }
    
    @Test
    public void shouldBuildRemoveAxiom() {
        changeListBuilder.removeAxiom(ontologyId, axiom);
        assertChangeListContains(RemoveAxiomChange.of(ontologyId, axiom));
    }

    @Test
    public void shouldBuildWithChange() {
        changeListBuilder.add(ontologyChange);
        assertChangeListContains(ontologyChange);
    }

    private void assertChangeListContains(OntologyChange change) {
        var changeList = changeListBuilder.build(subject);
        assertThat(changeList.getChanges(), hasItem(change));
    }

    @Test
    public void shouldBeNonEmpty() {
        changeListBuilder.add(ontologyChange);
        assertThat(changeListBuilder.isEmpty(), is(false));
    }
    @Test
    public void shouldBeEmpty() {
        assertThat(changeListBuilder.isEmpty(), is(true));
    }
}
