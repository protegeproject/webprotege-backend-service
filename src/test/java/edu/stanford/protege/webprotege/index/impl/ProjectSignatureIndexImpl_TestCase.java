package edu.stanford.protege.webprotege.index.impl;

import edu.stanford.protege.webprotege.index.OntologySignatureIndex;
import edu.stanford.protege.webprotege.index.ProjectOntologiesIndex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-15
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ProjectSignatureIndexImpl_TestCase {

    private ProjectSignatureIndexImpl impl;

    @Mock
    private ProjectOntologiesIndex projectOntologiesIndex;

    @Mock
    private OntologySignatureIndex ontologySignatureIndex;

    @Mock
    private OWLOntologyID ontologyIdA, ontologyIdB;

    @Mock
    private OWLEntity entityA, entityB;

    @BeforeEach
    public void setUp() {
        impl = new ProjectSignatureIndexImpl(projectOntologiesIndex,
                                             ontologySignatureIndex);

        when(projectOntologiesIndex.getOntologyIds())
                .thenReturn(Stream.of(ontologyIdA, ontologyIdB));

        when(ontologySignatureIndex.getEntitiesInSignature(ontologyIdA))
                .thenReturn(Stream.of(entityA));
        when(ontologySignatureIndex.getEntitiesInSignature(ontologyIdB))
                .thenReturn(Stream.of(entityB));
    }

    @Test
    public void shouldGetDependencies() {
        assertThat(impl.getDependencies(), containsInAnyOrder(projectOntologiesIndex, ontologySignatureIndex));
    }

    @Test
    public void shouldGetSignatureInProjectOntologies() {
        var signature = impl.getSignature().collect(Collectors.toSet());
        assertThat(signature, containsInAnyOrder(entityA, entityB));
    }
}
