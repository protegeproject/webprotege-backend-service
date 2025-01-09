package edu.stanford.protege.webprotege.crud.supplied;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.change.OntologyChange;
import edu.stanford.protege.webprotege.change.OntologyChangeList;
import edu.stanford.protege.webprotege.common.DictionaryLanguage;
import edu.stanford.protege.webprotege.crud.*;
import edu.stanford.protege.webprotege.crud.gen.GeneratedAnnotationsSettings;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.vocab.Namespaces;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static edu.stanford.protege.webprotege.OWLDeclarationAxiomMatcher.declarationFor;
import static edu.stanford.protege.webprotege.OWLEntityMatcher.owlThing;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.*;


/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 16/04/2014
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class SuppliedNameSuffixEntityCrudKitHandlerTestCase {

    public static final String PREFIX = "http://stuff/";
    @Mock
    protected EntityCrudKitPrefixSettings prefixSettings;

    @Mock
    protected SuppliedNameSuffixSettings suffixSettings;

    @Mock
    protected EntityCrudContext crudContext;

    @Mock
    protected EntityShortForm entityShortForm;

    @Mock
    protected OWLOntology ontology;

    @Mock
    protected OntologyChangeList.Builder<OWLClass> builder;

    @Mock
    protected ChangeSetEntityCrudSession session;

    protected WhiteSpaceTreatment whiteSpaceTreatment = WhiteSpaceTreatment.TRANSFORM_TO_CAMEL_CASE;

    private SuppliedNameSuffixEntityCrudKitHandler handler;

    @Mock
    private DictionaryLanguage dictionaryLanguage;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private EntityIriPrefixResolver entityIriPrefixResolver;

    private GeneratedAnnotationsSettings generatedAnnotationsDescriptor = GeneratedAnnotationsSettings.empty();

    private EntityType<?> entityType = EntityType.CLASS;

    @BeforeEach
    public void setUp() throws Exception {
        OWLDataFactoryImpl dataFactory = new OWLDataFactoryImpl();
        when(suffixSettings.getWhiteSpaceTreatment()).thenReturn(whiteSpaceTreatment);
        when(crudContext.getTargetOntologyId()).thenReturn(ontologyId);
        when(crudContext.getPrefixedNameExpander()).thenReturn(PrefixedNameExpander.builder().withNamespaces(Namespaces.values()).build());
        when(crudContext.getDictionaryLanguage()).thenReturn(dictionaryLanguage);
        when(dictionaryLanguage.getLang()).thenReturn("");
        when(entityIriPrefixResolver.getIriPrefix(prefixSettings, entityType, ImmutableList.of())).thenReturn(PREFIX);
        handler = new SuppliedNameSuffixEntityCrudKitHandler(prefixSettings, suffixSettings,
                                                             generatedAnnotationsDescriptor,
                                                             dataFactory,
                                                             entityIriPrefixResolver);
    }

    @Test
    public void shouldAddDeclaration() {
        when(entityShortForm.getShortForm()).thenReturn("A");
        OWLClass cls = handler.create(session, EntityType.CLASS, entityShortForm, Optional.empty(),
                                      ImmutableList.of(),
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
        assertThat(addedAxioms, hasItem(is(declarationFor(cls))));
    }

    @Test
    public void shouldAddLabelEqualToSuppliedName() {
        String suppliedName = "MyLabel";
        when(entityShortForm.getShortForm()).thenReturn(suppliedName);
        handler.create(session, EntityType.CLASS, entityShortForm, Optional.of("en"), ImmutableList.of(), crudContext, builder);
        verifyHasLabelEqualTo(suppliedName, "en");
    }

    @Test
    public void shouldCreatedExpandedPrefixName() {
        when(entityShortForm.getShortForm()).thenReturn("owl:Thing");
        OWLClass cls = handler.create(session, EntityType.CLASS, entityShortForm, Optional.of("en"),
                ImmutableList.of(),
                                      crudContext,
                                      builder);
        assertThat(cls, is(owlThing()));
    }

    @Test
    public void shouldAddLabelEqualToLocalName() {
        String suppliedName = "owl:Thing";
        when(entityShortForm.getShortForm()).thenReturn(suppliedName);
        handler.create(session, EntityType.CLASS, entityShortForm, Optional.of("en"), ImmutableList.of(), crudContext, builder);
        verifyHasLabelEqualTo("Thing", "en");
    }

    @Test
    public void shouldCreateEntityWithAbsoluteIriIfSpecified() {
        String expectedIRI = "http://stuff.com/A";
        String shortForm = "<" + expectedIRI + ">";
        when(entityShortForm.getShortForm()).thenReturn(shortForm);
        OWLClass cls = handler.create(session, EntityType.CLASS, entityShortForm, Optional.of("en"),
                ImmutableList.of(),
                                      crudContext,
                                      builder);
        assertThat(cls.getIRI(), is(equalTo(IRI.create(expectedIRI))));
        verifyHasLabelEqualTo("A", "en");
    }


    private void verifyHasLabelEqualTo(String label, String lang) {
        var addAxiomCaptor = ArgumentCaptor.forClass(OntologyChange.class);
        verify(builder, atLeast(1)).add(addAxiomCaptor.capture());
        List<OWLAxiom> addedAxioms = addAxiomCaptor.getAllValues().stream()
                                                                      .map(OntologyChange::getAxiomOrThrow)
                .collect(Collectors.toList());
        var found = addedAxioms.stream()
                               .filter(ax -> ax instanceof OWLAnnotationAssertionAxiom)
                               .map(ax -> (OWLAnnotationAssertionAxiom) ax)
                               .filter(ax -> ax.getProperty().isLabel())
                               .map(OWLAnnotationAssertionAxiom::getValue)
                               .filter(OWLAnnotationValue::isLiteral)
                               .map(val -> (OWLLiteral) val)
                               .filter(lit -> lit.hasLang(lang))
                               .filter(lit -> lit.getLiteral().equals(label))
                               .findFirst();
        assertThat(found.isPresent(), Is.is(true));
    }
}
