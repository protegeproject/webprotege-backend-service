package edu.stanford.protege.webprotege.inject.project;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 25/03/2014
 *         <p>
 *         A  module for a project.  The module ensures that any object graph contains project specific objects for the
 *         specified project (e.g. root ontology, short form provider etc.)
 */
//@Module(includes = {IndexModule.class, ShortFormModule.class, ProjectActionHandlersModule.class})
public class ProjectModule {

//    private final ProjectId projectId;
//
//    public ProjectModule(ProjectId projectId) {
//        this.projectId = projectId;
//    }
//
//
//    @ProjectSingleton
//    OWLDataFactory providesDataFactory() {
//        return new OWLDataFactoryImpl();
//    }
//
//
//    @ProjectSingleton
//    public OWLEntityProvider providesOWLEntityProvider(OWLDataFactory dataFactory) {
//        return dataFactory;
//    }
//
//
//    @ProjectSingleton
//    public OWLEntityByTypeProvider provideOwlEntityByTypeProvider(OWLDataFactory dataFactory) {
//        return dataFactory;
//    }
//
//
//    @ProjectSingleton
//    public ProjectId provideProjectId() {
//        return projectId;
//    }
//
//
//    @RootOntologyDocument
//    public File provideRootOntologyDocument(RootOntologyDocumentProvider provider) {
//        return provider.get();
//    }
//
//
//    @ProjectDirectory
//    public File provideProjectDirectory(ProjectDirectoryProvider provider) {
//        return provider.get();
//    }
//
//
//    @ChangeHistoryFile
//    public File provideChangeHistoryFile(ChangeHistoryFileProvider provider) {
//        return provider.get();
//    }
//
//
//    @ProjectSpecificUiConfigurationDataDirectory
//    public File provideProjectSpecificUiConfigurationDataDirectory(ProjectSpecificUiConfigurationDataDirectoryProvider provider) {
//        return provider.get();
//    }
//
//
//    public OWLAnnotationPropertyProvider provideAnnotationPropertyProvider(OWLDataFactory factory) {
//        return factory;
//    }
//
//
//    public Set<ChangeMatcher> providesChangeMatchers(
//            AnnotationAssertionChangeMatcher annotationAssertionChangeMatcher,
//            PropertyDomainAxiomChangeMatcher propertyDomainAxiomChangeMatcher,
//            PropertyRangeAxiomChangeMatcher propertyRangeAxiomChangeMatcher,
//            EditedAnnotationAssertionChangeMatcher editedAnnotationAssertionChangeMatcher,
//            FunctionalDataPropertyAxiomChangeMatcher functionalDataPropertyAxiomChangeMatcher,
//            ClassAssertionAxiomMatcher classAssertionAxiomMatcher,
//            SubClassOfAxiomMatcher subClassOfAxiomMatcher,
//            ClassMoveChangeMatcher classMoveChangeMatcher,
//            SubClassOfEditChangeMatcher subClassOfEditChangeMatcher,
//            PropertyAssertionAxiomMatcher propertyAssertionAxiomMatcher,
//            SameIndividualAxiomChangeMatcher sameIndividualAxiomChangeMatcher,
//            EntityCreationMatcher entityCreationMatcher,
//            EntityDeletionMatcher entityDeletionMatcher) {
//        ImmutableSet<Object> matchers = ImmutableSet.of(annotationAssertionChangeMatcher,
//                                                        propertyDomainAxiomChangeMatcher,
//                                                        propertyRangeAxiomChangeMatcher,
//                                                        editedAnnotationAssertionChangeMatcher,
//                                                        functionalDataPropertyAxiomChangeMatcher,
//                                                        classAssertionAxiomMatcher,
//                                                        subClassOfAxiomMatcher,
//                                                        classMoveChangeMatcher,
//                                                        subClassOfEditChangeMatcher,
//                                                        propertyAssertionAxiomMatcher,
//                                                        sameIndividualAxiomChangeMatcher,
//                                                        entityCreationMatcher,
//                                                        entityDeletionMatcher);
//        return matchers.stream()
//                       .map(m -> (ChangeMatcher) m)
//                       .collect(toImmutableSet());
//    }
//
//
//    @ClassHierarchyRoot
//    public OWLClass providesClassHierarchyRoot(ClassHierarchyRootProvider provider) {
//        return provider.get();
//    }
//
//
//    @ObjectPropertyHierarchyRoot
//    public OWLObjectProperty providesObjectPropertyHierarchyRoot(ObjectPropertyHierarchyRootProvider provider) {
//        return provider.get();
//    }
//
//
//    @DataPropertyHierarchyRoot
//    public OWLDataProperty providesDataPropertyHierarchyRoot(DataPropertyHierarchyRootProvider provider) {
//        return provider.get();
//    }
//
//
//    public HierarchyProvider<OWLObjectProperty>
//    provideHierarchyProviderOfObjectProperty(ObjectPropertyHierarchyProvider provider) {
//        return provider;
//    }
//
//
//
//    public HierarchyProvider<OWLDataProperty>
//    provideHierarchyProviderOfDataProperty(
//            DataPropertyHierarchyProvider provider) {
//        return provider;
//    }
//
//
//    DataPropertyHierarchyProvider provideDataPropertyHierarchyProvider(DataPropertyHierarchyProviderImpl impl) {
//        return impl;
//    }
//
//
//    public HierarchyProvider<OWLAnnotationProperty>
//    provideHierarchyProviderOfAnnotationProperty(
//            AnnotationPropertyHierarchyProvider provider) {
//        return provider;
//    }
//
//
//    AnnotationPropertyHierarchyProvider provideAnnotationPropertyHierarchyProvider(
//            AnnotationPropertyHierarchyProviderImpl impl) {
//        return impl;
//    }
//
//
//    public HasGetAncestors<OWLClass> providesOWLClassAncestors(HierarchyProvider<OWLClass> hierarchyProvider) {
//        return hierarchyProvider;
//    }
//
//
//    public ClassHierarchyProvider getClassHierarchyProvider(ClassHierarchyProviderImpl impl) {
//        return impl;
//    }
//
//
//    public HasGetAncestors<OWLObjectProperty> providesOWLObjectPropertyAncestors(HierarchyProvider<OWLObjectProperty> hierarchyProvider) {
//        return hierarchyProvider;
//    }
//
//
//    public HasGetAncestors<OWLDataProperty> providesOWLDataPropertyAncestors(HierarchyProvider<OWLDataProperty> hierarchyProvider) {
//        return hierarchyProvider;
//    }
//
//
//    public ObjectPropertyHierarchyProvider provideObjectPropertyHierarchyProvider(ObjectPropertyHierarchyProviderImpl impl) {
//        return impl;
//    }
//
//
//    public HasGetAncestors<OWLAnnotationProperty> providesOWLAnnotationPropertyAncestors(HierarchyProvider<OWLAnnotationProperty> hierarchyProvider) {
//        return hierarchyProvider;
//    }
//
//
//    public WatchManager provideWatchManager(WatchManagerImpl impl) {
//        // Attach it so that it listens for entity frame changed events
//        // There's no need to detatch it because it is project scoped
//        // and has the same lifetime as a project event manager.
//        impl.attach();
//        return impl;
//    }
//
//
//    public WatchTriggeredHandler provideWatchTriggeredHandler(WatchTriggeredHandlerImpl impl) {
//        return impl;
//    }
//
//    @ProjectSingleton
//
//    public ShortFormProvider provideShortFormProvider(ShortFormAdapter shortFormAdapter) {
//        return shortFormAdapter;
//    }
//
//
//    public IRIShortFormProvider provideIriShortFormProvider(IriShortFormAdapter adapter) {
//        return adapter;
//    }
//
//
//    public HasGetRendering provideHasGetRendering(RenderingManager renderingManager) {
//        return renderingManager;
//    }
//
//
//    public HasLang provideHasLang() {
//        return () -> "en";
//    }
//
//
//    @ProjectSingleton
//    public OntologyIRIShortFormProvider provideOntologyIRIShortFormProvider(WebProtegeOntologyIRIShortFormProvider provider) {
//        return provider;
//    }
//
//
//    public EntityIRIChecker provideEntityIRIChecker(EntityIRICheckerImpl iriChecker) {
//        return iriChecker;
//    }
//
//
//    public OWLOntologyChecker provideOntologyChecker(ShellOntologyChecker checker) {
//        return checker;
//    }
//
//
//    public HighlightedEntityChecker provideHighlightedEntityChecker(NullHighlightedEntityChecker checker) {
//        return checker;
//    }
//
//
//    @ProjectSingleton
//    public EventManager<ProjectEvent<?>> providesEventManager(EventManagerProvider eventManagerProvider) {
//        return eventManagerProvider.get();
//    }
//
//
//    public EventLifeTime provideEventLifeTime() {
//        return EventManagerProvider.PROJECT_EVENT_LIFE_TIME;
//    }
//
//
//    public RevisionNumber provideRevisionNumber(RevisionNumberProvider provider) {
//        return provider.get();
//    }
//
//
//    public HasGetChangeSubjects provideHasGetChangeSubjects(OntologyChangeSubjectProvider provider) {
//        return provider;
//    }
//
//
//    public DeprecatedEntityChecker provideDeprecatedEntityChecker(DeprecatedEntityCheckerImpl checker) {
//        return checker;
//    }
//
//
//    public HasGetRevisionSummary provideGetRevisionSummary(RevisionManagerImpl impl) {
//        return impl;
//    }
//
//
//    @ProjectSingleton
//    public RevisionManager provideRevisionManager(RevisionManagerImpl impl) {
//        return impl;
//    }
//
//
//    @ProjectSingleton
//    public RevisionStore provideRevisionStore(RevisionStoreProvider provider) {
//        return provider.get();
//    }
//
//
//    ImmutableList<IRI> providesShortFormOrdering() {
//        return DefaultShortFormAnnotationPropertyIRIs.asImmutableList();
//    }
//
//
//    @ProjectSingleton
//    HasHtmlBrowserText providesHasHtmlBrowserText(RenderingManager rm) {
//        return rm;
//    }
//
//
//    @ProjectSingleton
//    OWLObjectRenderer providesOWLObjectRenderer(OWLObjectRendererImpl impl) {
//        return impl;
//    }
//
////
////    @ProjectSingleton
////    HasImportsClosure providesHasImportsClosure(RootOntologyProvider provider) {
////        return provider.get();
////    }
//
//
//
//    HasApplyChanges providesHasApplyChanges(ChangeManager manager) {
//        return manager;
//    }
//
//
//
//    @ProjectSingleton
//    HierarchyProvider<OWLClass> provideClassHierarchyProvider(ClassHierarchyProviderImpl provider) {
//        return provider;
//    }
//
//
//    OWLObjectSelector<OWLClassExpression> provideClassExpressionSelector(OWLClassExpressionSelector selector) {
//        return selector;
//    }
//
//
//    OWLObjectSelector<OWLObjectPropertyExpression> provideObjectPropertyExpressionSelector(
//            OWLObjectPropertyExpressionSelector selector) {
//        return selector;
//    }
//
//
//    OWLObjectSelector<OWLDataPropertyExpression> provideDataPropertyExpressionSelector(OWLDataPropertyExpressionSelector selector) {
//        return selector;
//    }
//
//
//    OWLObjectSelector<OWLIndividual> provideIndividualSelector(OWLIndividualSelector selector) {
//        return selector;
//    }
//
//
//    OWLObjectSelector<SWRLAtom> provideSWRLAtomSelector(SWRLAtomSelector selector) {
//        return selector;
//    }
//
//
//    Comparator<? super OWLClass> providesClassComparator(OWLObjectComparatorImpl impl) {
//        return impl;
//    }
//
//
//    Comparator<? super OWLObjectProperty> providesObjectPropertyComparator(OWLObjectComparatorImpl impl) {
//        return impl;
//    }
//
//
//    Comparator<? super OWLDataProperty> providesDataPropertyComparator(OWLObjectComparatorImpl impl) {
//        return impl;
//    }
//
//
//    Comparator<? super OWLAnnotationProperty> providesAnnotationPropertyComparator(AnnotationPropertyComparatorImpl impl) {
//        return impl;
//    }
//
//
//    Comparator<? super OWLAnnotation> providesAnnotationComparator(OWLObjectComparatorImpl impl) {
//        return impl;
//    }
//
//
//    Comparator<? super OWLNamedIndividual> providesNamedIndividualComparator(OWLObjectComparatorImpl impl) {
//        return impl;
//    }
//
//
//    Comparator<? super OWLDatatype> providesDatatypeComparator(OWLObjectComparatorImpl impl) {
//        return impl;
//    }
//
//
//    Comparator<? super SWRLAtom> providesSWRLAtomComparator(OWLObjectComparatorImpl impl) {
//        return impl;
//    }
//
//
//    Comparator<OntologyChange> providesOWLOntologyChangeRecordComparator(OntologyChangeComparator comparator) {
//        return comparator;
//    }
//
//
//    Comparator<PropertyValue> providePropertyValueComparator(PropertyValueComparator propertyValueComparator) {
//        return propertyValueComparator;
//    }
//
//
//    Comparator<OWLAxiom> providesAxiomComparator(AxiomComparatorImpl impl) {
//        return impl;
//    }
//
//
//    Comparator<OWLObject> providesOWLObjectComparator(OWLObjectComparatorImpl impl) {
//        return impl;
//    }
//
//
//    public Set<EventTranslator> providesEventTranslators(
//            BrowserTextChangedEventComputer c0,
//            HighLevelEventGenerator c1,
//            OWLClassHierarchyChangeComputer c2,
//            OWLObjectPropertyHierarchyChangeComputer c3,
//            OWLDataPropertyHierarchyChangeComputer c4,
//            OWLAnnotationPropertyHierarchyChangeComputer c5,
//            EntityDeprecatedChangedEventTranslator c6,
//            EntityTagsChangedEventComputer c7) {
//        return ImmutableSet.of(c0, c1, c2, c3, c4, c5, c6, c7);
//    }
//
//
//    HasPostEvents<ProjectEvent<?>> provideHasPostEvents(EventManager<ProjectEvent<?>> eventManager) {
//        return eventManager;
//    }
//
//
//    NullHighlightedEntityChecker providesNullHighlightedEntityChecker() {
//        return NullHighlightedEntityChecker.get();
//    }
//
//
//    HasContainsEntityInSignature providesHasContainsEntityInSignature(HasContainsEntityInSignatureImpl impl) {
//        return impl;
//    }
//
//
//    List<AxiomType<?>> providesAxiomTypeList() {
//        return DefaultAxiomTypeOrdering.get();
//    }
//
//
//    PropertyValueSubsumptionChecker providePropertyValueSubsumptionChecker(StructuralPropertyValueSubsumptionChecker impl) {
//        return impl;
//    }
//
//
//    HasHasAncestor<OWLClass, OWLClass> provideClassClassHasAncestor(ClassClassAncestorChecker checker) {
//        return checker;
//    }
//
//
//    HasHasAncestor<OWLObjectProperty, OWLObjectProperty> provideObjectPropertyObjectPropertyHasAncestor(
//            ObjectPropertyObjectPropertyAncestorChecker checker) {
//        return checker;
//    }
//
//
//
//    HasHasAncestor<OWLDataProperty, OWLDataProperty> provideDataPropertyDataPropertyHasAncestor(
//            DataPropertyDataPropertyAncestorChecker checker) {
//        return checker;
//    }
//
//
//    HasHasAncestor<OWLNamedIndividual, OWLClass> provideNamedIndividualClassHasAncestor(
//            NamedIndividualClassAncestorChecker checker) {
//        return checker;
//    }
//
//
//    OBONamespaceCache providesOboNamespaceCache(OBONamespaceCacheFactory factory) {
//        var namespaceCache = factory.create();
//        namespaceCache.rebuildNamespaceCache();
//        return namespaceCache;
//    }
//
//
//    MatchingEngine provideMatchingEngine(MatchingEngineImpl impl) {
//        return impl;
//    }
//
//
//    HierarchyPositionMatchingEngine provideHierarchyPositionMatchingEngine(HierarchyPositionMatchingEngineImpl impl) {
//        return impl;
//    }
//
//
//    TagRepository provideTagRepository(TagRepositoryImpl impl) {
//        impl.ensureIndexes();
//        return impl;
//    }
//
//    EntityTagsRepository provideEntityTagsRepository(EntityTagsRepositoryImpl impl) {
//        impl.ensureIndexes();
//        return impl;
//    }
//
//    @ProjectSingleton
//
//    ProjectDisposablesManager provideProjectDisposableObjectManager(DisposableObjectManager disposableObjectManager) {
//        return new ProjectDisposablesManager(disposableObjectManager);
//    }
//
//
//    LiteralRenderer provideLiteralRenderer(@Nonnull
//                                                   StringFormatterLiteralRendererImpl impl) {
//        return impl;
//    }
//
//
//    LiteralLexicalFormTransformer provideLiteralLexicalFormTransformer() {
//        return lexicalForm -> lexicalForm;
//    }
//
//
//    LiteralLangTagTransformer provideLangTagTransformer() {
//        return langTag -> langTag;
//    }
//
//
//
//    public EntityCrudKitPlugin<?, ?, ?> provideUUIDPlugin(UuidEntityCrudKitPlugin plugin) {
//        return plugin;
//    }
//
//
//
//    public EntityCrudKitPlugin<?, ?, ?> provideOBOIdPlugin(OBOIdSuffixEntityCrudKitPlugin plugin) {
//        return plugin;
//    }
//
//
//
//    public EntityCrudKitPlugin<?, ?, ?> provideSuppliedNamePlugin(SuppliedNameSuffixEntityCrudKitPlugin plugin) {
//        return plugin;
//    }
//
//
//
//    TermDefinitionManager provideTermDefinitionManager(TermDefinitionManagerImpl impl) {
//        return impl;
//    }
//
//
//    AnnotationsSectionRenderer provideAnnotationsSectionRenderer(AnnotationAssertionAxiomsBySubjectIndex index) {
//        return new AnnotationsSectionRenderer(index);
//    }
//
//    @SuppressWarnings("unchecked")
//
//    AnnotationsSectionRenderer<OWLClass> provideAnnotationsSectionRendererOwlClass(AnnotationsSectionRenderer impl) {
//        return impl;
//    }
//
//    @SuppressWarnings("unchecked")
//
//    AnnotationsSectionRenderer<OWLObjectProperty> provideAnnotationsSectionRendererOwlObjectProperty(AnnotationsSectionRenderer impl) {
//        return impl;
//    }
//
//    @SuppressWarnings("unchecked")
//
//    AnnotationsSectionRenderer<OWLDataProperty> provideAnnotationsSectionRendererOwlDataProperty(AnnotationsSectionRenderer impl) {
//        return impl;
//    }
//
//    @SuppressWarnings("unchecked")
//
//    AnnotationsSectionRenderer<OWLAnnotationProperty> provideAnnotationsSectionRendererOwlAnnotationProperty(AnnotationsSectionRenderer impl) {
//        return impl;
//    }
//
//    @SuppressWarnings("unchecked")
//
//    AnnotationsSectionRenderer<OWLNamedIndividual> provideAnnotationsSectionRendererOwlNamedIndividual(AnnotationsSectionRenderer impl) {
//        return impl;
//    }
//
//    @SuppressWarnings("unchecked")
//
//    AnnotationsSectionRenderer<OWLDatatype> provideAnnotationsSectionRendererOwlDatatype(AnnotationsSectionRenderer impl) {
//        return impl;
//    }
//
//
//    IRIOrdinalProvider provideIRIIndexProvider() {
//        return IRIOrdinalProvider.withDefaultAnnotationPropertyOrdering();
//    }
//
//
//    FrameComponentRenderer provideFrameComponentRenderer(FrameComponentRendererImpl impl) {
//        return impl;
//    }
//
//
//    RelationshipMatcherFactory provideRelationshipMatcherFactory(MatcherFactory matcherFactory) {
//        return matcherFactory;
//    }
//
//
//    HierarchyPositionMatcherFactory provideHierarchyPositionMatcherFactory(MatcherFactory matcherFactory) {
//        return matcherFactory;
//    }
//
//
//    EntityMatcherFactory provideEntityMatcherFactory(MatcherFactory matcherFactory) {
//        return matcherFactory;
//    }
//
//
//    ClassFrameProvider provideClassFrameProvider(ClassFrameProviderImpl impl) {
//        return impl;
//    }
//
//
//    @LuceneIndexesDirectory
//    Path provideLuceneIndexesDirectory(DataDirectoryProvider dataDirectoryProvider) {
//        var dataDirectory = dataDirectoryProvider.get().toPath();
//        return dataDirectory.resolve("lucene-indexes");
//
//    }
//
//
//    @ProjectSingleton
//    EntitySearchFilterIndexesManager provideEntitySearchFilterIndexesManager(LuceneIndexWriterImpl writer) {
//        return writer;
//    }
//
//
//    @ProjectSingleton
//    ProjectEntitySearchFiltersManager provideProjectEntitySearchFiltersManager(ProjectEntitySearchFiltersManagerImpl impl) {
//        return impl;
//    }
}

