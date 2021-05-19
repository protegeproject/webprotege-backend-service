package edu.stanford.protege.webprotege.inject.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mongodb.client.MongoDatabase;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import edu.stanford.protege.webprotege.axiom.AxiomComparatorImpl;
import edu.stanford.protege.webprotege.axiom.DefaultAxiomTypeOrdering;
import edu.stanford.protege.webprotege.change.HasApplyChanges;
import edu.stanford.protege.webprotege.change.*;
import edu.stanford.protege.webprotege.change.matcher.*;
import edu.stanford.protege.webprotege.crud.EntityCrudKitPlugin;
import edu.stanford.protege.webprotege.crud.obo.OBOIdSuffixEntityCrudKitPlugin;
import edu.stanford.protege.webprotege.crud.persistence.ProjectEntityCrudKitSettingsRepository;
import edu.stanford.protege.webprotege.crud.supplied.SuppliedNameSuffixEntityCrudKitPlugin;
import edu.stanford.protege.webprotege.crud.uuid.UuidEntityCrudKitPlugin;
import edu.stanford.protege.webprotege.events.*;
import edu.stanford.protege.webprotege.frame.*;
import edu.stanford.protege.webprotege.hierarchy.*;
import edu.stanford.protege.webprotege.index.*;
import edu.stanford.protege.webprotege.inject.DataDirectoryProvider;
import edu.stanford.protege.webprotege.inject.ProjectActionHandlersModule;
import edu.stanford.protege.webprotege.inject.ShortFormModule;
import edu.stanford.protege.webprotege.mansyntax.ShellOntologyChecker;
import edu.stanford.protege.webprotege.mansyntax.render.*;
import edu.stanford.protege.webprotege.match.*;
import edu.stanford.protege.webprotege.object.*;
import edu.stanford.protege.webprotege.obo.OBONamespaceCache;
import edu.stanford.protege.webprotege.obo.OBONamespaceCacheFactory;
import edu.stanford.protege.webprotege.obo.TermDefinitionManager;
import edu.stanford.protege.webprotege.obo.TermDefinitionManagerImpl;
import edu.stanford.protege.webprotege.owlapi.HasContainsEntityInSignatureImpl;
import edu.stanford.protege.webprotege.owlapi.StringFormatterLiteralRendererImpl;
import edu.stanford.protege.webprotege.project.ProjectDisposablesManager;
import edu.stanford.protege.webprotege.project.chg.ChangeManager;
import edu.stanford.protege.webprotege.renderer.LiteralRenderer;
import edu.stanford.protege.webprotege.renderer.*;
import edu.stanford.protege.webprotege.repository.ProjectEntitySearchFiltersManager;
import edu.stanford.protege.webprotege.revision.*;
import edu.stanford.protege.webprotege.search.EntitySearchFilterIndexesManager;
import edu.stanford.protege.webprotege.search.ProjectEntitySearchFiltersManagerImpl;
import edu.stanford.protege.webprotege.shortform.*;
import edu.stanford.protege.webprotege.tag.*;
import edu.stanford.protege.webprotege.util.DisposableObjectManager;
import edu.stanford.protege.webprotege.watches.WatchManager;
import edu.stanford.protege.webprotege.watches.WatchManagerImpl;
import edu.stanford.protege.webprotege.watches.WatchTriggeredHandler;
import edu.stanford.protege.webprotege.watches.WatchTriggeredHandlerImpl;
import edu.stanford.protege.webprotege.event.ProjectEvent;
import edu.stanford.protege.webprotege.frame.FrameComponentRenderer;
import edu.stanford.protege.webprotege.frame.PropertyValue;
import edu.stanford.protege.webprotege.inject.ProjectSingleton;
import edu.stanford.protege.webprotege.project.ProjectId;
import edu.stanford.protege.webprotege.renderer.HasHtmlBrowserText;
import edu.stanford.protege.webprotege.revision.RevisionNumber;
import org.semanticweb.owlapi.expression.OWLOntologyChecker;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.IRIShortFormProvider;
import org.semanticweb.owlapi.util.OntologyIRIShortFormProvider;
import org.semanticweb.owlapi.util.ShortFormProvider;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import javax.annotation.Nonnull;
import java.io.File;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static dagger.internal.codegen.DaggerStreams.toImmutableSet;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 25/03/2014
 *         <p>
 *         A  module for a project.  The module ensures that any object graph contains project specific objects for the
 *         specified project (e.g. root ontology, short form provider etc.)
 */
@Module(includes = {IndexModule.class, ShortFormModule.class, ProjectActionHandlersModule.class})
public class ProjectModule {

    private final ProjectId projectId;

    public ProjectModule(ProjectId projectId) {
        this.projectId = projectId;
    }

    @Provides
    @ProjectSingleton
    OWLDataFactory providesDataFactory() {
        return new OWLDataFactoryImpl();
    }

    @Provides
    @ProjectSingleton
    public OWLEntityProvider providesOWLEntityProvider(OWLDataFactory dataFactory) {
        return dataFactory;
    }

    @Provides
    @ProjectSingleton
    public OWLEntityByTypeProvider provideOwlEntityByTypeProvider(OWLDataFactory dataFactory) {
        return dataFactory;
    }

    @Provides
    @ProjectSingleton
    public ProjectId provideProjectId() {
        return projectId;
    }

    @Provides
    @RootOntologyDocument
    public File provideRootOntologyDocument(RootOntologyDocumentProvider provider) {
        return provider.get();
    }

    @Provides
    @ProjectDirectory
    public File provideProjectDirectory(ProjectDirectoryProvider provider) {
        return provider.get();
    }

    @Provides
    @ChangeHistoryFile
    public File provideChangeHistoryFile(ChangeHistoryFileProvider provider) {
        return provider.get();
    }

    @Provides
    @ProjectSpecificUiConfigurationDataDirectory
    public File provideProjectSpecificUiConfigurationDataDirectory(ProjectSpecificUiConfigurationDataDirectoryProvider provider) {
        return provider.get();
    }

    @Provides
    public OWLAnnotationPropertyProvider provideAnnotationPropertyProvider(OWLDataFactory factory) {
        return factory;
    }

    @Provides
    public Set<ChangeMatcher> providesChangeMatchers(
            AnnotationAssertionChangeMatcher annotationAssertionChangeMatcher,
            PropertyDomainAxiomChangeMatcher propertyDomainAxiomChangeMatcher,
            PropertyRangeAxiomChangeMatcher propertyRangeAxiomChangeMatcher,
            EditedAnnotationAssertionChangeMatcher editedAnnotationAssertionChangeMatcher,
            FunctionalDataPropertyAxiomChangeMatcher functionalDataPropertyAxiomChangeMatcher,
            ClassAssertionAxiomMatcher classAssertionAxiomMatcher,
            SubClassOfAxiomMatcher subClassOfAxiomMatcher,
            ClassMoveChangeMatcher classMoveChangeMatcher,
            SubClassOfEditChangeMatcher subClassOfEditChangeMatcher,
            PropertyAssertionAxiomMatcher propertyAssertionAxiomMatcher,
            SameIndividualAxiomChangeMatcher sameIndividualAxiomChangeMatcher,
            EntityCreationMatcher entityCreationMatcher,
            EntityDeletionMatcher entityDeletionMatcher) {
        ImmutableSet<Object> matchers = ImmutableSet.of(annotationAssertionChangeMatcher,
                                                        propertyDomainAxiomChangeMatcher,
                                                        propertyRangeAxiomChangeMatcher,
                                                        editedAnnotationAssertionChangeMatcher,
                                                        functionalDataPropertyAxiomChangeMatcher,
                                                        classAssertionAxiomMatcher,
                                                        subClassOfAxiomMatcher,
                                                        classMoveChangeMatcher,
                                                        subClassOfEditChangeMatcher,
                                                        propertyAssertionAxiomMatcher,
                                                        sameIndividualAxiomChangeMatcher,
                                                        entityCreationMatcher,
                                                        entityDeletionMatcher);
        return matchers.stream()
                       .map(m -> (ChangeMatcher) m)
                       .collect(toImmutableSet());
    }

    @Provides
    @ClassHierarchyRoot
    public OWLClass providesClassHierarchyRoot(ClassHierarchyRootProvider provider) {
        return provider.get();
    }

    @Provides
    @ObjectPropertyHierarchyRoot
    public OWLObjectProperty providesObjectPropertyHierarchyRoot(ObjectPropertyHierarchyRootProvider provider) {
        return provider.get();
    }

    @Provides
    @DataPropertyHierarchyRoot
    public OWLDataProperty providesDataPropertyHierarchyRoot(DataPropertyHierarchyRootProvider provider) {
        return provider.get();
    }

    @Provides
    public HierarchyProvider<OWLObjectProperty>
    provideHierarchyProviderOfObjectProperty(ObjectPropertyHierarchyProvider provider) {
        return provider;
    }


    @Provides
    public HierarchyProvider<OWLDataProperty>
    provideHierarchyProviderOfDataProperty(
            DataPropertyHierarchyProvider provider) {
        return provider;
    }

    @Provides
    DataPropertyHierarchyProvider provideDataPropertyHierarchyProvider(DataPropertyHierarchyProviderImpl impl) {
        return impl;
    }

    @Provides
    public HierarchyProvider<OWLAnnotationProperty>
    provideHierarchyProviderOfAnnotationProperty(
            AnnotationPropertyHierarchyProvider provider) {
        return provider;
    }

    @Provides
    AnnotationPropertyHierarchyProvider provideAnnotationPropertyHierarchyProvider(
            AnnotationPropertyHierarchyProviderImpl impl) {
        return impl;
    }

    @Provides
    public HasGetAncestors<OWLClass> providesOWLClassAncestors(HierarchyProvider<OWLClass> hierarchyProvider) {
        return hierarchyProvider;
    }

    @Provides
    public ClassHierarchyProvider getClassHierarchyProvider(ClassHierarchyProviderImpl impl) {
        return impl;
    }

    @Provides
    public HasGetAncestors<OWLObjectProperty> providesOWLObjectPropertyAncestors(HierarchyProvider<OWLObjectProperty> hierarchyProvider) {
        return hierarchyProvider;
    }

    @Provides
    public HasGetAncestors<OWLDataProperty> providesOWLDataPropertyAncestors(HierarchyProvider<OWLDataProperty> hierarchyProvider) {
        return hierarchyProvider;
    }

    @Provides
    public ObjectPropertyHierarchyProvider provideObjectPropertyHierarchyProvider(ObjectPropertyHierarchyProviderImpl impl) {
        return impl;
    }

    @Provides
    public HasGetAncestors<OWLAnnotationProperty> providesOWLAnnotationPropertyAncestors(HierarchyProvider<OWLAnnotationProperty> hierarchyProvider) {
        return hierarchyProvider;
    }

    @Provides
    public WatchManager provideWatchManager(WatchManagerImpl impl) {
        // Attach it so that it listens for entity frame changed events
        // There's no need to detatch it because it is project scoped
        // and has the same lifetime as a project event manager.
        impl.attach();
        return impl;
    }

    @Provides
    public WatchTriggeredHandler provideWatchTriggeredHandler(WatchTriggeredHandlerImpl impl) {
        return impl;
    }

    @ProjectSingleton
    @Provides
    public ShortFormProvider provideShortFormProvider(ShortFormAdapter shortFormAdapter) {
        return shortFormAdapter;
    }

    @Provides
    public IRIShortFormProvider provideIriShortFormProvider(IriShortFormAdapter adapter) {
        return adapter;
    }

    @Provides
    public HasGetRendering provideHasGetRendering(RenderingManager renderingManager) {
        return renderingManager;
    }

    @Provides
    public HasLang provideHasLang() {
        return () -> "en";
    }

    @Provides
    @ProjectSingleton
    public OntologyIRIShortFormProvider provideOntologyIRIShortFormProvider(WebProtegeOntologyIRIShortFormProvider provider) {
        return provider;
    }

    @Provides
    public EntityIRIChecker provideEntityIRIChecker(EntityIRICheckerImpl iriChecker) {
        return iriChecker;
    }

    @Provides
    public OWLOntologyChecker provideOntologyChecker(ShellOntologyChecker checker) {
        return checker;
    }

    @Provides
    public HighlightedEntityChecker provideHighlightedEntityChecker(NullHighlightedEntityChecker checker) {
        return checker;
    }

    @Provides
    @ProjectSingleton
    public EventManager<ProjectEvent<?>> providesEventManager(EventManagerProvider eventManagerProvider) {
        return eventManagerProvider.get();
    }

    @Provides
    public EventLifeTime provideEventLifeTime() {
        return EventManagerProvider.PROJECT_EVENT_LIFE_TIME;
    }

    @Provides
    public RevisionNumber provideRevisionNumber(RevisionNumberProvider provider) {
        return provider.get();
    }

    @Provides
    public HasGetChangeSubjects provideHasGetChangeSubjects(OntologyChangeSubjectProvider provider) {
        return provider;
    }

    @Provides
    public DeprecatedEntityChecker provideDeprecatedEntityChecker(DeprecatedEntityCheckerImpl checker) {
        return checker;
    }

    @Provides
    public HasGetRevisionSummary provideGetRevisionSummary(RevisionManagerImpl impl) {
        return impl;
    }

    @Provides
    @ProjectSingleton
    public RevisionManager provideRevisionManager(RevisionManagerImpl impl) {
        return impl;
    }

    @Provides
    @ProjectSingleton
    public RevisionStore provideRevisionStore(RevisionStoreProvider provider) {
        return provider.get();
    }

    @Provides
    ImmutableList<IRI> providesShortFormOrdering() {
        return DefaultShortFormAnnotationPropertyIRIs.asImmutableList();
    }

    @Provides
    @ProjectSingleton
    HasHtmlBrowserText providesHasHtmlBrowserText(RenderingManager rm) {
        return rm;
    }

    @Provides
    @ProjectSingleton
    OWLObjectRenderer providesOWLObjectRenderer(OWLObjectRendererImpl impl) {
        return impl;
    }

//    @Provides
//    @ProjectSingleton
//    HasImportsClosure providesHasImportsClosure(RootOntologyProvider provider) {
//        return provider.get();
//    }


    @Provides
    HasApplyChanges providesHasApplyChanges(ChangeManager manager) {
        return manager;
    }


    @Provides
    @ProjectSingleton
    HierarchyProvider<OWLClass> provideClassHierarchyProvider(ClassHierarchyProviderImpl provider) {
        return provider;
    }

    @Provides
    OWLObjectSelector<OWLClassExpression> provideClassExpressionSelector(OWLClassExpressionSelector selector) {
        return selector;
    }

    @Provides
    OWLObjectSelector<OWLObjectPropertyExpression> provideObjectPropertyExpressionSelector(
            OWLObjectPropertyExpressionSelector selector) {
        return selector;
    }

    @Provides
    OWLObjectSelector<OWLDataPropertyExpression> provideDataPropertyExpressionSelector(OWLDataPropertyExpressionSelector selector) {
        return selector;
    }

    @Provides
    OWLObjectSelector<OWLIndividual> provideIndividualSelector(OWLIndividualSelector selector) {
        return selector;
    }

    @Provides
    OWLObjectSelector<SWRLAtom> provideSWRLAtomSelector(SWRLAtomSelector selector) {
        return selector;
    }

    @Provides
    Comparator<? super OWLClass> providesClassComparator(OWLObjectComparatorImpl impl) {
        return impl;
    }

    @Provides
    Comparator<? super OWLObjectProperty> providesObjectPropertyComparator(OWLObjectComparatorImpl impl) {
        return impl;
    }

    @Provides
    Comparator<? super OWLDataProperty> providesDataPropertyComparator(OWLObjectComparatorImpl impl) {
        return impl;
    }

    @Provides
    Comparator<? super OWLAnnotationProperty> providesAnnotationPropertyComparator(AnnotationPropertyComparatorImpl impl) {
        return impl;
    }

    @Provides
    Comparator<? super OWLAnnotation> providesAnnotationComparator(OWLObjectComparatorImpl impl) {
        return impl;
    }

    @Provides
    Comparator<? super OWLNamedIndividual> providesNamedIndividualComparator(OWLObjectComparatorImpl impl) {
        return impl;
    }

    @Provides
    Comparator<? super OWLDatatype> providesDatatypeComparator(OWLObjectComparatorImpl impl) {
        return impl;
    }

    @Provides
    Comparator<? super SWRLAtom> providesSWRLAtomComparator(OWLObjectComparatorImpl impl) {
        return impl;
    }

    @Provides
    Comparator<OntologyChange> providesOWLOntologyChangeRecordComparator(OntologyChangeComparator comparator) {
        return comparator;
    }

    @Provides
    Comparator<PropertyValue> providePropertyValueComparator(PropertyValueComparator propertyValueComparator) {
        return propertyValueComparator;
    }

    @Provides
    Comparator<OWLAxiom> providesAxiomComparator(AxiomComparatorImpl impl) {
        return impl;
    }

    @Provides
    Comparator<OWLObject> providesOWLObjectComparator(OWLObjectComparatorImpl impl) {
        return impl;
    }

    @Provides
    public Set<EventTranslator> providesEventTranslators(
            BrowserTextChangedEventComputer c0,
            HighLevelEventGenerator c1,
            OWLClassHierarchyChangeComputer c2,
            OWLObjectPropertyHierarchyChangeComputer c3,
            OWLDataPropertyHierarchyChangeComputer c4,
            OWLAnnotationPropertyHierarchyChangeComputer c5,
            EntityDeprecatedChangedEventTranslator c6,
            EntityTagsChangedEventComputer c7) {
        return ImmutableSet.of(c0, c1, c2, c3, c4, c5, c6, c7);
    }

    @Provides
    HasPostEvents<ProjectEvent<?>> provideHasPostEvents(EventManager<ProjectEvent<?>> eventManager) {
        return eventManager;
    }

    @Provides
    NullHighlightedEntityChecker providesNullHighlightedEntityChecker() {
        return NullHighlightedEntityChecker.get();
    }

    @Provides
    HasContainsEntityInSignature providesHasContainsEntityInSignature(HasContainsEntityInSignatureImpl impl) {
        return impl;
    }

    @Provides
    List<AxiomType<?>> providesAxiomTypeList() {
        return DefaultAxiomTypeOrdering.get();
    }

    @Provides
    PropertyValueSubsumptionChecker providePropertyValueSubsumptionChecker(StructuralPropertyValueSubsumptionChecker impl) {
        return impl;
    }

    @Provides
    HasHasAncestor<OWLClass, OWLClass> provideClassClassHasAncestor(ClassClassAncestorChecker checker) {
        return checker;
    }

    @Provides
    HasHasAncestor<OWLObjectProperty, OWLObjectProperty> provideObjectPropertyObjectPropertyHasAncestor(
            ObjectPropertyObjectPropertyAncestorChecker checker) {
        return checker;
    }


    @Provides
    HasHasAncestor<OWLDataProperty, OWLDataProperty> provideDataPropertyDataPropertyHasAncestor(
            DataPropertyDataPropertyAncestorChecker checker) {
        return checker;
    }

    @Provides
    HasHasAncestor<OWLNamedIndividual, OWLClass> provideNamedIndividualClassHasAncestor(
            NamedIndividualClassAncestorChecker checker) {
        return checker;
    }

    @Provides
    OBONamespaceCache providesOboNamespaceCache(OBONamespaceCacheFactory factory) {
        var namespaceCache = factory.create();
        namespaceCache.rebuildNamespaceCache();
        return namespaceCache;
    }

    @Provides
    MatchingEngine provideMatchingEngine(MatchingEngineImpl impl) {
        return impl;
    }

    @Provides
    HierarchyPositionMatchingEngine provideHierarchyPositionMatchingEngine(HierarchyPositionMatchingEngineImpl impl) {
        return impl;
    }

    @Provides
    TagRepository provideTagRepository(TagRepositoryImpl impl) {
        impl.ensureIndexes();
        return impl;
    }

    @Provides
    TagRepositoryCachingImpl provideTagRepositoryCachingImpl(TagRepositoryImpl impl) {
        return new TagRepositoryCachingImpl(impl);
    }

    @Provides
    EntityTagsRepository provideEntityTagsRepository(EntityTagsRepositoryImpl impl) {
        impl.ensureIndexes();
        return impl;
    }

    @ProjectSingleton
    @Provides
    EntityTagsRepositoryCachingImpl provideEntityTagsRepositoryCachingImpl(EntityTagsRepositoryImpl impl) {
        EntityTagsRepositoryCachingImpl rep = new EntityTagsRepositoryCachingImpl(impl);
        rep.ensureIndexes();
        rep.preloadCache();
        return rep;
    }

    @ProjectSingleton
    @Provides
    ProjectDisposablesManager provideProjectDisposableObjectManager(DisposableObjectManager disposableObjectManager) {
        return new ProjectDisposablesManager(disposableObjectManager);
    }

    @Provides
    LiteralRenderer provideLiteralRenderer(@Nonnull
                                                   StringFormatterLiteralRendererImpl impl) {
        return impl;
    }

    @Provides
    LiteralLexicalFormTransformer provideLiteralLexicalFormTransformer() {
        return lexicalForm -> lexicalForm;
    }

    @Provides
    LiteralLangTagTransformer provideLangTagTransformer() {
        return langTag -> langTag;
    }

    @Provides
    @IntoSet
    public EntityCrudKitPlugin<?, ?, ?> provideUUIDPlugin(UuidEntityCrudKitPlugin plugin) {
        return plugin;
    }

    @Provides
    @IntoSet
    public EntityCrudKitPlugin<?, ?, ?> provideOBOIdPlugin(OBOIdSuffixEntityCrudKitPlugin plugin) {
        return plugin;
    }

    @Provides
    @IntoSet
    public EntityCrudKitPlugin<?, ?, ?> provideSuppliedNamePlugin(SuppliedNameSuffixEntityCrudKitPlugin plugin) {
        return plugin;
    }


    @Provides
    @ProjectSingleton
    public ProjectEntityCrudKitSettingsRepository provideProjectEntityCrudKitSettingsRepository(
            MongoDatabase database, ObjectMapper objectMapper) {
        return new ProjectEntityCrudKitSettingsRepository(database, objectMapper);
    }

    @Provides
    TermDefinitionManager provideTermDefinitionManager(TermDefinitionManagerImpl impl) {
        return impl;
    }

    @Provides
    AnnotationsSectionRenderer provideAnnotationsSectionRenderer(AnnotationAssertionAxiomsBySubjectIndex index) {
        return new AnnotationsSectionRenderer(index);
    }

    @SuppressWarnings("unchecked")
    @Provides
    AnnotationsSectionRenderer<OWLClass> provideAnnotationsSectionRendererOwlClass(AnnotationsSectionRenderer impl) {
        return impl;
    }

    @SuppressWarnings("unchecked")
    @Provides
    AnnotationsSectionRenderer<OWLObjectProperty> provideAnnotationsSectionRendererOwlObjectProperty(AnnotationsSectionRenderer impl) {
        return impl;
    }

    @SuppressWarnings("unchecked")
    @Provides
    AnnotationsSectionRenderer<OWLDataProperty> provideAnnotationsSectionRendererOwlDataProperty(AnnotationsSectionRenderer impl) {
        return impl;
    }

    @SuppressWarnings("unchecked")
    @Provides
    AnnotationsSectionRenderer<OWLAnnotationProperty> provideAnnotationsSectionRendererOwlAnnotationProperty(AnnotationsSectionRenderer impl) {
        return impl;
    }

    @SuppressWarnings("unchecked")
    @Provides
    AnnotationsSectionRenderer<OWLNamedIndividual> provideAnnotationsSectionRendererOwlNamedIndividual(AnnotationsSectionRenderer impl) {
        return impl;
    }

    @SuppressWarnings("unchecked")
    @Provides
    AnnotationsSectionRenderer<OWLDatatype> provideAnnotationsSectionRendererOwlDatatype(AnnotationsSectionRenderer impl) {
        return impl;
    }

    @Provides
    IRIOrdinalProvider provideIRIIndexProvider() {
        return IRIOrdinalProvider.withDefaultAnnotationPropertyOrdering();
    }

    @Provides
    FrameComponentRenderer provideFrameComponentRenderer(FrameComponentRendererImpl impl) {
        return impl;
    }

    @Provides
    RelationshipMatcherFactory provideRelationshipMatcherFactory(MatcherFactory matcherFactory) {
        return matcherFactory;
    }

    @Provides
    HierarchyPositionMatcherFactory provideHierarchyPositionMatcherFactory(MatcherFactory matcherFactory) {
        return matcherFactory;
    }

    @Provides
    EntityMatcherFactory provideEntityMatcherFactory(MatcherFactory matcherFactory) {
        return matcherFactory;
    }

    @Provides
    ClassFrameProvider provideClassFrameProvider(ClassFrameProviderImpl impl) {
        return impl;
    }

    @Provides
    @LuceneIndexesDirectory
    Path provideLuceneIndexesDirectory(DataDirectoryProvider dataDirectoryProvider) {
        var dataDirectory = dataDirectoryProvider.get().toPath();
        return dataDirectory.resolve("lucene-indexes");

    }

    @Provides
    @ProjectSingleton
    EntitySearchFilterIndexesManager provideEntitySearchFilterIndexesManager(LuceneIndexWriterImpl writer) {
        return writer;
    }

    @Provides
    @ProjectSingleton
    ProjectEntitySearchFiltersManager provideProjectEntitySearchFiltersManager(ProjectEntitySearchFiltersManagerImpl impl) {
        return impl;
    }
}

