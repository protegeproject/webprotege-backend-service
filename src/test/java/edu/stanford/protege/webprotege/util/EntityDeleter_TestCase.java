package edu.stanford.protege.webprotege.util;

import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.change.RemoveAxiomChange;
import edu.stanford.protege.webprotege.change.RemoveOntologyAnnotationChange;
import edu.stanford.protege.webprotege.index.ProjectOntologiesIndex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.util.Collections;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-08
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class EntityDeleter_TestCase {

    private EntityDeleter entityDeleter;

    @Mock
    private ReferenceFinder referenceFinder;

    @Mock
    private ProjectOntologiesIndex projectOntologiesIndex;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLEntity entity;

    @Mock
    private ReferenceFinder.ReferenceSet referenceSet;

    @Mock
    private OWLAxiom axiom;

    @Mock
    private OWLAnnotation ontologyAnnotation;

    @BeforeEach
    public void setUp() {
        when(projectOntologiesIndex.getOntologyIds())
                .thenReturn(Stream.of(ontologyId));

        when(referenceFinder.getReferenceSet(Collections.singleton(entity), ontologyId))
                .thenReturn(referenceSet);

        when(referenceSet.getOntologyId())
                .thenReturn(ontologyId);
        when(referenceSet.getReferencingAxioms())
                .thenReturn(ImmutableSet.of(axiom));
        when(referenceSet.getReferencingOntologyAnnotations())
                .thenReturn(ImmutableSet.of(ontologyAnnotation));

        entityDeleter = new EntityDeleter(referenceFinder,
                                          projectOntologiesIndex);


    }

    @Test
    public void shouldGetOntologyChangesToDeleteEntity() {
        var changes = entityDeleter.getChangesToDeleteEntities(Collections.singleton(entity));
        assertThat(changes, hasItems(RemoveAxiomChange.of(ontologyId, axiom),
                                     RemoveOntologyAnnotationChange.of(ontologyId, ontologyAnnotation)));
    }

    @Test
    public void shouldGetEmptyChangesForEmptyEntitiesSet() {
        var changes = entityDeleter.getChangesToDeleteEntities(Collections.emptySet());
        assertThat(changes.isEmpty(), is(true));
    }

    @Test
public void shouldThrowNpeForNullEntitiesSet() {
    assertThrows(NullPointerException.class, () -> { 
        entityDeleter.getChangesToDeleteEntities(null);
     });
}
}
