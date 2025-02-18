package edu.stanford.protege.webprotege.index.impl;

import edu.stanford.protege.webprotege.index.EntitiesInOntologySignatureIndex;
import edu.stanford.protege.webprotege.index.ProjectOntologiesIndex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.mockito.Mock;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-17
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class EntitiesInProjectSignatureIndexImpl_TestCase {

    private EntitiesInProjectSignatureIndexImpl impl;

    @Mock
    private ProjectOntologiesIndex projectOntologiesIndex;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLEntity entity;

    @Mock
    private EntitiesInOntologySignatureIndex entitiesInOntologySignatureIndex;

    @BeforeEach
    public void setUp() {
        impl = new EntitiesInProjectSignatureIndexImpl(projectOntologiesIndex,
                                                       entitiesInOntologySignatureIndex);
        when(projectOntologiesIndex.getOntologyIds())
                .thenReturn(Stream.of(ontologyId));
        when(entitiesInOntologySignatureIndex.containsEntityInSignature(entity, ontologyId))
                .thenReturn(true);
    }

    @Test
    public void shouldGetDependencies() {
        assertThat(impl.getDependencies(), containsInAnyOrder(projectOntologiesIndex, entitiesInOntologySignatureIndex));
    }

    @Test
    public void shouldContainEntityInSignature() {
        var contains = impl.containsEntityInSignature(entity);
        assertThat(contains, is(true));
    }

    @Test
    public void shouldNotContainUnknownEntityInSignature() {
        var contains = impl.containsEntityInSignature(mock(OWLEntity.class));
        assertThat(contains, is(false));
    }

    @Test
    public void shouldReturnFalseForEmptySetOfProjectOntologies() {
        when(projectOntologiesIndex.getOntologyIds())
                .thenReturn(Stream.empty());
        var contains = impl.containsEntityInSignature(entity);
        assertThat(contains, is(false));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNpeIfEntityIsNull() {
    assertThrows(NullPointerException.class, () -> { 
        impl.containsEntityInSignature(null);
     });
}

}
