package edu.stanford.protege.webprotege.crud.icatx;


import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.change.OntologyChange;
import edu.stanford.protege.webprotege.change.OntologyChangeList;
import edu.stanford.protege.webprotege.common.AnnotationAssertionDictionaryLanguage;
import edu.stanford.protege.webprotege.crud.*;
import edu.stanford.protege.webprotege.ipc.CommandExecutor;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static edu.stanford.protege.webprotege.OWLDeclarationAxiomMatcher.declarationFor;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class IcatxSuffixEntityCrudKitHandlerTestCase {

    public static final String DEFAULT_PREFIX = "http://stuff/";

    public static final String MATCHES_RULE_PREFIX = "http://this-mathed-the-rule/";

    @Mock
    private EntityIriPrefixResolver entityIriPrefixResolver;

    @Mock
    private IcatxSuffixSettings suffixSettings;

    @Mock
    private EntityCrudKitPrefixSettings prefixSettings;

    @Mock
    protected ChangeSetEntityCrudSession session;

    @Mock
    private EntityShortForm entityShortForm;

    private OWLDataFactory dataFactory = new OWLDataFactoryImpl();

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    protected OntologyChangeList.Builder<OWLClass> builder;

    @Mock
    private CommandExecutor<GetUniqueIdRequest, GetUniqueIdResponse> uniqueIdExecutor;

    @Mock
    protected EntityCrudContext crudContext;

    private IcatxSuffixEntityCrudKitHandler handler;
    protected AnnotationAssertionDictionaryLanguage dictionaryLanguage;
    protected IRI annotationPropertyIri = OWLRDFVocabulary.RDFS_LABEL.getIRI();

    @Before
    public void setUp(){
        dictionaryLanguage = AnnotationAssertionDictionaryLanguage.get(annotationPropertyIri, "en");
        when(crudContext.getTargetOntologyId()).thenReturn(ontologyId);
        when(crudContext.getDictionaryLanguage()).thenReturn(dictionaryLanguage);

        handler = new IcatxSuffixEntityCrudKitHandler(suffixSettings, prefixSettings, uniqueIdExecutor, dataFactory,entityIriPrefixResolver);
    }

    @Test
    public void GIVEN_newEntity_WHEN_create_THEN_entityAxiomShouldBeDeclared() {
        when(entityIriPrefixResolver.getIriPrefix(eq(prefixSettings), eq(EntityType.CLASS), any()))
                .thenReturn(DEFAULT_PREFIX);
        when(entityShortForm.getShortForm()).thenReturn("A");
        OWLClass cls = handler.create(session, EntityType.CLASS, entityShortForm, Optional.of("en"), ImmutableList.of(),
                crudContext,
                builder);
        var ontologyChangeCaptor = ArgumentCaptor.forClass(OntologyChange.class);
        verify(builder, atLeast(1)).add(ontologyChangeCaptor.capture());
        var addedAxioms = ontologyChangeCaptor.getAllValues()
                .stream()
                .map(OntologyChange::getAxiomOrThrow)
                .filter(ax -> ax instanceof OWLDeclarationAxiom)
                .map(ax -> (OWLDeclarationAxiom) ax)
                .collect(Collectors.toList());
        assertThat(addedAxioms, hasItem(Matchers.is(declarationFor(cls))));
    }

    @Test
    public void GIVEN_entityOutOfRules_WHEN_create_THEN_defaultPrefixShouldBeUsed() {
        when(prefixSettings.getIRIPrefix()).thenReturn(DEFAULT_PREFIX);
        when(entityIriPrefixResolver.getIriPrefix(eq(prefixSettings), eq(EntityType.CLASS), any()))
                .thenReturn(DEFAULT_PREFIX);
        when(entityShortForm.getShortForm()).thenReturn("A");
        OWLClass cls = handler.create(session, EntityType.CLASS, entityShortForm, Optional.of("en"), ImmutableList.of(),
                crudContext,
                builder);
        String[] parts = cls.getIRI().toString().split("/");
        String iriEnding = parts[parts.length - 1];
        assertTrue(isValidUUID(iriEnding));
    }

    @Test
    public void GIVEN_icatxEntity_WHEN_create_THEN_prefixSetByRulesShouldBeUsed() {
        when(prefixSettings.getIRIPrefix()).thenReturn(DEFAULT_PREFIX);
        when(entityIriPrefixResolver.getIriPrefix(eq(prefixSettings), eq(EntityType.CLASS), any()))
                .thenReturn(MATCHES_RULE_PREFIX);
        when(entityShortForm.getShortForm()).thenReturn("A");
        GetUniqueIdRequest request = new GetUniqueIdRequest(MATCHES_RULE_PREFIX);
        when(uniqueIdExecutor.execute(eq(request), any())).thenReturn(CompletableFuture.supplyAsync(() -> new GetUniqueIdResponse(MATCHES_RULE_PREFIX + "22323")));
        OWLClass cls = handler.create(session, EntityType.CLASS, entityShortForm, Optional.of("en"), ImmutableList.of(),
                crudContext,
                builder);
        assertEquals(MATCHES_RULE_PREFIX + "22323", cls.getIRI().toString());
    }

    public static boolean isValidUUID(String str) {
        try {
            UUID.fromString(str);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
