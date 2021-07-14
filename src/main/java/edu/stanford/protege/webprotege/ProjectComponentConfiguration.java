package edu.stanford.protege.webprotege;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.app.ApplicationNameSupplier;
import edu.stanford.protege.webprotege.app.PlaceUrl;
import edu.stanford.protege.webprotege.app.UserInSessionFactory;
import edu.stanford.protege.webprotege.axiom.*;
import edu.stanford.protege.webprotege.change.HasGetChangeSubjects;
import edu.stanford.protege.webprotege.change.OntologyChangeComparator;
import edu.stanford.protege.webprotege.change.OntologyChangeRecordTranslator;
import edu.stanford.protege.webprotege.change.OntologyChangeSubjectProvider;
import edu.stanford.protege.webprotege.change.matcher.*;
import edu.stanford.protege.webprotege.crud.*;
import edu.stanford.protege.webprotege.crud.gen.GeneratedAnnotationsGenerator;
import edu.stanford.protege.webprotege.crud.gen.IncrementingPatternDescriptorValueGenerator;
import edu.stanford.protege.webprotege.crud.obo.OBOIdSuffixEntityCrudKitHandlerFactory;
import edu.stanford.protege.webprotege.crud.obo.OBOIdSuffixEntityCrudKitPlugin;
import edu.stanford.protege.webprotege.crud.oboid.OBOIdSuffixKit;
import edu.stanford.protege.webprotege.crud.persistence.ProjectEntityCrudKitSettingsRepository;
import edu.stanford.protege.webprotege.crud.supplied.SuppliedNameSuffixEntityCrudKitHandlerFactory;
import edu.stanford.protege.webprotege.crud.supplied.SuppliedNameSuffixEntityCrudKitPlugin;
import edu.stanford.protege.webprotege.crud.supplied.SuppliedNameSuffixKit;
import edu.stanford.protege.webprotege.crud.uuid.UuidEntityCrudKitHandlerFactory;
import edu.stanford.protege.webprotege.crud.uuid.UuidEntityCrudKitPlugin;
import edu.stanford.protege.webprotege.crud.uuid.UuidSuffixKit;
import edu.stanford.protege.webprotege.dispatch.ProjectActionHandler;
import edu.stanford.protege.webprotege.dispatch.impl.ProjectActionHandlerRegistry;
import edu.stanford.protege.webprotege.entity.EntityNodeRenderer;
import edu.stanford.protege.webprotege.entity.SubjectClosureResolver;
import edu.stanford.protege.webprotege.event.ProjectEvent;
import edu.stanford.protege.webprotege.events.*;
import edu.stanford.protege.webprotege.filemanager.FileContents;
import edu.stanford.protege.webprotege.frame.*;
import edu.stanford.protege.webprotege.frame.translator.*;
import edu.stanford.protege.webprotege.hierarchy.*;
import edu.stanford.protege.webprotege.index.*;
import edu.stanford.protege.webprotege.index.impl.IndexUpdater;
import edu.stanford.protege.webprotege.index.impl.RootIndexImpl;
import edu.stanford.protege.webprotege.index.impl.UpdatableIndex;
import edu.stanford.protege.webprotege.inject.*;
import edu.stanford.protege.webprotege.inject.project.*;
import edu.stanford.protege.webprotege.issues.EntityDiscussionThreadRepository;
import edu.stanford.protege.webprotege.lang.ActiveLanguagesManager;
import edu.stanford.protege.webprotege.lang.ActiveLanguagesManagerImpl;
import edu.stanford.protege.webprotege.lang.LanguageManager;
import edu.stanford.protege.webprotege.mail.SendMail;
import edu.stanford.protege.webprotege.mansyntax.ShellOntologyChecker;
import edu.stanford.protege.webprotege.mansyntax.render.*;
import edu.stanford.protege.webprotege.match.*;
import edu.stanford.protege.webprotege.msg.MessageFormatter;
import edu.stanford.protege.webprotege.object.*;
import edu.stanford.protege.webprotege.obo.*;
import edu.stanford.protege.webprotege.owlapi.HasContainsEntityInSignatureImpl;
import edu.stanford.protege.webprotege.owlapi.OWLObjectStringFormatter;
import edu.stanford.protege.webprotege.owlapi.RenameMapFactory;
import edu.stanford.protege.webprotege.owlapi.StringFormatterLiteralRendererImpl;
import edu.stanford.protege.webprotege.project.*;
import edu.stanford.protege.webprotege.project.chg.ChangeManager;
import edu.stanford.protege.webprotege.renderer.LiteralRenderer;
import edu.stanford.protege.webprotege.renderer.*;
import edu.stanford.protege.webprotege.revision.*;
import edu.stanford.protege.webprotege.shortform.*;
import edu.stanford.protege.webprotege.tag.*;
import edu.stanford.protege.webprotege.templates.TemplateEngine;
import edu.stanford.protege.webprotege.user.UserDetailsManager;
import edu.stanford.protege.webprotege.util.DisposableObjectManager;
import edu.stanford.protege.webprotege.util.IriReplacerFactory;
import edu.stanford.protege.webprotege.watches.*;
import edu.stanford.protege.webprotege.webhook.JsonPayloadWebhookExecutor;
import edu.stanford.protege.webprotege.webhook.ProjectChangedWebhookInvoker;
import edu.stanford.protege.webprotege.webhook.WebhookExecutor;
import edu.stanford.protege.webprotege.webhook.WebhookRepository;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.IRIShortFormProvider;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.inject.Provider;
import java.io.File;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-07-12
 */
public class ProjectComponentConfiguration {

    @Bean
    ProjectComponent projectComponent(ProjectId projectId,
                                      EventManager<ProjectEvent<?>> eventManager,
                                      RevisionManager revisionManager,
                                      ProjectDisposablesManager projectDisposablesManager,
                                      ProjectActionHandlerRegistry actionHandlerRegistry) {
        return new ProjectComponentImpl(projectId,
                                        eventManager,
                                        revisionManager,
                                        projectDisposablesManager, actionHandlerRegistry);
    }

    @Bean
    RootOntologyDocumentProvider rootOntologyDocumentProvider(@ProjectDirectory File projectDirectory) {
        return new RootOntologyDocumentProvider(projectDirectory);
    }

    @Bean
    @RootOntologyDocument
    public File rootOntologyDocument(RootOntologyDocumentProvider provider) {
        return provider.get();
    }

    @Bean
    ProjectDirectoryProvider projectDirectoryProvider(ProjectDirectoryFactory projectDirectoryFactory,
                                                      ProjectId projectId) {
        return new ProjectDirectoryProvider(projectDirectoryFactory, projectId);
    }

    @Bean
    @ProjectDirectory
    public File projectDirectory(ProjectDirectoryProvider provider) {
        return provider.get();
    }

    @Bean
    ChangeHistoryFileProvider changeHistoryFileProvider(ProjectId projectId,
                                                        ChangeHistoryFileFactory changeHistoryFileFactory) {
        return new ChangeHistoryFileProvider(projectId, changeHistoryFileFactory);
    }

    @Bean
    @ChangeHistoryFile
    public File changeHistoryFile(ChangeHistoryFileProvider provider) {
        return provider.get();
    }

    @Bean
    AnnotationAssertionChangeMatcher annotationAssertionChangeMatcher() {
        return new AnnotationAssertionChangeMatcher();
    }

    @Bean
    PropertyDomainAxiomChangeMatcher propertyDomainAxiomChangeMatcher() {
        return new PropertyDomainAxiomChangeMatcher();
    }

    @Bean
    PropertyRangeAxiomChangeMatcher propertyRangeAxiomChangeMatcher() {
        return new PropertyRangeAxiomChangeMatcher();
    }

    @Bean
    EditedAnnotationAssertionChangeMatcher editedAnnotationAssertionChangeMatcher(OWLObjectStringFormatter p0,
                                                                                  LiteralLangTagTransformer p1) {
        return new EditedAnnotationAssertionChangeMatcher(p0, p1);
    }

    @Bean
    FunctionalDataPropertyAxiomChangeMatcher functionalDataPropertyAxiomChangeMatcher() {
        return new FunctionalDataPropertyAxiomChangeMatcher();
    }

    @Bean
    ClassAssertionAxiomMatcher classAssertionAxiomMatcher() {
        return new ClassAssertionAxiomMatcher();
    }

    @Bean
    SubClassOfAxiomMatcher subClassOfAxiomMatcher(OWLObjectStringFormatter p1) {
        return new SubClassOfAxiomMatcher(p1);
    }

    @Bean
    ClassMoveChangeMatcher classMoveChangeMatcher(OWLObjectStringFormatter p1) {
        return new ClassMoveChangeMatcher(p1);
    }

    @Bean
    SubClassOfEditChangeMatcher subClassOfEditChangeMatcher() {
        return new SubClassOfEditChangeMatcher();
    }

    @Bean
    PropertyAssertionAxiomMatcher propertyAssertionAxiomMatcher() {
        return new PropertyAssertionAxiomMatcher();
    }

    @Bean
    SameIndividualAxiomChangeMatcher sameIndividualAxiomChangeMatcher() {
        return new SameIndividualAxiomChangeMatcher();
    }

    @Bean
    EntityCreationMatcher entityCreationMatcher(OWLObjectStringFormatter p1) {
        return new EntityCreationMatcher(p1);
    }

    @Bean
    EntityDeletionMatcher entityDeletionMatcher() {
        return new EntityDeletionMatcher();
    }

    @Bean
    OWLObjectStringFormatter owlObjectStringFormatter(ShortFormProvider p1,
                                                      IRIShortFormProvider p2,
                                                      LiteralRenderer p3) {
        return new OWLObjectStringFormatter(p1, p2, p3);
    }

    @Bean
    ClassHierarchyRootProvider classHierarchyRootProvider(OWLDataFactory dataFactory) {
        return new ClassHierarchyRootProvider(dataFactory);
    }

    @Bean
    @ClassHierarchyRoot
    public OWLClass classHierarchyRoot(ClassHierarchyRootProvider provider) {
        return provider.get();
    }

    @Bean
    ObjectPropertyHierarchyRootProvider objectPropertyHierarchyRootProvider(OWLDataFactory p1) {
        return new ObjectPropertyHierarchyRootProvider(p1);
    }

    @Bean
    @ObjectPropertyHierarchyRoot
    public OWLObjectProperty objectPropertyHierarchyRoot(ObjectPropertyHierarchyRootProvider provider) {
        return provider.get();
    }

    @Bean
    DataPropertyHierarchyRootProvider dataPropertyHierarchyRootProvider(OWLDataFactory p1) {
        return new DataPropertyHierarchyRootProvider(p1);
    }

    @Bean
    @DataPropertyHierarchyRoot
    public OWLDataProperty dataPropertyHierarchyRoot(DataPropertyHierarchyRootProvider provider) {
        return provider.get();
    }

    @Bean
    ObjectPropertyHierarchyProviderImpl objectPropertyHierarchyProvider(ProjectId p1,
                                                                        @ObjectPropertyHierarchyRoot OWLObjectProperty p2,
                                                                        EntitiesInProjectSignatureIndex p3,
                                                                        ProjectOntologiesIndex p4,
                                                                        OntologySignatureByTypeIndex p5,
                                                                        SubObjectPropertyAxiomsBySubPropertyIndex p6,
                                                                        AxiomsByTypeIndex p7) {
        return new ObjectPropertyHierarchyProviderImpl(p1, p2, p3, p4, p5, p6, p7);
    }


    @Bean
    DataPropertyHierarchyProviderImpl dataPropertyHierarchyProvider(ProjectId projectId,
                                                                    @DataPropertyHierarchyRoot OWLDataProperty dataProperty,
                                                                    ProjectOntologiesIndex projectOntologiesIndex,
                                                                    AxiomsByTypeIndex axiomsByTypeIndex,
                                                                    OntologySignatureByTypeIndex ontologySignatureByTypeIndex,
                                                                    SubDataPropertyAxiomsBySubPropertyIndex subDataPropertyAxiomsBySubPropertyIndex,
                                                                    EntitiesInProjectSignatureIndex entitiesInProjectSignatureIndex) {
        return new DataPropertyHierarchyProviderImpl(projectId,
                                                     dataProperty,
                                                     projectOntologiesIndex,
                                                     axiomsByTypeIndex,
                                                     ontologySignatureByTypeIndex,
                                                     subDataPropertyAxiomsBySubPropertyIndex,
                                                     entitiesInProjectSignatureIndex);
    }

    @Bean
    public AnnotationPropertyHierarchyProvider hierarchyProviderOfAnnotationProperty(ProjectId projectId,
                                                                                     OWLAnnotationPropertyProvider owlAnnotationPropertyProvider,
                                                                                     ProjectSignatureByTypeIndex projectSignatureByTypeIndex,
                                                                                     ProjectOntologiesIndex projectOntologiesIndex,
                                                                                     SubAnnotationPropertyAxiomsBySubPropertyIndex subAnnotationPropertyAxiomsBySubPropertyIndex,
                                                                                     SubAnnotationPropertyAxiomsBySuperPropertyIndex subAnnotationPropertyAxiomsBySuperPropertyIndex,
                                                                                     EntitiesInProjectSignatureIndex entitiesInProjectSignatureIndex) {
        return new AnnotationPropertyHierarchyProviderImpl(projectId,
                                                           owlAnnotationPropertyProvider,
                                                           projectSignatureByTypeIndex,
                                                           projectOntologiesIndex,
                                                           subAnnotationPropertyAxiomsBySubPropertyIndex,
                                                           subAnnotationPropertyAxiomsBySuperPropertyIndex,
                                                           entitiesInProjectSignatureIndex);
    }

    @Bean
    WatchRecordRepositoryImpl watchRecordRepository(MongoTemplate template,
                                                    ObjectMapper objectMapper) {
        return new WatchRecordRepositoryImpl(template, objectMapper);
    }

    @Bean
    IndirectlyWatchedEntitiesFinder indirectlyWatchedEntitiesFinder(HasGetAncestors<OWLClass> p1,
                                                                    HasGetAncestors<OWLObjectProperty> p2,
                                                                    HasGetAncestors<OWLDataProperty> p3,
                                                                    HasGetAncestors<OWLAnnotationProperty> p4,
                                                                    ProjectClassAssertionAxiomsByIndividualIndex p5) {
        return new IndirectlyWatchedEntitiesFinder(p1, p2, p3, p4, p5);
    }

    @Bean
    public WatchManagerImpl watchManager(ProjectId p1,
                                         WatchRecordRepository p2,
                                         IndirectlyWatchedEntitiesFinder p3,
                                         WatchTriggeredHandler p4,
                                         EventManager<ProjectEvent<?>> p5) {
        var impl = new WatchManagerImpl(p1, p2, p3, p4, p5);
        // Attach it so that it listens for entity frame changed events
        // There's no need to detatch it because it is project scoped
        // and has the same lifetime as a project event manager.
        impl.attach();
        return impl;
    }

    @Bean
    public WatchTriggeredHandler watchTriggeredHandler(ProjectId p1,
                                                       RenderingManager p2,
                                                       ApplicationNameSupplier p3,
                                                       AccessManager p4,
                                                       PlaceUrl p5,
                                                       SendMail p6,
                                                       UserDetailsManager p7,
                                                       ProjectDetailsManager p8,
                                                       TemplateEngine p9,
                                                       @WatchNotificationEmailTemplate FileContents p10) {
        return new WatchTriggeredHandlerImpl(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10);
    }

    @ProjectSingleton
    @Bean
    public ShortFormProvider shortFormProvider(DictionaryManager p1) {
        return new ShortFormAdapter(p1);
    }

    @Bean
    @Primary
    public IRIShortFormProvider iriShortFormProvider(EntitiesInProjectSignatureByIriIndex p1, DictionaryManager p2) {
        return new IriShortFormAdapter(p1, p2);
    }

    @Bean
    ManchesterSyntaxObjectRenderer manchesterSyntaxObjectRenderer(ShortFormProvider p1,
                                                                  EntityIRIChecker p2,
                                                                  HttpLinkRenderer p4) {
        return new ManchesterSyntaxObjectRenderer(p1, p2, LiteralStyle.REGULAR, p4, new MarkdownLiteralRenderer());
    }

    @Bean
    RenderingManager renderingManager(DictionaryManager dictionaryManager,
                                      DeprecatedEntityChecker deprecatedEntityChecker,
                                      ManchesterSyntaxObjectRenderer manchesterSyntaxObjectRenderer) {
        return new RenderingManager(dictionaryManager, deprecatedEntityChecker, manchesterSyntaxObjectRenderer);
    }

    @Bean
    public HasLang hasLang() {
        return () -> "en";
    }

    @Bean
    DefaultOntologyIdManager defaultOntologyIdManager(ProjectOntologiesIndex p1) {
        return new DefaultOntologyIdManagerImpl(p1);
    }

    @Bean
    @ProjectSingleton
    public WebProtegeOntologyIRIShortFormProvider webProtegeOntologyIRIShortFormProvider(DefaultOntologyIdManager defaultOntologyIdManager) {
        return new WebProtegeOntologyIRIShortFormProvider(defaultOntologyIdManager);
    }

    @Bean
    public EntityIRICheckerImpl entityIRIChecker(EntitiesInProjectSignatureByIriIndex entitiesInProjectSignatureByIriIndex) {
        return new EntityIRICheckerImpl(entitiesInProjectSignatureByIriIndex);
    }

    @Bean
    public ShellOntologyChecker ontologyChecker(ProjectOntologiesIndex projectOntologiesIndex,
                                                WebProtegeOntologyIRIShortFormProvider webProtegeOntologyIRIShortFormProvider,
                                                DefaultOntologyIdManager defaultOntologyIdManager) {
        return new ShellOntologyChecker(projectOntologiesIndex,
                                        webProtegeOntologyIRIShortFormProvider,
                                        defaultOntologyIdManager);
    }

    @Bean
    public NullHighlightedEntityChecker highlightedEntityChecker() {
        return new NullHighlightedEntityChecker();
    }

    @Bean
    @ProjectSingleton
    public EventManager<ProjectEvent<?>> eventManager(EventManagerProvider eventManagerProvider) {
        return eventManagerProvider.get();
    }

    @Bean
    public EventLifeTime eventLifeTime() {
        return EventManagerProvider.PROJECT_EVENT_LIFE_TIME;
    }

    @Bean
    public RevisionNumber revisionNumber(RevisionNumberProvider provider) {
        return provider.get();
    }

    @Bean
    SubjectClosureResolver subjectClosureResolver(AnnotationAssertionAxiomsByValueIndex p1,
                                                  ProjectOntologiesIndex p2,
                                                  EntitiesInProjectSignatureByIriIndex p3) {
        return new SubjectClosureResolver(p1, p2, p3);
    }

    @Bean
    public OntologyChangeSubjectProvider hasGetChangeSubjects(EntitiesInProjectSignatureByIriIndex entitiesInProjectSignatureByIriIndex,
                                                              SubjectClosureResolver subjectClosureResolver) {
        return new OntologyChangeSubjectProvider(entitiesInProjectSignatureByIriIndex, subjectClosureResolver);
    }

    @Bean
    public DeprecatedEntityCheckerImpl deprecatedEntityChecker(DeprecatedEntitiesByEntityIndex deprecatedEntitiesByEntityIndex) {
        return new DeprecatedEntityCheckerImpl(deprecatedEntitiesByEntityIndex);
    }

    @Bean
    public RevisionManagerImpl getRevisionSummary(RevisionStore revisionStore) {
        return new RevisionManagerImpl(revisionStore);
    }

    @Bean
    public RevisionStoreImpl revisionStore(ProjectId p1,
                                           ChangeHistoryFileFactory p2,
                                           OWLDataFactory p3,
                                           OntologyChangeRecordTranslator p4) {
        return new RevisionStoreImpl(p1, p2, p3, p4);
    }

    @Bean
    public RevisionStoreProvider revisionStoreProvider(RevisionStoreImpl revisionStore,
                                                       ProjectDisposablesManager projectDisposablesManager) {
        return new RevisionStoreProvider(revisionStore, projectDisposablesManager);
    }

    @Bean
    ImmutableList<IRI> shortFormOrdering() {
        return DefaultShortFormAnnotationPropertyIRIs.asImmutableList();
    }

    @Bean
    @Primary
    OWLObjectRendererImpl owlObjectRenderer(RenderingManager renderingManager) {
        return new OWLObjectRendererImpl(renderingManager);
    }

    @Bean
    DictionaryUpdatesProcessor dictionaryUpdatesProcessor(HasGetChangeSubjects p1, DictionaryManager p2) {
        return new DictionaryUpdatesProcessor(p1, p2);
    }

    @Bean
    PrefixDeclarationsStore prefixDeclarationsStore(ObjectMapper p1, MongoTemplate p2) {
        return new PrefixDeclarationsStore(p1, p2);
    }

    @Bean
    WebhookExecutor webhookExecutor() {
        return new WebhookExecutor();
    }

    @Bean
    JsonPayloadWebhookExecutor jsonPayloadWebhookExecutor(WebhookExecutor p1, ObjectMapper p2) {
        return new JsonPayloadWebhookExecutor(p1, p2);
    }

    @Bean
    ProjectChangedWebhookInvoker projectChangedWebhookInvoker(ProjectId p1,
                                                              JsonPayloadWebhookExecutor p2,
                                                              WebhookRepository p3) {
        return new ProjectChangedWebhookInvoker(p1, p2, p3);
    }

    @Bean
    EventTranslatorManager eventTranslatorManager(Set<EventTranslator> p1) {
        return new EventTranslatorManager(p1);
    }

    @Bean
    EntityCrudKitPluginManager entityCrudKitPluginManager(Set<EntityCrudKitPlugin<?, ?, ?>> p1) {
        return new EntityCrudKitPluginManager(p1);
    }

    @Bean
    EntityCrudKitRegistry entityCrudKitRegistry(EntityCrudKitPluginManager p1) {
        return new EntityCrudKitRegistry(p1);
    }

    @Bean
    ProjectEntityCrudKitHandlerCache projectEntityCrudKitHandlerCache(ProjectEntityCrudKitSettingsRepository p1,
                                                                      ProjectId p2, EntityCrudKitRegistry p3) {
        return new ProjectEntityCrudKitHandlerCache(p1, p2, p3);
    }

    @Bean
    RootIndexImpl rootIndex(OntologyAxiomsIndex p1, OntologyAnnotationsIndex p2) {
        return new RootIndexImpl(p1, p2);
    }

    @Bean
    EntityCrudContextFactory entityCrudContextFactory(ProjectId p1, ProjectDetailsRepository p2) {
        return new EntityCrudContextFactory(p1, p2);
    }

    @Bean
    RenameMapFactory renameMapFactory(RenderingManager p1, OWLDataFactory p2) {
        return new RenameMapFactory(p1, p2);
    }

    @Bean
    IndexUpdater indexUpdater(RevisionManager p1, Set<UpdatableIndex> p2, @IndexUpdatingService ExecutorService p3, ProjectId p4) {
        return new IndexUpdater(p1, p2, p3, p4);
    }

    @Bean
    IriReplacerFactory iriReplacerFactory(OWLDataFactory p1) {
        return new IriReplacerFactory(p1);
    }

    @Bean
    EntityIriPrefixCriteriaRewriter entityIriPrefixCriteriaRewriter() {
        return new EntityIriPrefixCriteriaRewriter();
    }

    @Bean
    IncrementingPatternDescriptorValueGenerator incrementingPatternDescriptorValueGenerator(OWLDataFactory p1,
                                                                                            AnnotationAssertionAxiomsByValueIndex p2,
                                                                                            ProjectOntologiesIndex p3) {
        return new IncrementingPatternDescriptorValueGenerator(p1, p2, p3);
    }

    @Bean
    GeneratedAnnotationsGenerator generatedAnnotationsGenerator(MatcherFactory p1,
                                                                EntityIriPrefixCriteriaRewriter p2,
                                                                IncrementingPatternDescriptorValueGenerator p3,
                                                                OWLDataFactory p4, DefaultOntologyIdManager p5) {
        return new GeneratedAnnotationsGenerator(p1, p2, p3, p4, p5);
    }

    @Bean
    ChangeManager changeManager(ProjectId p1,
                                  OWLDataFactory p2,
                                  DictionaryUpdatesProcessor p3,
                                  ActiveLanguagesManager p4,
                                  AccessManager p5,
                                  PrefixDeclarationsStore p6,
                                  ProjectDetailsRepository p7,
                                  ProjectChangedWebhookInvoker p8,
                                  EventManager<ProjectEvent<?>> p9,
                                  Provider<EventTranslatorManager> p10,
                                  ProjectEntityCrudKitHandlerCache p11,
                                  RevisionManager p12,
                                  RootIndex p13,
                                  DictionaryManager p14,
                                  ClassHierarchyProvider p15,
                                  ObjectPropertyHierarchyProvider p16,
                                  DataPropertyHierarchyProvider p17,
                                  AnnotationPropertyHierarchyProvider p18,
                                  UserInSessionFactory p19,
                                  EntityCrudContextFactory p20,
                                  RenameMapFactory p21,
                                  BuiltInPrefixDeclarations p22,
                                  IndexUpdater p23,
                                  DefaultOntologyIdManager p24,
                                  IriReplacerFactory p25,
                                  GeneratedAnnotationsGenerator p26) {
        return new ChangeManager(p1,
                                 p2,
                                 p3,
                                 p4,
                                 p5,
                                 p6,
                                 p7,
                                 p8,
                                 p9,
                                 p10,
                                 p11,
                                 p12,
                                 p13,
                                 p14,
                                 p15,
                                 p16,
                                 p17,
                                 p18,
                                 p19,
                                 p20,
                                 p21,
                                 p22,
                                 p23,
                                 p24,
                                 p25,
                                 p26);
    }


    @Bean
    ClassHierarchyProviderImpl classHierarchyProvider(ProjectId p1,
                                                      @ClassHierarchyRoot OWLClass p2,
                                                      ProjectOntologiesIndex p3,
                                                      SubClassOfAxiomsBySubClassIndex p4,
                                                      EquivalentClassesAxiomsIndex p5,
                                                      ProjectSignatureByTypeIndex p6,
                                                      EntitiesInProjectSignatureByIriIndex p7,
                                                      ClassHierarchyChildrenAxiomsIndex p8) {
        return new ClassHierarchyProviderImpl(p1, p2, p3, p4, p5, p6, p7, p8);
    }

    @Bean
    OWLClassExpressionSelector classExpressionSelector(Comparator<? super OWLClass> p1) {
        return new OWLClassExpressionSelector(p1);
    }

    @Bean
    OWLObjectPropertyExpressionSelector objectPropertyExpressionSelector(Comparator<? super OWLObjectProperty> p1) {
        return new OWLObjectPropertyExpressionSelector(p1);
    }

    @Bean
    OWLDataPropertyExpressionSelector dataPropertyExpressionSelector(Comparator<? super OWLDataProperty> p1) {
        return new OWLDataPropertyExpressionSelector(p1);
    }

    @Bean
    OWLIndividualSelector individualSelector(Comparator<? super OWLNamedIndividual> p1) {
        return new OWLIndividualSelector(p1);
    }

    @Bean
    SWRLAtomSelector swrlAtomSelector(Comparator<? super SWRLAtom> p1) {
        return new SWRLAtomSelector(p1);
    }

    @Bean
    OWLObjectComparatorImpl classComparator(RenderingManager p1) {
        return new OWLObjectComparatorImpl(p1);
    }


    @Bean
    @Primary
    AnnotationPropertyComparatorImpl annotationPropertyComparator(ShortFormProvider p1, IRIOrdinalProvider p2) {
        return new AnnotationPropertyComparatorImpl(p1, p2);
    }

    @Bean
    OntologyChangeComparator oWLOntologyChangeRecordComparator(AxiomComparatorImpl p1,
                                                               Comparator<? super OWLAnnotation> p2) {
        return new OntologyChangeComparator(p1, p2);
    }

    @Bean
    PropertyValueComparator propertyValueComparator(Comparator<? super OWLAnnotationProperty> p1, HasLang p2) {
        return new PropertyValueComparator(p1, p2);
    }

    @Bean
    AxiomSubjectProvider axiomSubjectProvider(OWLObjectSelector<OWLClassExpression> p1,
                                              OWLObjectSelector<OWLObjectPropertyExpression> p2,
                                              OWLObjectSelector<OWLDataPropertyExpression> p3,
                                              OWLObjectSelector<OWLIndividual> p4, OWLObjectSelector<SWRLAtom> p5) {
        return new AxiomSubjectProvider(p1, p2, p3, p4, p5);
    }

    @Bean
    AxiomBySubjectComparator axiomBySubjectComparator(AxiomSubjectProvider p1, OWLObjectComparatorImpl p2) {
        return new AxiomBySubjectComparator(p1, p2);
    }

    @Bean
    AxiomByTypeComparator axiomByTypeComparator() {
        return new AxiomByTypeComparator(DefaultAxiomTypeOrdering.get());
    }

    @Bean
    AxiomByRenderingComparator axiomByRenderingComparator(OWLObjectRenderer p1) {
        return new AxiomByRenderingComparator(p1);
    }

    @Bean
    AxiomComparatorImpl axiomComparator(AxiomBySubjectComparator p1,
                                        AxiomByTypeComparator p2,
                                        AxiomByRenderingComparator p3) {
        return new AxiomComparatorImpl(p1, p2, p3);
    }

    @Bean
    BrowserTextChangedEventComputer browserTextChangedEventComputer(ProjectId p1,
                                                                    DictionaryManager p2,
                                                                    HasGetChangeSubjects p3,
                                                                    HasContainsEntityInSignature p4) {
        return new BrowserTextChangedEventComputer(p1, p2, p3, p4);
    }

    @Bean
    HighLevelEventGenerator highLevelEventGenerator(ProjectId p1,
                                                    RenderingManager p2,
                                                    EntitiesInProjectSignatureByIriIndex p3,
                                                    HasGetRevisionSummary p4,
                                                    EntitiesInProjectSignatureIndex p5) {
        return new HighLevelEventGenerator(p1, p2, p3, p4, p5);
    }

    @Bean
    CriteriaBasedTagsManager criteriaBasedTagsManager(TagRepository p1, MatchingEngine p2, ProjectId projectId) {
        return new CriteriaBasedTagsManager(p1, p2, projectId);
    }

    @Bean
    TagsManager tagsManager(ProjectId p1,
                            EntityTagsRepository p2,
                            CriteriaBasedTagsManager p3,
                            TagRepository p4,
                            HasPostEvents<ProjectEvent<?>> p5) {
        return new TagsManager(p1, p2, p3, p4, p5);
    }

    @Bean
    EntityNodeRenderer entityNodeRenderer(ProjectId p1,
                                          DictionaryManager p2,
                                          DeprecatedEntityChecker p3,
                                          WatchManager p4,
                                          EntityDiscussionThreadRepository p5,
                                          TagsManager p6,
                                          LanguageManager p7) {
        return new EntityNodeRenderer(p1, p2, p3, p4, p5, p6, p7);
    }

    @Bean
    GraphNodeRenderer graphNodeRenderer(EntityNodeRenderer p1) {
        return new GraphNodeRenderer(p1);
    }

    @Bean
    EntityHierarchyChangedEventProxyFactory entityHierarchyChangedEventProxyFactory(GraphNodeRenderer p1,
                                                                                    EntityNodeRenderer p2,
                                                                                    ProjectId p3) {
        return new EntityHierarchyChangedEventProxyFactory(p1, p2, p3);
    }

    @Bean
    OWLClassHierarchyChangeComputer owlClassHierarchyChangeComputer(ProjectId p1,
                                                                    ClassHierarchyProvider p2,
                                                                    EntityNodeRenderer p3,
                                                                    EntityHierarchyChangedEventProxyFactory p4) {
        return new OWLClassHierarchyChangeComputer(p1, p2, p3, p4);
    }

    @Bean
    OWLObjectPropertyHierarchyChangeComputer owlObjectPropertyHierarchyChangeComputer(ProjectId p1,
                                                                                      ObjectPropertyHierarchyProvider p2,
                                                                                      EntityNodeRenderer p3) {
        return new OWLObjectPropertyHierarchyChangeComputer(p1, p2, p3);
    }

    @Bean
    OWLDataPropertyHierarchyChangeComputer owlDataPropertyHierarchyChangeComputer(ProjectId p1,
                                                                                  DataPropertyHierarchyProvider p2,
                                                                                  EntityNodeRenderer p3) {
        return new OWLDataPropertyHierarchyChangeComputer(p1, p2, p3);
    }

    @Bean
    OWLAnnotationPropertyHierarchyChangeComputer owlAnnotationPropertyHierarchyChangeComputer(ProjectId p1,
                                                                                              AnnotationPropertyHierarchyProvider p2,
                                                                                              EntityNodeRenderer p3) {
        return new OWLAnnotationPropertyHierarchyChangeComputer(p1, p2, p3);
    }

    @Bean
    EntityDeprecatedChangedEventTranslator entityDeprecatedChangedEventTranslator(ProjectId p1,
                                                                                  DeprecatedEntityChecker p2,
                                                                                  EntitiesInProjectSignatureByIriIndex p3) {
        return new EntityDeprecatedChangedEventTranslator(p1, p2, p3);
    }

    @Bean
    EntityTagsChangedEventComputer entityTagsChangedEventComputer(ProjectId p1,
                                                                  OntologyChangeSubjectProvider p2,
                                                                  TagsManager p3) {
        return new EntityTagsChangedEventComputer(p1, p2, p3);
    }

    @Bean
    public Set<EventTranslator> eventTranslators(BrowserTextChangedEventComputer c0,
                                                 HighLevelEventGenerator c1,
                                                 OWLClassHierarchyChangeComputer c2,
                                                 OWLObjectPropertyHierarchyChangeComputer c3,
                                                 OWLDataPropertyHierarchyChangeComputer c4,
                                                 OWLAnnotationPropertyHierarchyChangeComputer c5,
                                                 EntityDeprecatedChangedEventTranslator c6,
                                                 EntityTagsChangedEventComputer c7) {
        return ImmutableSet.of(c0, c1, c2, c3, c4, c5, c6, c7);
    }

    @Bean
    @Primary
    HasContainsEntityInSignatureImpl hasContainsEntityInSignature(EntitiesInProjectSignatureIndex p1) {
        return new HasContainsEntityInSignatureImpl(p1);
    }

    @Bean
    List<AxiomType<?>> axiomTypeList() {
        return DefaultAxiomTypeOrdering.get();
    }

    @Bean
    StructuralPropertyValueSubsumptionChecker propertyValueSubsumptionChecker(HasHasAncestor<OWLClass, OWLClass> p1,
                                                                              HasHasAncestor<OWLObjectProperty, OWLObjectProperty> p2,
                                                                              HasHasAncestor<OWLDataProperty, OWLDataProperty> p3,
                                                                              HasHasAncestor<OWLNamedIndividual, OWLClass> p4) {
        return new StructuralPropertyValueSubsumptionChecker(p1, p2, p3, p4);
    }

    @Bean
    ClassClassAncestorChecker classClassHasAncestor(ClassHierarchyProvider p1) {
        return new ClassClassAncestorChecker(p1);
    }

    @Bean
    ObjectPropertyObjectPropertyAncestorChecker objectPropertyObjectPropertyHasAncestor(ObjectPropertyHierarchyProvider p1) {
        return new ObjectPropertyObjectPropertyAncestorChecker(p1);
    }


    @Bean
    DataPropertyDataPropertyAncestorChecker dataPropertyDataPropertyHasAncestor(HierarchyProvider<OWLDataProperty> p1) {
        return new DataPropertyDataPropertyAncestorChecker(p1);
    }

    @Bean
    NamedIndividualClassAncestorChecker namedIndividualClassHasAncestor(HasHasAncestor<OWLClass, OWLClass> p1,
                                                                        ClassAssertionAxiomsByIndividualIndex p2,
                                                                        ProjectOntologiesIndex p3) {
        return new NamedIndividualClassAncestorChecker(p1, p2, p3);
    }

    @Bean
    OBONamespaceCacheFactory oboNamespaceCacheFactory(OntologyAnnotationsIndex p1,
                                                      DefaultOntologyIdManager p2,
                                                      AxiomsByEntityReferenceIndex p3,
                                                      ProjectOntologiesIndex p4,
                                                      OWLDataFactory p5) {
        return new OBONamespaceCacheFactory(p1, p2, p3, p4, p5);
    }

    @Bean
    OBONamespaceCache oboNamespaceCache(OBONamespaceCacheFactory factory) {
        var namespaceCache = factory.create();
//        namespaceCache.rebuildNamespaceCache();
        return namespaceCache;
    }

    @Bean
    MatchingEngineImpl matchingEngine(ProjectSignatureIndex p1, MatcherFactory p2) {
        return new MatchingEngineImpl(p1, p2);
    }

    @Bean
    HierarchyPositionMatchingEngineImpl hierarchyPositionMatchingEngine(ClassHierarchyProvider p1,
                                                                        IndividualsByTypeIndex p2,
                                                                        ProjectSignatureIndex p3) {
        return new HierarchyPositionMatchingEngineImpl(p1, p2, p3);
    }

    @Bean
    ProjectDisposablesManager projectDisposablesManager(DisposableObjectManager disposableObjectManager) {
        return new ProjectDisposablesManager(disposableObjectManager);
    }

    @Bean
    StringFormatterLiteralRendererImpl literalRenderer(ShortFormProvider p1, LiteralLexicalFormTransformer p2) {
        return new StringFormatterLiteralRendererImpl(p1, p2);
    }

    @Bean
    LiteralLexicalFormTransformer literalLexicalFormTransformer() {
        return lexicalForm -> lexicalForm;
    }

    @Bean
    LiteralLangTagTransformer langTagTransformer() {
        return langTag -> langTag;
    }

    @Bean
    UuidSuffixKit uuidSuffixKit() {
        return new UuidSuffixKit();
    }

    @Bean
    public UuidEntityCrudKitPlugin uuidPlugin(UuidSuffixKit p1, UuidEntityCrudKitHandlerFactory p2) {
        return new UuidEntityCrudKitPlugin(p1, p2);
    }

    @Bean
    OBOIdSuffixKit oboIdSuffixKit() {
        return new OBOIdSuffixKit();
    }

    @Bean
    HierarchyPositionCriteriaMatchableEntityTypesExtractor hierarchyPositionCriteriaMatchableEntityTypesExtractor() {
        return new HierarchyPositionCriteriaMatchableEntityTypesExtractor();
    }

    @Bean
    EntityIriPrefixResolver entityIriPrefixResolver(MatcherFactory p1,
                                                    EntityIriPrefixCriteriaRewriter p2,
                                                    HierarchyPositionCriteriaMatchableEntityTypesExtractor p3) {
        return new EntityIriPrefixResolver(p1, p2, p3);
    }

    @Bean
    OBOIdSuffixEntityCrudKitHandlerFactory oboIdSuffixEntityCrudKitHandlerFactory(OWLDataFactory p1,
                                                                                  EntitiesInProjectSignatureByIriIndex p2,
                                                                                  EntityIriPrefixResolver p3) {
        return new OBOIdSuffixEntityCrudKitHandlerFactory(p1, p2, p3);
    }

    @Bean
    public OBOIdSuffixEntityCrudKitPlugin oboIdPlugin(OBOIdSuffixKit p1, OBOIdSuffixEntityCrudKitHandlerFactory p2) {
        return new OBOIdSuffixEntityCrudKitPlugin(p1, p2);
    }

    @Bean
    SuppliedNameSuffixKit suppliedNameSuffixKit() {
        return new SuppliedNameSuffixKit();
    }

    @Bean
    SuppliedNameSuffixEntityCrudKitHandlerFactory suppliedNameSuffixEntityCrudKitHandlerFactory(OWLDataFactory p1,
                                                                                                EntityIriPrefixResolver p2) {
        return new SuppliedNameSuffixEntityCrudKitHandlerFactory(p1, p2);
    }

    @Bean
    public SuppliedNameSuffixEntityCrudKitPlugin suppliedNamePlugin(SuppliedNameSuffixKit p1,
                                                                    SuppliedNameSuffixEntityCrudKitHandlerFactory p2) {
        return new SuppliedNameSuffixEntityCrudKitPlugin(p1, p2);
    }


    @Bean
    @ProjectSingleton
    public ProjectEntityCrudKitSettingsRepository projectEntityCrudKitSettingsRepository(MongoTemplate p1,
                                                                                         ObjectMapper p2) {
        return new ProjectEntityCrudKitSettingsRepository(p1, p2);
    }

    @Bean
    XRefConverter xRefConverter() {
        return new XRefConverter();
    }

    @Bean
    XRefExtractor xRefExtractor(AnnotationToXRefConverter p1, ProjectAnnotationAssertionAxiomsBySubjectIndex p2) {
        return new XRefExtractor(p1, p2);
    }

    @Bean
    AnnotationToXRefConverter annotationToXRefConverter(XRefConverter p1, OWLDataFactory p2) {
        return new AnnotationToXRefConverter(p1, p2);
    }

    @Bean
    MessageFormatter messageFormatter(RenderingManager p1) {
        return new MessageFormatter(p1);
    }

    @Bean
    TermDefinitionManagerImpl termDefinitionManager(OWLDataFactory p1,
                                                    AnnotationToXRefConverter p2,
                                                    ChangeManager p3,
                                                    XRefExtractor p4,
                                                    RenderingManager p5,
                                                    ProjectOntologiesIndex p6,
                                                    AnnotationAssertionAxiomsBySubjectIndex p7,
                                                    DefaultOntologyIdManager p8,
                                                    MessageFormatter p9) {
        return new TermDefinitionManagerImpl(p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }

    @Bean
    @Primary
    AnnotationsSectionRenderer annotationsSectionRenderer(AnnotationAssertionAxiomsBySubjectIndex p1) {
        return new AnnotationsSectionRenderer(p1);
    }

    @SuppressWarnings("unchecked")
    @Bean
    AnnotationsSectionRenderer<OWLClass> annotationsSectionRendererOwlClass(AnnotationsSectionRenderer impl) {
        return impl;
    }

    @SuppressWarnings("unchecked")
    @Bean
    AnnotationsSectionRenderer<OWLObjectProperty> annotationsSectionRendererOwlObjectProperty(AnnotationsSectionRenderer impl) {
        return impl;
    }

    @SuppressWarnings("unchecked")
    @Bean
    AnnotationsSectionRenderer<OWLDataProperty> annotationsSectionRendererOwlDataProperty(AnnotationsSectionRenderer impl) {
        return impl;
    }

    @SuppressWarnings("unchecked")
    @Bean
    AnnotationsSectionRenderer<OWLAnnotationProperty> annotationsSectionRendererOwlAnnotationProperty(
            AnnotationsSectionRenderer impl) {
        return impl;
    }

    @SuppressWarnings("unchecked")
    @Bean
    AnnotationsSectionRenderer<OWLNamedIndividual> annotationsSectionRendererOwlNamedIndividual(
            AnnotationsSectionRenderer impl) {
        return impl;
    }

    @SuppressWarnings("unchecked")
    @Bean
    AnnotationsSectionRenderer<OWLDatatype> annotationsSectionRendererOwlDatatype(AnnotationsSectionRenderer impl) {
        return impl;
    }

    @Bean
    IRIOrdinalProvider iRIIndexProvider() {
        return IRIOrdinalProvider.withDefaultAnnotationPropertyOrdering();
    }

    @Bean
    FrameComponentRendererImpl frameComponentRenderer(RenderingManager p1,
                                                      EntitiesInProjectSignatureByIriIndex p2,
                                                      EntitiesInProjectSignatureByIriIndex p3) {
        return new FrameComponentRendererImpl(p1, p2, p3);
    }

    @Bean
    PropertyValueMinimiser propertyValueMinimiser(PropertyValueSubsumptionChecker p1) {
        return new PropertyValueMinimiser(p1);
    }

    @Bean
    ClassExpression2PropertyValuesTranslator classExpression2PropertyValuesTranslator() {
        return new ClassExpression2PropertyValuesTranslator();
    }

    @Bean
    SubClassOfAxiom2PropertyValuesTranslator subClassOfAxiom2PropertyValuesTranslator(
            ClassExpression2PropertyValuesTranslator p1) {
        return new SubClassOfAxiom2PropertyValuesTranslator(p1);
    }

    @Bean
    EquivalentClassesAxiom2PropertyValuesTranslator equivalentClassesAxiom2PropertyValuesTranslator(
            ClassExpression2PropertyValuesTranslator p1) {
        return new EquivalentClassesAxiom2PropertyValuesTranslator(p1);
    }

    @Bean
    ClassAssertionAxiom2PropertyValuesTranslator classAssertionAxiom2PropertyValuesTranslator(
            ClassExpression2PropertyValuesTranslator p1) {
        return new ClassAssertionAxiom2PropertyValuesTranslator(p1);
    }

    @Bean
    ObjectPropertyAssertionAxiom2PropertyValuesTranslator objectPropertyAssertionAxiom2PropertyValuesTranslator() {
        return new ObjectPropertyAssertionAxiom2PropertyValuesTranslator();
    }

    @Bean
    DataPropertyAssertionAxiom2PropertyValuesTranslator dataPropertyAssertionAxiom2PropertyValuesTranslator() {
        return new DataPropertyAssertionAxiom2PropertyValuesTranslator();
    }

    @Bean
    Annotation2PropertyValueTranslator annotation2PropertyValueTranslator() {
        return new Annotation2PropertyValueTranslator();
    }


    @Bean
    AnnotationAssertionAxiom2PropertyValuesTranslator annotationAssertionAxiom2PropertyValuesTranslator(
            Annotation2PropertyValueTranslator p1) {
        return new AnnotationAssertionAxiom2PropertyValuesTranslator(p1);
    }

    @Bean
    AxiomTranslatorFactory axiomTranslatorFactory(SubClassOfAxiom2PropertyValuesTranslator p1,
                                                  EquivalentClassesAxiom2PropertyValuesTranslator p2,
                                                  ClassAssertionAxiom2PropertyValuesTranslator p3,
                                                  ObjectPropertyAssertionAxiom2PropertyValuesTranslator p4,
                                                  DataPropertyAssertionAxiom2PropertyValuesTranslator p5,
                                                  AnnotationAssertionAxiom2PropertyValuesTranslator p6) {
        return new AxiomTranslatorFactory(p1, p2, p3, p4, p5, p6);
    }

    @Bean
    AxiomPropertyValueTranslator axiomPropertyValueTranslator(AxiomTranslatorFactory p1) {
        return new AxiomPropertyValueTranslator(p1);
    }

    @Bean
    Class2ClassFrameTranslatorFactory class2ClassFrameTranslatorFactory(Provider<ClassFrameAxiomsIndex> p1,
                                                                        Provider<HasGetAncestors<OWLClass>> p2,
                                                                        Provider<PropertyValueMinimiser> p3,
                                                                        Provider<AxiomPropertyValueTranslator> p4,
                                                                        Provider<RelationshipMatcherFactory> p5) {
        return new Class2ClassFrameTranslatorFactory(p1, p2, p3, p4, p5);
    }

    @Bean
    ClassFrameProviderImpl classFrameProvider(Class2ClassFrameTranslatorFactory p1) {
        return new ClassFrameProviderImpl(p1);
    }

    @Bean
    @LuceneIndexesDirectory
    Path luceneIndexesDirectory(DataDirectoryProvider dataDirectoryProvider) {
        var dataDirectory = dataDirectoryProvider.get().toPath();
        return dataDirectory.resolve("lucene-indexes");

    }

    @Bean
    ActiveLanguagesManagerImpl activeLanguagesManager(ProjectId p1,
                                                      AxiomsByEntityReferenceIndex p2,
                                                      ProjectOntologiesIndex p3) {
        return new ActiveLanguagesManagerImpl(p1, p2, p3);
    }

    @Bean
    LanguageManager languageManager(ProjectId p1,
                                    ActiveLanguagesManager p2,
                                    ProjectDetailsRepository p3) {
        return new LanguageManager(p1, p2, p3);
    }

    @Bean
    @Scope("prototype")
    ShortFormCache shortFormCache() {
        return new ShortFormCache();
    }

    @Bean
    BuiltInShortFormDictionary builtInShortFormDictionary(ShortFormCache p1, OWLEntityProvider p2) {
        return new BuiltInShortFormDictionary(p1, p2);
    }

    @Bean
    DictionaryManager dictionaryManager(LanguageManager p1,
                                        MultiLingualDictionary p2,
                                        MultilingualDictionaryUpdater p3,
                                        BuiltInShortFormDictionary p4) {
        return new DictionaryManager(p1, p2, p3, p4);
    }

    @Bean
    BuiltInPrefixDeclarations builtInPrefixDeclarations(BuiltInPrefixDeclarationsLoader p1) {
        return p1.getBuiltInPrefixDeclarations();
    }

    @Bean
    BuiltInPrefixDeclarationsLoader builtInPrefixDeclarationsLoader(OverridableFileFactory p1) {
        return new BuiltInPrefixDeclarationsLoader(p1);
    }

    @Bean
    RevisionNumberProvider revisionNumberProvider(RevisionManager p1) {
        return new RevisionNumberProvider(p1);
    }

    @Bean
    EventManagerProvider eventManagerProvider(ProjectDisposablesManager p1, ProjectId p2) {
        return new EventManagerProvider(p1, p2);
    }

    @Bean
    UuidEntityCrudKitHandlerFactory uuidEntityCrudKitHandlerFactory(OWLDataFactory p1,
                                                                    EntitiesInProjectSignatureByIriIndex p2,
                                                                    EntityIriPrefixResolver p3) {
        return new UuidEntityCrudKitHandlerFactory(p1, p2, p3);
    }

    @Bean
    ProjectActionHandlerRegistry projectActionHandlerRegistry(Set<ProjectActionHandler> actionHandlers) {
        return new ProjectActionHandlerRegistry(actionHandlers);
    }
}
