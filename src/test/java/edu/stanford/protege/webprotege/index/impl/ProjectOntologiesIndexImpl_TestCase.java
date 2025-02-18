package edu.stanford.protege.webprotege.index.impl;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.change.AddAxiomChange;
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

import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-06
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ProjectOntologiesIndexImpl_TestCase {

    @Mock
    private OWLOntologyID rootOntologyId;

    private ProjectOntologiesIndexImpl impl;

    @Mock
    private OWLAxiom axiom;

    @BeforeEach
    public void setUp() {
        impl = new ProjectOntologiesIndexImpl();
        impl.applyChanges(ImmutableList.of(AddAxiomChange.of(rootOntologyId, axiom)));
    }

    @Test
    public void shouldReturnStreamOfReferencedOntologyId() {
        var ontologyIdStream = impl.getOntologyIds();
        var ontologyIds = ontologyIdStream.collect(Collectors.toSet());
        assertThat(ontologyIds, contains(rootOntologyId));
    }

    @Test
    public void shouldOnlyContainReferencedOntologies() {
        impl.applyChanges(ImmutableList.of(RemoveAxiomChange.of(rootOntologyId, axiom)));
        assertThat(impl.getOntologyIds().count(), is(0L));
    }

    @Test
    public void shouldHandleMultiple() {
        impl.applyChanges(ImmutableList.of(AddAxiomChange.of(rootOntologyId, axiom)));
        impl.applyChanges(ImmutableList.of(RemoveAxiomChange.of(rootOntologyId, axiom)));
        assertThat(impl.getOntologyIds().count(), is(1L));
    }
}
