package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.index.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.model.*;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-22
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ObjectPropertyHierarchyProviderImpl_TestCase {

    private ObjectPropertyHierarchyProviderImpl provider;

    private ProjectId projectId = ProjectId.generate();

    @Mock
    private ProjectOntologiesIndex projectOntologiesIndex;

    @Mock
    private SubObjectPropertyAxiomsBySubPropertyIndex subPropertyAxiomsIndex;

    @Mock
    private EntitiesInProjectSignatureIndex entitiesInSignature;

    @Mock
    private OWLOntologyID ontologyId;

    private OWLDataFactory dataFactory = new OWLDataFactoryImpl();

    private OWLObjectProperty propertyA = dataFactory.getOWLObjectProperty(mockIri());

    private OWLObjectProperty propertyB = dataFactory.getOWLObjectProperty(mockIri());

    private OWLObjectProperty propertyC = dataFactory.getOWLObjectProperty(mockIri());

    private OWLObjectProperty propertyD = dataFactory.getOWLObjectProperty(mockIri());

    @Mock
    private AxiomsByTypeIndex axiomsByTypeIndex;

    @Mock
    private OntologySignatureByTypeIndex ontologySignatureByTypeIndex;


    @BeforeEach
    public void setUp() {

        when(projectOntologiesIndex.getOntologyIds())
                .thenAnswer(invocation -> Stream.of(ontologyId));

        var propertyASubPropertyOfB = dataFactory.getOWLSubObjectPropertyOfAxiom(propertyA, propertyB,
                                                                          noAnnotations());

        var propertyBSubPropertyOfC = dataFactory.getOWLSubObjectPropertyOfAxiom(propertyB, propertyC,
                                                                          noAnnotations());


        when(subPropertyAxiomsIndex.getSubPropertyOfAxioms(any(), any()))
                .thenAnswer(invocation -> Stream.empty());

        when(subPropertyAxiomsIndex.getSubPropertyOfAxioms(propertyA, ontologyId))
                .thenAnswer(invocation -> Stream.of(propertyASubPropertyOfB));

        when(subPropertyAxiomsIndex.getSubPropertyOfAxioms(propertyB, ontologyId))
                .thenAnswer(invocation -> Stream.of(propertyBSubPropertyOfC));

        when(entitiesInSignature.containsEntityInSignature(propertyC))
                .thenReturn(true);
        when(entitiesInSignature.containsEntityInSignature(propertyD))
                .thenReturn(true);

        when(ontologySignatureByTypeIndex.getSignature(EntityType.OBJECT_PROPERTY, ontologyId))
                .thenAnswer(invocation -> Stream.of(propertyA, propertyB, propertyC, propertyD));

        when(axiomsByTypeIndex.getAxiomsByType(AxiomType.SUB_OBJECT_PROPERTY, ontologyId))
                .thenAnswer(invocation -> Stream.of(propertyASubPropertyOfB, propertyBSubPropertyOfC));

        provider = new ObjectPropertyHierarchyProviderImpl(projectId,
                                                           dataFactory.getOWLTopObjectProperty(),
                                                           entitiesInSignature,
                                                           projectOntologiesIndex,
                                                           ontologySignatureByTypeIndex,
                                                           subPropertyAxiomsIndex,
                                                           axiomsByTypeIndex);
    }

    private static Set<OWLAnnotation> noAnnotations() {
        return Collections.emptySet();
    }

    @Test
    public void shouldGetParents() {
        var parents = provider.getParents(propertyA);
        assertThat(parents, contains(propertyB));
    }

    @Test
    public void shouldGetAncestors() {
        var ancestors = provider.getAncestors(propertyA);
        assertThat(ancestors, containsInAnyOrder(propertyB, propertyC));
    }

    @Test
    public void shouldGetChidlren() {
        var children = provider.getChildren(propertyC);
        assertThat(children, contains(propertyB));
    }

    @Test
    public void shouldGetDescendants() {
        var descendants = provider.getDescendants(propertyC);
        assertThat(descendants, containsInAnyOrder(propertyA, propertyB));
    }

    @Test
    public void shouldGetRoots() {
        var roots = provider.getRoots();
        assertThat(roots, hasItems(dataFactory.getOWLTopObjectProperty()));
    }


    private static IRI mockIri() {
        return mock(IRI.class);
    }

}
