package edu.stanford.protege.webprotege.merge;

import edu.stanford.protege.webprotege.index.OntologyAnnotationsIndex;
import edu.stanford.protege.webprotege.index.OntologyAxiomsIndex;
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
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-21
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ProjectOntologiesBuilder_TestCase {

    private ProjectOntologiesBuilder builder;

    @Mock
    private ProjectOntologiesIndex projectOntologiesIndex;

    @Mock
    private OntologyAnnotationsIndex ontologyAnnotationsIndex;

    @Mock
    private OntologyAxiomsIndex ontologyAxiomsIndex;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLAnnotation annotation;

    @Mock
    private OWLAxiom axiom;


    @BeforeEach
    public void setUp() {
        builder = new ProjectOntologiesBuilder(projectOntologiesIndex,
                                               ontologyAnnotationsIndex,
                                               ontologyAxiomsIndex);
        when(projectOntologiesIndex.getOntologyIds())
                .thenReturn(Stream.of(ontologyId));

        when(ontologyAnnotationsIndex.getOntologyAnnotations(ontologyId))
                .thenReturn(Stream.of(annotation));

        when(ontologyAxiomsIndex.getAxioms(ontologyId))
                .thenReturn(Stream.of(axiom));
    }

    @Test
    public void shouldBuildCollectionOfProjectOntologies() {
        var ontologies = builder.buildProjectOntologies();
        assertThat(ontologies.size(), is(1));
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    public void shouldBuildOntologyWithOntologyId() {
        var ontologies = builder.buildProjectOntologies();
        var ontology = ontologies.stream().findFirst().get();
        assertThat(ontology.getOntologyId(), is(ontologyId));
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    public void shouldBuildOntologyWithAnnotations() {
        var ontologies = builder.buildProjectOntologies();
        var ontology = ontologies.stream().findFirst().get();
        assertThat(ontology.getAnnotations(), contains(annotation));
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    public void shouldBuildOntologyWithAxioms() {
        var ontologies = builder.buildProjectOntologies();
        var ontology = ontologies.stream().findFirst().get();
        assertThat(ontology.getAxioms(), contains(axiom));
    }
}
