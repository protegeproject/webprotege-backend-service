package edu.stanford.protege.webprotege.index.impl;

import edu.stanford.protege.webprotege.change.AddAxiomChange;
import edu.stanford.protege.webprotege.change.AxiomChange;
import edu.stanford.protege.webprotege.change.RemoveAxiomChange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.util.List;
import java.util.function.Consumer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-09-05
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AxiomChangeHandler_TestCase {

    private AxiomChangeHandler handler;

    @Mock
    private Consumer<AddAxiomChange> addAxiomChangeConsumer;

    @Mock
    private Consumer<RemoveAxiomChange> removeAxiomChangeConsumer;

    @Mock
    private Consumer<AxiomChange> axiomChangeConsumer;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLAxiom axiom;

    private AddAxiomChange addAxiomChange;

    private RemoveAxiomChange removeAxiomChange;

    @BeforeEach
    public void setUp() {
        addAxiomChange = AddAxiomChange.of(ontologyId, axiom);
        removeAxiomChange = RemoveAxiomChange.of(ontologyId, axiom);
        handler = new AxiomChangeHandler();
        handler.setAddAxiomChangeConsumer(addAxiomChangeConsumer);
        handler.setRemoveAxiomChangeConsumer(removeAxiomChangeConsumer);
        handler.setAxiomChangeConsumer(axiomChangeConsumer);
    }

    @Test
    public void shouldCallAxiomChangeConsumerOnAddAxiom() {
        handler.handleOntologyChanges(List.of(addAxiomChange));
        verify(axiomChangeConsumer, times(1)).accept(addAxiomChange);
    }

    @Test
    public void shouldCallAddAxiomChangeConsumerOnAddAxiom() {
        handler.handleOntologyChanges(List.of(addAxiomChange));
        verify(addAxiomChangeConsumer, times(1)).accept(addAxiomChange);
    }

    @Test
    public void shouldNotCallRemoveAxiomChangeConsumerOnAddAxiom() {
        handler.handleOntologyChanges(List.of(addAxiomChange));
        verify(removeAxiomChangeConsumer, never()).accept(any());
    }

    @Test
    public void shouldCallAxiomChangeConsumerOnRemoveAxiom() {
        handler.handleOntologyChanges(List.of(removeAxiomChange));
        verify(axiomChangeConsumer, times(1)).accept(any());
    }

    @Test
    public void shouldCallRemoveAxiomChangeConsumerOnRemoveAxiom() {
        handler.handleOntologyChanges(List.of(removeAxiomChange));
        verify(removeAxiomChangeConsumer, times(1)).accept(any());
    }

    @Test
    public void shouldNotCallAddAxiomChangeConsumerOnRemoveAxiom() {
        handler.handleOntologyChanges(List.of(removeAxiomChange));
        verify(addAxiomChangeConsumer, never()).accept(any());
    }
}
