package edu.stanford.protege.webprotege.entity;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.hierarchy.ClassHierarchyProvider;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.model.IRI;
import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GetIcatxEntityTypeActionHandlerTest {


    @Mock
    private ClassHierarchyProvider classHierarchyProvider;


    @Mock
    private AccessManager accessManager;

    @Mock
    private IcatxEntityTypeConfigurationRepository repository;

    @InjectMocks
    private GetIcatxEntityTypeActionHandler actionHandler;

    @BeforeEach
    public void setUp(){
        when(repository.getAllConfigurations()).thenReturn(Arrays.asList(
                new IcatxEntityTypeConfiguration(IRI.create("http://who.int/icd#ICDCategory"),"","ICD"),
                new IcatxEntityTypeConfiguration(IRI.create("http://id.who.int/icd/entity/979408586"),"ICD","ICD_EXTENSION"),
                new IcatxEntityTypeConfiguration(IRI.create("http://id.who.int/icd/entity/423829389"),"ICF","ICF_EXTENSION")));
    }



    @Test
    public void GIVEN_entityWithSingleAncestor_WHEN_fetchingConfig_THEN_SingleAncestorTypeIsReturned(){
        when(classHierarchyProvider.getAncestors(any())).thenReturn(Arrays.asList(new OWLClassImpl(IRI.create("http://who.int/icd#ICDCategory"))));

        GetIcatxEntityTypeResult result = actionHandler.execute(new GetIcatxEntityTypeAction(ProjectId.generate(), IRI.create("http://id.who.int/icd/entity/555555")), new ExecutionContext());

        assertNotNull(result);
        assertEquals(1, result.icatxEntityTypes().size());
        assertEquals("ICD", result.icatxEntityTypes().get(0));
    }

    @Test
    public void GIVEN_entityWithMultipleAncestors_WHEN_fetchingConfig_THEN_bothAncestorTypesAreReturned(){
        when(classHierarchyProvider.getAncestors(any())).thenReturn(Arrays.asList(new OWLClassImpl(IRI.create("http://who.int/icd#ICDCategory")),
                new OWLClassImpl(IRI.create("http://id.who.int/icd/entity/423829389"))));
        GetIcatxEntityTypeResult result = actionHandler.execute(new GetIcatxEntityTypeAction(ProjectId.generate(), IRI.create("http://id.who.int/icd/entity/555555")), new ExecutionContext());

        assertNotNull(result);
        assertEquals(2, result.icatxEntityTypes().size());
        assertTrue(result.icatxEntityTypes().contains("ICD"));
        assertTrue(result.icatxEntityTypes().contains("ICF_EXTENSION"));
    }

    @Test
    public void GIVEN_entityWithConflictingAncestors_WHEN_fetchConfig_THEN_conflictingAncestorsAreEliminated(){
        when(classHierarchyProvider.getAncestors(any())).thenReturn(Arrays.asList(new OWLClassImpl(IRI.create("http://who.int/icd#ICDCategory")),
                new OWLClassImpl(IRI.create("http://id.who.int/icd/entity/979408586"))));

        GetIcatxEntityTypeResult result = actionHandler.execute(new GetIcatxEntityTypeAction(ProjectId.generate(), IRI.create("http://id.who.int/icd/entity/555555")), new ExecutionContext());

        assertNotNull(result);
        assertEquals(1, result.icatxEntityTypes().size());
        assertTrue(result.icatxEntityTypes().contains("ICD_EXTENSION"));

    }


}
