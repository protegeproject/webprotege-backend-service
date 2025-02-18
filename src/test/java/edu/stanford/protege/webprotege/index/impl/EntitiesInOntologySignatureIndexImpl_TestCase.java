package edu.stanford.protege.webprotege.index.impl;

import edu.stanford.protege.webprotege.index.OntologyAnnotationsSignatureIndex;
import edu.stanford.protege.webprotege.index.OntologyAxiomsSignatureIndex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-19
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class EntitiesInOntologySignatureIndexImpl_TestCase {

    private EntitiesInOntologySignatureIndexImpl impl;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLEntity entity;

    @Mock
    private OntologyAxiomsSignatureIndex ontologyAxiomsSignatureIndex;

    @Mock
    private OntologyAnnotationsSignatureIndex ontologyAnnotationsSignatureIndex;

    @BeforeEach
    public void setUp() {
        impl = new EntitiesInOntologySignatureIndexImpl(ontologyAxiomsSignatureIndex, ontologyAnnotationsSignatureIndex);
        when(ontologyAxiomsSignatureIndex.containsEntityInOntologyAxiomsSignature(entity, ontologyId))
                .thenReturn(true);
    }

    @Test
    public void shouldContainEntityInSignature() {
        var contains = impl.containsEntityInSignature(entity, ontologyId);
        assertThat(contains, is(true));
    }

    @Test
    public void shouldNotContainUnknownEntityInSignature() {
        var contains = impl.containsEntityInSignature(mock(OWLEntity.class), ontologyId);
        assertThat(contains, is(false));
    }

    @Test
    public void shouldNotContainEntityInUnknownOntologySignature() {
        var contains = impl.containsEntityInSignature(entity, mock(OWLOntologyID.class));
        assertThat(contains, is(false));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNpeIfOntologyIdIsNull() {
    assertThrows(NullPointerException.class, () -> { 
        impl.containsEntityInSignature(entity, null);
     });
}

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNpeIfEntityIsNull() {
    assertThrows(NullPointerException.class, () -> { 
        impl.containsEntityInSignature(null, ontologyId);
     });
}
}
