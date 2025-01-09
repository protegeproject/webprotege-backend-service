package edu.stanford.protege.webprotege.index.impl;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.change.AddAxiomChange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntityProvider;
import org.semanticweb.owlapi.model.OWLOntologyID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Class;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.SubClassOf;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-09-16
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AxiomsByEntityReferenceIndexImpl_ContainsEntityInOntologyAxiomsSignature_TestCase {

    private AxiomsByEntityReferenceIndexImpl impl;

    @Mock
    private OWLEntityProvider entityProvider;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private IRI subClsIri;

    @Mock
    private IRI superClsIri;

    private OWLClass subCls;

    private OWLClass superCls;

    @BeforeEach
    public void setUp() {
        subCls = Class(subClsIri);
        superCls = Class(superClsIri);
        var axiom = SubClassOf(subCls, superCls);
        impl = new AxiomsByEntityReferenceIndexImpl(entityProvider);
        impl.applyChanges(ImmutableList.of(AddAxiomChange.of(ontologyId, axiom)));
    }

    @Test
    public void shouldContainSubClass() {
        assertThat(impl.containsEntityInOntologyAxiomsSignature(subCls, ontologyId), is(true));
    }

    @Test
    public void shouldContainSuperClass() {
        assertThat(impl.containsEntityInOntologyAxiomsSignature(superCls, ontologyId), is(true));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNpeIfEntityIsNull() {
    assertThrows(NullPointerException.class, () -> { 
        impl.containsEntityInOntologyAxiomsSignature(null, ontologyId);
     });
}

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNpeIfOntologyIdIsNull() {
    assertThrows(NullPointerException.class, () -> { 
        impl.containsEntityInOntologyAxiomsSignature(subCls, null);
     });
}

    @Test
    public void shouldReturnFalseForUnknownEntity() {
        assertThat(impl.containsEntityInOntologyAxiomsSignature(Class(mock(IRI.class)), ontologyId), is(false));
    }

    @Test
    public void shouldReturnFalseForUnknowOntology() {
        assertThat(impl.containsEntityInOntologyAxiomsSignature(subCls, mock(OWLOntologyID.class)), is(false));
    }
}
