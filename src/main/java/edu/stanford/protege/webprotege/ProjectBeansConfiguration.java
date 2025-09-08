package edu.stanford.protege.webprotege;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.app.ApplicationHostSupplier;
import edu.stanford.protege.webprotege.app.ApplicationNameSupplier;
import edu.stanford.protege.webprotege.app.PlaceUrl;
import edu.stanford.protege.webprotege.axiom.*;
import edu.stanford.protege.webprotege.axioms.AddAxiomsDelegateHandler;
import edu.stanford.protege.webprotege.axioms.RemoveAxiomsDelegateHandler;
import edu.stanford.protege.webprotege.bulkop.EditAnnotationsChangeListGeneratorFactory;
import edu.stanford.protege.webprotege.bulkop.EditParentsChangeListGeneratorFactory;
import edu.stanford.protege.webprotege.bulkop.MoveClassesChangeListGeneratorFactory;
import edu.stanford.protege.webprotege.bulkop.SetAnnotationValueActionChangeListGeneratorFactory;
import edu.stanford.protege.webprotege.change.HasApplyChanges;
import edu.stanford.protege.webprotege.change.*;
import edu.stanford.protege.webprotege.change.matcher.*;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.crud.*;
import edu.stanford.protege.webprotege.crud.gen.GeneratedAnnotationsGenerator;
import edu.stanford.protege.webprotege.crud.gen.IncrementingPatternDescriptorValueGenerator;
import edu.stanford.protege.webprotege.crud.icatx.*;
import edu.stanford.protege.webprotege.crud.obo.OBOIdSuffixEntityCrudKitHandlerFactory;
import edu.stanford.protege.webprotege.crud.obo.OBOIdSuffixEntityCrudKitPlugin;
import edu.stanford.protege.webprotege.crud.oboid.OboIdSuffixKit;
import edu.stanford.protege.webprotege.crud.persistence.ProjectEntityCrudKitSettingsRepository;
import edu.stanford.protege.webprotege.crud.supplied.SuppliedNameSuffixEntityCrudKitHandlerFactory;
import edu.stanford.protege.webprotege.crud.supplied.SuppliedNameSuffixEntityCrudKitPlugin;
import edu.stanford.protege.webprotege.crud.supplied.SuppliedNameSuffixKit;
import edu.stanford.protege.webprotege.crud.uuid.UuidEntityCrudKitHandlerFactory;
import edu.stanford.protege.webprotege.crud.uuid.UuidEntityCrudKitPlugin;
import edu.stanford.protege.webprotege.crud.uuid.UuidSuffixKit;
import edu.stanford.protege.webprotege.diff.OntologyDiff2OntologyChanges;
import edu.stanford.protege.webprotege.diff.OrderingDiffElementRenderer;
import edu.stanford.protege.webprotege.diff.ProjectOrderedChildren2DiffElementsTranslator;
import edu.stanford.protege.webprotege.diff.Revision2DiffElementsTranslator;
import edu.stanford.protege.webprotege.dispatch.ProjectActionHandler;
import edu.stanford.protege.webprotege.dispatch.impl.ProjectActionHandlerRegistry;
import edu.stanford.protege.webprotege.entity.*;
import edu.stanford.protege.webprotege.events.*;
import edu.stanford.protege.webprotege.filemanager.FileContents;
import edu.stanford.protege.webprotege.forms.*;
import edu.stanford.protege.webprotege.frame.*;
import edu.stanford.protege.webprotege.frame.translator.*;
import edu.stanford.protege.webprotege.hierarchy.*;
import edu.stanford.protege.webprotege.hierarchy.ordering.ProjectOrderedChildrenManager;
import edu.stanford.protege.webprotege.hierarchy.ordering.ProjectOrderedChildrenService;
import edu.stanford.protege.webprotege.hierarchy.ordering.ProjectOrderedChildrenServiceImpl;
import edu.stanford.protege.webprotege.hierarchy.ordering.dtos.UpdateEntityChildrenRequest;
import edu.stanford.protege.webprotege.hierarchy.ordering.dtos.UpdateEntityChildrenResponse;
import edu.stanford.protege.webprotege.icd.*;
import edu.stanford.protege.webprotege.icd.hierarchy.ClassHierarchyRetiredClassDetectorImpl;
import edu.stanford.protege.webprotege.icd.mappers.AncestorHierarchyNodeMapper;
import edu.stanford.protege.webprotege.index.*;
import edu.stanford.protege.webprotege.index.impl.IndexUpdater;
import edu.stanford.protege.webprotege.index.impl.IndexUpdaterFactory;
import edu.stanford.protege.webprotege.index.impl.RootIndexImpl;
import edu.stanford.protege.webprotege.index.impl.UpdatableIndex;
import edu.stanford.protege.webprotege.individuals.CreateIndividualsChangeListGeneratorFactory;
import edu.stanford.protege.webprotege.inject.*;
import edu.stanford.protege.webprotege.inject.project.ProjectDirectoryFactory;
import edu.stanford.protege.webprotege.inject.project.*;
import edu.stanford.protege.webprotege.ipc.CommandExecutor;
import edu.stanford.protege.webprotege.ipc.EventDispatcher;
import edu.stanford.protege.webprotege.ipc.impl.CommandExecutorImpl;
import edu.stanford.protege.webprotege.issues.*;
import edu.stanford.protege.webprotege.issues.mention.MentionParser;
import edu.stanford.protege.webprotege.lang.ActiveLanguagesManager;
import edu.stanford.protege.webprotege.lang.ActiveLanguagesManagerImpl;
import edu.stanford.protege.webprotege.lang.LanguageManager;
import edu.stanford.protege.webprotege.locking.ReadWriteLockService;
import edu.stanford.protege.webprotege.logicaldefinitions.LogicalDefinitionExtractor;
import edu.stanford.protege.webprotege.logicaldefinitions.NecessaryConditionsExtractor;
import edu.stanford.protege.webprotege.logicaldefinitions.UpdateLogicalDefinitionsChangeListGeneratorFactory;
import edu.stanford.protege.webprotege.mail.CommentMessageIdGenerator;
import edu.stanford.protege.webprotege.mail.MessageIdGenerator;
import edu.stanford.protege.webprotege.mail.SendMail;
import edu.stanford.protege.webprotege.mansyntax.ManchesterSyntaxChangeGeneratorFactory;
import edu.stanford.protege.webprotege.mansyntax.ManchesterSyntaxFrameParser;
import edu.stanford.protege.webprotege.mansyntax.OntologyAxiomPairChangeGenerator;
import edu.stanford.protege.webprotege.mansyntax.ShellOntologyChecker;
import edu.stanford.protege.webprotege.mansyntax.render.*;
import edu.stanford.protege.webprotege.match.*;
import edu.stanford.protege.webprotege.merge.*;
import edu.stanford.protege.webprotege.msg.MessageFormatter;
import edu.stanford.protege.webprotege.object.*;
import edu.stanford.protege.webprotege.ontology.ProcessUploadedOntologiesRequest;
import edu.stanford.protege.webprotege.ontology.ProcessUploadedOntologiesResponse;
import edu.stanford.protege.webprotege.owlapi.HasContainsEntityInSignatureImpl;
import edu.stanford.protege.webprotege.owlapi.OWLObjectStringFormatter;
import edu.stanford.protege.webprotege.owlapi.RenameMapFactory;
import edu.stanford.protege.webprotege.owlapi.StringFormatterLiteralRendererImpl;
import edu.stanford.protege.webprotege.project.*;
import edu.stanford.protege.webprotege.project.chg.ChangeManager;
import edu.stanford.protege.webprotege.renderer.LiteralRenderer;
import edu.stanford.protege.webprotege.renderer.*;
import edu.stanford.protege.webprotege.revision.*;
import edu.stanford.protege.webprotege.revision.uiHistoryConcern.NewRevisionsEventEmitterService;
import edu.stanford.protege.webprotege.revision.uiHistoryConcern.NewRevisionsEventEmitterServiceImpl;
import edu.stanford.protege.webprotege.revision.uiHistoryConcern.OrderingChangesManager;
import edu.stanford.protege.webprotege.search.EntitySearcherFactory;
import edu.stanford.protege.webprotege.shortform.*;
import edu.stanford.protege.webprotege.tag.CriteriaBasedTagsManager;
import edu.stanford.protege.webprotege.tag.EntityTagsRepository;
import edu.stanford.protege.webprotege.tag.TagRepository;
import edu.stanford.protege.webprotege.tag.TagsManager;
import edu.stanford.protege.webprotege.templates.TemplateEngine;
import edu.stanford.protege.webprotege.usage.ReferencingAxiomVisitorFactory;
import edu.stanford.protege.webprotege.user.UserDetailsManager;
import edu.stanford.protege.webprotege.util.DisposableObjectManager;
import edu.stanford.protege.webprotege.util.EntityDeleter;
import edu.stanford.protege.webprotege.util.IriReplacerFactory;
import edu.stanford.protege.webprotege.util.ReferenceFinder;
import edu.stanford.protege.webprotege.viz.EdgeMatcherFactory;
import edu.stanford.protege.webprotege.viz.EntityGraphBuilderFactory;
import edu.stanford.protege.webprotege.viz.EntityGraphEdgeLimit;
import edu.stanford.protege.webprotege.watches.*;
import edu.stanford.protege.webprotege.webhook.*;
import org.semanticweb.owlapi.expression.OWLOntologyChecker;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.IRIShortFormProvider;
import org.semanticweb.owlapi.util.OntologyIRIShortFormProvider;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.MongoTemplate;

import jakarta.annotation.Nonnull;
import jakarta.inject.Provider;
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
public class ProjectBeansConfiguration {

    @Bean
    ProjectComponent projectComponent(ProjectId projectId,
                                      RevisionManager revisionManager,
                                      ProjectDisposablesManager projectDisposablesManager,
                                      ProjectActionHandlerRegistry actionHandlerRegistry,
                                      ApplicationContext applicationContext,
                                      EntityFrameFormDataDtoBuilderFactory entityFrameFormDataDtoBuilderFactory) {
        return new ProjectComponentImpl(applicationContext,
                                        projectId,
                                        revisionManager,
                                        projectDisposablesManager,
                                        actionHandlerRegistry,
                                        entityFrameFormDataDtoBuilderFactory);
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
    public Set<OWLClass> classHierarchyRoot(ClassHierarchyRootProvider provider) {
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
    WatchRecordRepositoryImpl watchRecordRepository(MongoTemplate template, ObjectMapper objectMapper) {
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
                                         EventDispatcher p5) {
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
                                      ManchesterSyntaxObjectRenderer manchesterSyntaxObjectRenderer,
                                      EntityStatusManager entityStatusManager) {
        return new RenderingManager(dictionaryManager, deprecatedEntityChecker, manchesterSyntaxObjectRenderer, entityStatusManager);
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
    public IcdReleasedEntityStatusManager getIcdReleasedClassesManager() {
        return new IcdReleasedEntityStatusManagerImpl();
    }

    @Bean
    ReleasedClassesChecker getReleasedClassesIndex(IcdReleasedEntityStatusManager icdReleasedEntityStatusManager) {
        return new ReleasedClassesCheckerImpl(icdReleasedEntityStatusManager);
    }

    @Bean
    RetiredClassChecker getRetiredClassChecker(OWLLiteralExtractorManager owlLiteralExtractorManager) {
        return new RetiredClassCheckerImpl(owlLiteralExtractorManager);
    }

    @Bean
    OWLLiteralExtractorManager getOWLLiteralManager(AnnotationAssertionAxiomsIndex index) {
        return new OWLLiteralExtractorManager(index);
    }

    @Bean
    EntityStatusManager getEntityStatusManager(ReleasedClassesChecker releasedClassesChecker) {
        return new EntityStatusManagerImpl(releasedClassesChecker);
    }

    @Bean
    public RevisionManagerImpl getRevisionSummary(RevisionStore revisionStore) {
        var revisionManager = new RevisionManagerImpl(revisionStore);
        return revisionManager;
    }

    @Bean
    public RevisionStoreImpl revisionStore(ProjectId p1,
                                           ChangeHistoryFileFactory p2,
                                           OWLDataFactory p3,
                                           OntologyChangeRecordTranslator p4) {
        var revisionStore = new RevisionStoreImpl(p1, p2, p3, p4);
        revisionStore.load();
        return revisionStore;
    }

    @Bean
    public RevisionStoreProvider revisionStoreProvider(RevisionStoreImpl revisionStore,
                                                       ProjectDisposablesManager projectDisposablesManager) {
        return new RevisionStoreProvider(revisionStore);
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
    @Scope("prototype")
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
                                                                      ProjectId p2,
                                                                      EntityCrudKitRegistry p3) {
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
    IndexUpdater indexUpdater(IndexUpdaterFactory indexUpdaterFactory) {
        var indexUpdater = indexUpdaterFactory.create();
        indexUpdater.buildIndexes();
        return indexUpdater;
    }

    @Bean
    IndexUpdaterFactory indexUpdaterFactory(ProjectId p1,
                                            RevisionManager p2,
                                            Set<UpdatableIndex> p3,
                                            @IndexUpdatingService ExecutorService p4) {
        return new IndexUpdaterFactory(p1, p2, p3, p4);
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
                                                                OWLDataFactory p4,
                                                                DefaultOntologyIdManager p5) {
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
                                Provider<EventTranslatorManager> p10,
                                ProjectEntityCrudKitHandlerCache p11,
                                RevisionManager p12,
                                RootIndex p13,
                                DictionaryManager p14,
                                ClassHierarchyProvider p15,
                                ObjectPropertyHierarchyProvider p16,
                                DataPropertyHierarchyProvider p17,
                                AnnotationPropertyHierarchyProvider p18,
                                EntityCrudContextFactory p20,
                                RenameMapFactory p21,
                                BuiltInPrefixDeclarations p22,
                                IndexUpdater p23,
                                DefaultOntologyIdManager p24,
                                IriReplacerFactory p25,
                                GeneratedAnnotationsGenerator p26,
                                EventDispatcher p27,
                                NewRevisionsEventEmitterService p28,
                                ProjectRevisionRepository p29,
                                HierarchyProviderManager p30) {
        return new ChangeManager(p1,
                                 p2,
                                 p3,
                                 p4,
                                 p5,
                                 p6,
                                 p7,
                                 p8,
                                 p10,
                                 p11,
                                 p12,
                                 p13,
                                 p14,
                                 p15,
                                 p16,
                                 p17,
                                 p18,
                                 p20,
                                 p21,
                                 p22,
                                 p23,
                                 p24,
                                 p25,
                                 p26,
                                 p27,
                                 p28,
                                 p29,
                                p30);
                    }

    @Bean
    NewRevisionsEventEmitterService newRevisionsEventEmitterService(ProjectChangesManager p1,
                                                                    OrderingChangesManager p2,
                                                                    EventDispatcher p3,
                                                                    ProjectId p4) {
        return new NewRevisionsEventEmitterServiceImpl(p1, p2, p3, p4);
    }

    @Bean
    OrderingDiffElementRenderer orderingDiffElementRenderer(RenderingManager p1) {
        return new OrderingDiffElementRenderer(p1);
    }

    @Bean
    ProjectOrderedChildren2DiffElementsTranslator projectOrderedChildren2DiffElementsTranslator() {
        return new ProjectOrderedChildren2DiffElementsTranslator();
    }

    @Bean
    OrderingChangesManager orderingChangesManager(ProjectOrderedChildren2DiffElementsTranslator p1,
                                                  OrderingDiffElementRenderer p2) {
        return new OrderingChangesManager(p1, p2);
    }

    @Bean
    ClassHierarchyProvider classHierarchyProvider(ProjectId p1,
                                                      @ClassHierarchyRoot Set<OWLClass> p2,
                                                      ProjectOntologiesIndex p3,
                                                      SubClassOfAxiomsBySubClassIndex p4,
                                                      EquivalentClassesAxiomsIndex p5,
                                                      ProjectSignatureByTypeIndex p6,
                                                      EntitiesInProjectSignatureByIriIndex p7,
                                                      ClassHierarchyChildrenAxiomsIndex p8) {
        return new ClassHierarchyProviderImpl(p1, p2, p3, p4, p5, p6, p7, p8);
    }

    @Bean
    ClassHierarchyCycleDetectorImpl classCycleDetectorProvider(@Nonnull ClassHierarchyProvider p1) {
        return new ClassHierarchyCycleDetectorImpl(p1);
    }

    @Bean
    ClassHierarchyRetiredClassDetectorImpl classHierarchyRetiredAcestorDetector(@Nonnull ClassHierarchyProvider p1,
                                                                                @Nonnull RetiredClassChecker p2) {
        return new ClassHierarchyRetiredClassDetectorImpl(p1, p2);
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
    public ReferenceFinder referenceFinder(@Nonnull AxiomsByReferenceIndex axiomsIndex,
                                           @Nonnull OntologyAnnotationsIndex ontologyAnnotationsIndex) {
        return new ReferenceFinder(axiomsIndex, ontologyAnnotationsIndex);
    }

    @Bean
    public EntityDeleter entityDeleter(ReferenceFinder referenceFinder,
                                       ProjectOntologiesIndex projectOntologiesIndex) {
        return new EntityDeleter(referenceFinder, projectOntologiesIndex);
    }

    @Bean
    AxiomSubjectProvider axiomSubjectProvider(OWLObjectSelector<OWLClassExpression> p1,
                                              OWLObjectSelector<OWLObjectPropertyExpression> p2,
                                              OWLObjectSelector<OWLDataPropertyExpression> p3,
                                              OWLObjectSelector<OWLIndividual> p4,
                                              OWLObjectSelector<SWRLAtom> p5) {
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
    @Scope("prototype")
    BrowserTextChangedEventComputer browserTextChangedEventComputer(ProjectId p1,
                                                                    DictionaryManager p2,
                                                                    HasGetChangeSubjects p3,
                                                                    HasContainsEntityInSignature p4) {
        return new BrowserTextChangedEventComputer(p1, p2, p3, p4);
    }

    @Bean
    @Scope("prototype")
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
                            EventDispatcher p5) {
        return new TagsManager(p1, p2, p3, p4, p5);
    }

    @Bean
    EntityNodeRenderer entityNodeRenderer(ProjectId p1,
                                          DictionaryManager p2,
                                          DeprecatedEntityChecker p3,
                                          WatchManager p4,
                                          EntityDiscussionThreadRepository p5,
                                          TagsManager p6,
                                          LanguageManager p7,
                                          EntityStatusManager p8) {
        return new EntityNodeRenderer(p1, p2, p3, p4, p5, p6, p7, p8);
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
    @Scope("prototype")
    EntityDeprecatedChangedEventTranslator entityDeprecatedChangedEventTranslator(ProjectId p1,
                                                                                  DeprecatedEntityChecker p2,
                                                                                  EntitiesInProjectSignatureByIriIndex p3) {
        return new EntityDeprecatedChangedEventTranslator(p1, p2, p3);
    }

    @Bean
    @Scope("prototype")
    EntityTagsChangedEventComputer entityTagsChangedEventComputer(ProjectId p1,
                                                                  OntologyChangeSubjectProvider p2,
                                                                  TagsManager p3) {
        return new EntityTagsChangedEventComputer(p1, p2, p3);
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
    MatchingEngineImpl matchingEngine(ProjectSignatureIndex p1, MatcherFactory p2) {
        return new MatchingEngineImpl(p1, p2);
    }

    @Bean
    HierarchyPositionMatchingEngineImpl hierarchyPositionMatchingEngine(ClassHierarchyProvider p1,
                                                                        IndividualsByTypeIndex p2,
                                                                        ProjectSignatureIndex p3) {
        return new HierarchyPositionMatchingEngineImpl(p1, p2, p3);
    }

    @Scope("prototype")
    @Bean
    ManagedHierarchiesChangedComputer managedHierarchiesChangedComputer(ProjectId p1, NamedHierarchyManager p2, HierarchyProviderManager p3, HierarchyChangesComputerFactory p4) {
        return new ManagedHierarchiesChangedComputer(p1, p2, p3, p4);
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
    OboIdSuffixKit OboIdSuffixKit() {
        return new OboIdSuffixKit();
    }

    @Bean
    IcatxGenerationSuffixKit icatxGenerationSuffixKit() {
        return new IcatxGenerationSuffixKit();
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
    IcatxSuffixEntityCrudKitHandlerFactory icatxSuffixEntityCrudKitHandlerFactory(EntityIriPrefixResolver p1, OWLDataFactory p2, CommandExecutor<GetUniqueIdRequest, GetUniqueIdResponse> p3) {
        return new IcatxSuffixEntityCrudKitHandlerFactory(p1, p2, p3);
    }

    @Bean
    public OBOIdSuffixEntityCrudKitPlugin oboIdPlugin(OboIdSuffixKit p1, OBOIdSuffixEntityCrudKitHandlerFactory p2) {
        return new OBOIdSuffixEntityCrudKitPlugin(p1, p2);
    }

    @Bean
    public IcatxEntityCrudKitPlugin icatxEntityCrudKitPlugin(IcatxGenerationSuffixKit p1,
                                                             IcatxSuffixEntityCrudKitHandlerFactory p2) {
        return new IcatxEntityCrudKitPlugin(p1, p2);
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
    MessageFormatter messageFormatter(RenderingManager p1) {
        return new MessageFormatter(p1);
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
    @Primary
    FrameComponentRendererImpl frameComponentRenderer(RenderingManager p1,
                                                      EntitiesInProjectSignatureByIriIndex p2,
                                                      EntitiesInProjectSignatureByIriIndex p3) {
        return new FrameComponentRendererImpl(p1, p2, p3);
    }

    @Bean
    @Scope("prototype")
    FrameComponentSessionRenderer frameComponentSessionRenderer(FrameComponentRenderer p1) {
        return new FrameComponentSessionRenderer(p1);
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
    ProjectBackupDirectoryProvider projectBackupDirectoryProvider(ProjectId projectId){
        return new ProjectBackupDirectoryProvider(projectId);
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
    LanguageManager languageManager(ProjectId p1, ActiveLanguagesManager p2, ProjectDetailsRepository p3) {
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
    AddAxiomsDelegateHandler addAxiomsActionHandler2(AccessManager accessManager,
                                                     ChangeManager changeManager,
                                                     DefaultOntologyIdManager defaultOntologyIdManager) {
        return new AddAxiomsDelegateHandler(accessManager,
                                            changeManager,
                                            defaultOntologyIdManager);
    }

    @Bean
    RemoveAxiomsDelegateHandler removeAxiomsDelegateHandler(AccessManager accessManager,
                                                        ChangeManager changeManager,
                                                        DefaultOntologyIdManager defaultOntologyIdManager) {
        return new RemoveAxiomsDelegateHandler(accessManager,
                                            changeManager,
                                            defaultOntologyIdManager);
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
    UuidEntityCrudKitHandlerFactory uuidEntityCrudKitHandlerFactory(OWLDataFactory p1,
                                                                    EntitiesInProjectSignatureByIriIndex p2,
                                                                    EntityIriPrefixResolver p3) {
        return new UuidEntityCrudKitHandlerFactory(p1, p2, p3);
    }

    @Bean
    PlainFrameRenderer plainFrameRenderer(FrameComponentRenderer p1, Comparator<? super PropertyValue> p2) {
        return new PlainFrameRenderer(p1, p2);
    }

    @Bean
    ProjectActionHandlerRegistry projectActionHandlerRegistry(Set<ProjectActionHandler<?,?>> actionHandlers) {
        return new ProjectActionHandlerRegistry(actionHandlers);
    }

    @Bean
    FrameChangeGeneratorFactory frameChangeGeneratorFactory(ProjectOntologiesIndex p1,
                                                            ReverseEngineeredChangeDescriptionGeneratorFactory p2,
                                                            DefaultOntologyIdManager p3,
                                                            OntologyAxiomsIndex p4,
                                                            ObjectPropertyFrameTranslator p5,
                                                            DataPropertyFrameTranslator p6,
                                                            AnnotationPropertyFrameTranslator p7,
                                                            NamedIndividualFrameTranslator p8,
                                                            RenderingManager p9,
                                                            ClassFrameProvider p10,
                                                            ClassFrame2FrameAxiomsTranslator p11) {
        return new FrameChangeGeneratorFactory(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11);
    }

    @Bean
    PropertyValue2AxiomTranslator propertyValue2AxiomTranslator() {
        return new PropertyValue2AxiomTranslator();
    }

    @Bean
    ClassFrame2FrameAxiomsTranslator classFrame2FrameAxiomsTranslator(OWLDataFactory p1,
                                                                      PropertyValue2AxiomTranslator p2) {
        return new ClassFrame2FrameAxiomsTranslator(p1, p2);
    }

    @Bean
    NamedIndividualFrameTranslator namedIndividualFrameTranslator(NamedIndividualFrameAxiomIndex p1,
                                                                  PropertyValueMinimiser p2,
                                                                  ClassFrameProvider p3,
                                                                  Provider<AxiomPropertyValueTranslator> p4,
                                                                  PropertyValue2AxiomTranslator p5,
                                                                  OWLDataFactory p6) {
        return new NamedIndividualFrameTranslator(p1, p2, p3, p4, p5, p6);
    }

    @Bean
    ObjectPropertyFrameTranslator objectPropertyFrameTranslator(ProjectOntologiesIndex p1,
                                                                AnnotationAssertionAxiomsBySubjectIndex p2,
                                                                ObjectPropertyDomainAxiomsIndex p3,
                                                                ObjectPropertyRangeAxiomsIndex p4,
                                                                ObjectPropertyCharacteristicsIndex p5,
                                                                Provider<AxiomPropertyValueTranslator> p6,
                                                                PropertyValue2AxiomTranslator p7) {
        return new ObjectPropertyFrameTranslator(p1, p2, p3, p4, p5, p6, p7);
    }

    @Bean
    DataPropertyFrameTranslator dataPropertyFrameTranslator(ProjectOntologiesIndex p1,
                                                            AnnotationAssertionAxiomsBySubjectIndex p2,
                                                            DataPropertyDomainAxiomsIndex p3,
                                                            DataPropertyRangeAxiomsIndex p4,
                                                            DataPropertyCharacteristicsIndex p5,
                                                            Provider<AxiomPropertyValueTranslator> p6,
                                                            PropertyValue2AxiomTranslator p7) {
        return new DataPropertyFrameTranslator(p1, p2, p3, p4, p5, p6, p7);
    }

    @Bean
    AnnotationPropertyFrameTranslator annotationPropertyFrameTranslator(ProjectOntologiesIndex p1,
                                                                        AnnotationAssertionAxiomsBySubjectIndex p2,
                                                                        AnnotationPropertyDomainAxiomsIndex p3,
                                                                        AnnotationPropertyRangeAxiomsIndex p4) {
        return new AnnotationPropertyFrameTranslator(p1, p2, p3, p4);
    }

    @Bean
    ReverseEngineeredChangeDescriptionGeneratorFactory reverseEngineeredChangeDescriptionGeneratorFactory(Set<ChangeMatcher> p1,
                                                                                                          OWLObjectStringFormatter p2) {
        return new ReverseEngineeredChangeDescriptionGeneratorFactory(p1, p2);
    }

    @Bean
    FrameComponentSessionRendererFactory frameComponentSessionRendererFactory(FrameComponentRenderer p1) {
        return new FrameComponentSessionRendererFactory(p1);
    }

    @Bean
    @Scope("prototype")
    ProjectOntologiesBuilder projectOntologiesBuilder(ProjectOntologiesIndex p1,
                                                      OntologyAnnotationsIndex p2,
                                                      OntologyAxiomsIndex p3) {
        return new ProjectOntologiesBuilder(p1, p2, p3);
    }

    @Bean
    @Scope("prototype")
    ContextRenderer contextRenderer(RenderingManager p1) {
        return new ContextRenderer(p1);
    }

    @Bean
    @Scope("prototype")
    CreateClassesChangeGeneratorFactory createClassesChangeGeneratorFactory(Provider<OWLDataFactory> p1,
                                                                            Provider<MessageFormatter> p2,
                                                                            Provider<DefaultOntologyIdManager> p3) {
        return new CreateClassesChangeGeneratorFactory(p1, p2, p3);
    }

    @Bean
    @Scope("prototype")
    CreateObjectPropertiesChangeGeneratorFactory createObjectPropertiesChangeGeneratorFactory(Provider<OWLDataFactory> p1,
                                                                                              Provider<MessageFormatter> p2,
                                                                                              Provider<DefaultOntologyIdManager> p3) {
        return new CreateObjectPropertiesChangeGeneratorFactory(p1, p2, p3);
    }

    @Bean
    @Scope("prototype")
    CreateDataPropertiesChangeGeneratorFactory createDataPropertiesChangeGeneratorFactory(Provider<OWLDataFactory> p1,
                                                                                          Provider<MessageFormatter> p2,
                                                                                          Provider<DefaultOntologyIdManager> p3) {
        return new CreateDataPropertiesChangeGeneratorFactory(p1, p2, p3);
    }

    @Bean
    @Scope("prototype")
    CreateAnnotationPropertiesChangeGeneratorFactory createAnnotationPropertiesChangeGeneratorFactory(Provider<OWLDataFactory> p1,
                                                                                                      Provider<MessageFormatter> p2,
                                                                                                      Provider<DefaultOntologyIdManager> p3) {
        return new CreateAnnotationPropertiesChangeGeneratorFactory(p1, p2, p3);
    }

    @Bean
    @Scope("prototype")
    CreateIndividualsChangeListGeneratorFactory createIndividualsChangeListGeneratorFactory(Provider<OWLDataFactory> p1,
                                                                                            Provider<MessageFormatter> p2,
                                                                                            Provider<DefaultOntologyIdManager> p3) {
        return new CreateIndividualsChangeListGeneratorFactory(p1, p2, p3);
    }

    @Bean
    ReferencingAxiomVisitorFactory referencingAxiomVisitorFactory(RenderingManager p1,
                                                                  EntitiesInProjectSignatureByIriIndex p2) {
        return new ReferencingAxiomVisitorFactory(p1, p2);
    }

    @Bean
    FindAndReplaceIRIPrefixChangeGeneratorFactory findAndReplaceIRIPrefixChangeGeneratorFactory(Provider<ProjectSignatureIndex> p1,
                                                                                                Provider<EntityRenamer> p2) {
        return new FindAndReplaceIRIPrefixChangeGeneratorFactory(p1, p2);
    }

    @Bean
    OwlOntologyFacadeFactory owlOntologyFacadeFactory(OntologyAnnotationsIndex p1,
                                                      OntologySignatureIndex p2,
                                                      OntologyAxiomsIndex p3,
                                                      EntitiesInOntologySignatureByIriIndex p4,
                                                      AnnotationAssertionAxiomsBySubjectIndex p5,
                                                      OWLDataFactory p6,
                                                      SubAnnotationPropertyAxiomsBySubPropertyIndex p7,
                                                      AnnotationPropertyDomainAxiomsIndex p8,
                                                      AnnotationPropertyRangeAxiomsIndex p9,
                                                      SubClassOfAxiomsBySubClassIndex p10,
                                                      AxiomsByReferenceIndex p11,
                                                      EquivalentClassesAxiomsIndex p12,
                                                      DisjointClassesAxiomsIndex p13,
                                                      AxiomsByTypeIndex p14,
                                                      SubObjectPropertyAxiomsBySubPropertyIndex p15,
                                                      ObjectPropertyDomainAxiomsIndex p16,
                                                      ObjectPropertyRangeAxiomsIndex p17,
                                                      InverseObjectPropertyAxiomsIndex p18,
                                                      EquivalentObjectPropertiesAxiomsIndex p19,
                                                      DisjointObjectPropertiesAxiomsIndex p20,
                                                      SubDataPropertyAxiomsBySubPropertyIndex p21,
                                                      DataPropertyDomainAxiomsIndex p22,
                                                      DataPropertyRangeAxiomsIndex p23,
                                                      EquivalentDataPropertiesAxiomsIndex p24,
                                                      DisjointDataPropertiesAxiomsIndex p25,
                                                      ClassAssertionAxiomsByIndividualIndex p26,
                                                      ClassAssertionAxiomsByClassIndex p27,
                                                      DataPropertyAssertionAxiomsBySubjectIndex p28,
                                                      ObjectPropertyAssertionAxiomsBySubjectIndex p29,
                                                      SameIndividualAxiomsIndex p30,
                                                      DifferentIndividualsAxiomsIndex p31) {
        return new OwlOntologyFacadeFactory(p1,
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
                                            p26,
                                            p27,
                                            p28,
                                            p29,
                                            p30,
                                            p31);
    }

    @Bean
    ManchesterSyntaxChangeGeneratorFactory manchesterSyntaxChangeGeneratorFactory(ManchesterSyntaxFrameParser p1,
                                                                                  OntologyAxiomPairChangeGenerator p2,
                                                                                  ReverseEngineeredChangeDescriptionGeneratorFactory p3) {
        return new ManchesterSyntaxChangeGeneratorFactory(p1, p2, p3);
    }

    @Bean
    @Scope("prototype")
    ManchesterSyntaxFrameParser manchesterSyntaxFrameParser(OWLOntologyChecker p1,
                                                            OWLDataFactory p2,
                                                            DictionaryManager p3,
                                                            DefaultOntologyIdManager p4) {
        return new ManchesterSyntaxFrameParser(p1, p2, p3, p4);
    }

    @Bean
    @Scope("prototype")
    OntologyAxiomPairChangeGenerator ontologyAxiomPairChangeGenerator() {
        return new OntologyAxiomPairChangeGenerator();
    }

    @Bean
    ModifiedProjectOntologiesCalculatorFactory modifiedProjectOntologiesCalculatorFactory(OntologyDiffCalculator p1) {
        return new ModifiedProjectOntologiesCalculatorFactory(p1);
    }

    @Bean
    @Scope("prototype")
    OntologyPatcher ontologyPatcher(HasApplyChanges p1, OntologyDiff2OntologyChanges p2) {
        return new OntologyPatcher(p1, p2);
    }

    @Bean
    @Scope("prototype")
    OntologyDiff2OntologyChanges ontologyDiff2OntologyChanges() {
        return new OntologyDiff2OntologyChanges();
    }

    @Bean
    @Scope("prototype")
    OntologyDiffCalculator ontologyDiffCalculator(AnnotationDiffCalculator p1, AxiomDiffCalculator p2) {
        return new OntologyDiffCalculator(p1, p2);
    }

    @Bean
    @Scope("prototype")
    AxiomDiffCalculator axiomDiffCalculator() {
        return new AxiomDiffCalculator();
    }

    @Bean
    @Scope("prototype")
    AnnotationDiffCalculator annotationDiffCalculator() {
        return new AnnotationDiffCalculator();
    }

    @Bean
    ProjectChangesManager projectChangesManager(ProjectId p1,
                                                RevisionManager p2,
                                                RenderingManager p3,
                                                Comparator<OntologyChange> p4,
                                                Provider<Revision2DiffElementsTranslator> p5,
                                                EntitiesInProjectSignatureByIriIndex p6) {
        return new ProjectChangesManager(p1, p2, p3, p4, p5, p6);
    }

    @Bean
    @Scope("prototype")
    Revision2DiffElementsTranslator revision2DiffElementsTranslator(OntologyIRIShortFormProvider p1,
                                                                    DefaultOntologyIdManager p2,
                                                                    ProjectOntologiesIndex p3) {
        return new Revision2DiffElementsTranslator(p1, p2, p3);
    }

    @Bean
    WatchedChangesManager watchedChangesManager(ProjectChangesManager p1,
                                                ClassHierarchyProvider p2,
                                                ObjectPropertyHierarchyProvider p3,
                                                DataPropertyHierarchyProvider p4,
                                                AnnotationPropertyHierarchyProvider p5,
                                                RevisionManager p6,
                                                EntitiesByRevisionCache p7,
                                                ProjectClassAssertionAxiomsByIndividualIndex p8) {
        return new WatchedChangesManager(p1, p2, p3, p4, p5, p6, p7, p8);
    }

    @Bean
    EntitiesByRevisionCache entitiesByRevisionCache(AxiomSubjectProvider p1,
                                                    HasContainsEntityInSignature p2,
                                                    OWLDataFactory p3) {
        return new EntitiesByRevisionCache(p1, p2, p3);
    }

    @Bean
    RevisionReverterChangeListGeneratorFactory revisionReverterChangeListGeneratorFactory(Provider<RevisionManager> p1) {
        return new RevisionReverterChangeListGeneratorFactory(p1);
    }

    @Bean
    CommentNotificationEmailer commentNotificationEmailer(ProjectDetailsManager p1,
                                                          UserDetailsManager p2,
                                                          AccessManager p3,
                                                          DiscussionThreadParticipantsExtractor p4,
                                                          CommentNotificationEmailGenerator p5,
                                                          SendMail p6,
                                                          CommentMessageIdGenerator p7) {
        return new CommentNotificationEmailer(p1, p2, p3, p4, p5, p6, p7);
    }

    @Bean
    DiscussionThreadParticipantsExtractor discussionThreadParticipantsExtractor(CommentParticipantsExtractor p1) {
        return new DiscussionThreadParticipantsExtractor(p1);
    }

    @Bean
    CommentParticipantsExtractor commentParticipantsExtractor(MentionParser p1) {
        return new CommentParticipantsExtractor(p1);
    }

    @Bean
    MentionParser mentionParser() {
        return new MentionParser();
    }

    @Bean
    CommentNotificationEmailGenerator commentNotificationEmailGenerator(@CommentNotificationEmailTemplate FileContents p1,
                                                                        TemplateEngine p2,
                                                                        ApplicationNameSupplier p3,
                                                                        PlaceUrl p4) {
        return new CommentNotificationEmailGenerator(p1, p2, p3, p4);
    }

    @Bean
    CommentMessageIdGenerator commentMessageIdGenerator(MessageIdGenerator p1) {
        return new CommentMessageIdGenerator(p1);
    }

    @Bean
    MessageIdGenerator messageIdGenerator(ApplicationHostSupplier p1) {
        return new MessageIdGenerator(p1);
    }

    @Bean
    CommentPostedSlackWebhookInvoker commentPostedSlackWebhookInvoker(ApplicationNameSupplier p1,
                                                                      PlaceUrl p2,
                                                                      WebhookExecutor p3,
                                                                      SlackWebhookRepository p4,
                                                                      @CommentNotificationSlackTemplate FileContents p5,
                                                                      TemplateEngine p6) {
        return new CommentPostedSlackWebhookInvoker(p1, p2, p3, p4, p5, p6);
    }

    @Bean
    EntitySearcherFactory entitySearcherFactory(ProjectId p1, DictionaryManager p2, EntityNodeRenderer p3, MatcherFactory p4) {
        return new EntitySearcherFactory(p1, p2, p3, p4);
    }

    @Bean
    DeleteEntitiesChangeListGeneratorFactory deleteEntitiesChangeListGeneratorFactory(Provider<MessageFormatter> p1,
                                                                                      Provider<EntityDeleter> p2) {
        return new DeleteEntitiesChangeListGeneratorFactory(p1, p2);
    }

    @Bean
    HierarchyChangesComputerFactory genericHierarchyChangedComputerFactory(ProjectId p1, EntityNodeRenderer p2, EntityHierarchyChangedEventProxyFactory p3) {
        return new HierarchyChangesComputerFactory(p1, p2, p3);
    }

    @Bean
    HierarchyProviderManager hierarchyProviderMapper(HierarchyProviderFactory p6) {
        return new HierarchyProviderManager(p6);
    }

    @Bean
    ClassHierarchyProviderFactory classHierarchyProviderFactory(ProjectId p1, ProjectOntologiesIndex p2, SubClassOfAxiomsBySubClassIndex p3, EquivalentClassesAxiomsIndex p4, ProjectSignatureByTypeIndex p5, EntitiesInProjectSignatureByIriIndex p6, ClassHierarchyChildrenAxiomsIndex p7, ClassHierarchyProvider p8) {
        return new ClassHierarchyProviderFactory(p1, p2, p3, p4, p5, p6, p7, p8);
    }

    @Bean
    ObjectPropertyHierarchyProviderFactory objectPropertyHierarchyProviderFactory(ProjectId p1, EntitiesInProjectSignatureIndex p2, ProjectOntologiesIndex p3, OntologySignatureByTypeIndex p4, SubObjectPropertyAxiomsBySubPropertyIndex p5, AxiomsByTypeIndex p6, ObjectPropertyHierarchyProvider p7) {
        return new ObjectPropertyHierarchyProviderFactory(p1, p2, p3, p4, p5, p6, p7);
    }

    @Bean
    HierarchyProviderFactory hierarchyProviderFactory(ClassHierarchyProviderFactory p5,
                                                      AnnotationPropertyHierarchyProviderFactory p6,
                                                      ObjectPropertyHierarchyProviderFactory p7,
                                                      DataPropertyHierarchyProviderFactory p8) {
        return new HierarchyProviderFactory(p5, p6, p7, p8);
    }

    @Bean
    AnnotationPropertyHierarchyProviderFactory annotationPropertyHierarchyProviderFactory(ProjectId p1, OWLAnnotationPropertyProvider p2, ProjectSignatureByTypeIndex p3, ProjectOntologiesIndex p4, SubAnnotationPropertyAxiomsBySubPropertyIndex p5, SubAnnotationPropertyAxiomsBySuperPropertyIndex p6, EntitiesInProjectSignatureIndex p7, AnnotationPropertyHierarchyProvider p8) {
        return new AnnotationPropertyHierarchyProviderFactory(p1,
                p2, p3, p4, p5, p6, p7, p8);
    }

    @Bean
    DataPropertyHierarchyProviderFactory dataPropertyHierarchyProviderFactory(ProjectId p1, EntitiesInProjectSignatureIndex p2, ProjectOntologiesIndex p3, OntologySignatureByTypeIndex p4, SubDataPropertyAxiomsBySubPropertyIndex p5, AxiomsByTypeIndex p6, DataPropertyHierarchyProvider p7) {
        return new DataPropertyHierarchyProviderFactory(p1,
                p2,
                p3,
                p4, p5, p6, p7);
    }


    @Bean
    MoveEntityChangeListGeneratorFactory moveEntityChangeListGeneratorFactory(OWLDataFactory p1,
                                                                              MessageFormatter p2,
                                                                              ProjectOntologiesIndex p3,
                                                                              DefaultOntologyIdManager p4,
                                                                              EquivalentClassesAxiomsIndex p5,
                                                                              SubClassOfAxiomsBySubClassIndex p6,
                                                                              SubObjectPropertyAxiomsBySubPropertyIndex p7,
                                                                              SubDataPropertyAxiomsBySubPropertyIndex p8,
                                                                              SubAnnotationPropertyAxiomsBySubPropertyIndex p9) {
        return new MoveEntityChangeListGeneratorFactory(p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }

    @Bean
    EditParentsChangeListGeneratorFactory editParentsChangeListGeneratorFactory(ProjectOntologiesIndex p1,
                                                                                SubClassOfAxiomsBySubClassIndex p2,
                                                                                OWLDataFactory p3) {
        return new EditParentsChangeListGeneratorFactory(p1, p2, p3);
    }

    @Bean
    MergeEntitiesChangeListGeneratorFactory mergeEntitiesChangeListGeneratorFactory(Provider<ProjectId> p1,
                                                                                    Provider<OWLDataFactory> p2,
                                                                                    Provider<EntityDiscussionThreadRepository> p3,
                                                                                    Provider<EntityRenamer> p4,
                                                                                    Provider<DefaultOntologyIdManager> p5,
                                                                                    Provider<ProjectOntologiesIndex> p6,
                                                                                    Provider<AnnotationAssertionAxiomsBySubjectIndex> p7) {
        return new MergeEntitiesChangeListGeneratorFactory(p1, p2, p3, p4, p5, p6, p7);
    }

    @Bean
    RevisionDetailsExtractor revisionDetailsExtractor(AxiomSubjectProvider p1) {
        return new RevisionDetailsExtractor(p1);
    }

    @Bean
    SetAnnotationValueActionChangeListGeneratorFactory setAnnotationValueActionChangeListGeneratorFactory(OWLDataFactory p1,
                                                                                                          ProjectOntologiesIndex p2,
                                                                                                          EntitiesInOntologySignatureIndex p3,
                                                                                                          AnnotationAssertionAxiomsBySubjectIndex p4) {
        return new SetAnnotationValueActionChangeListGeneratorFactory(p1, p2, p3, p4);
    }

    @Bean
    EditAnnotationsChangeListGeneratorFactory editAnnotationsChangeListGeneratorFactory(Provider<OWLDataFactory> p1,
                                                                                        Provider<ProjectOntologiesIndex> p2,
                                                                                        Provider<AnnotationAssertionAxiomsBySubjectIndex> p3) {
        return new EditAnnotationsChangeListGeneratorFactory(p1, p2, p3);
    }

    @Bean
    MoveClassesChangeListGeneratorFactory moveClassesChangeListGeneratorFactory(ProjectOntologiesIndex p1,
                                                                                SubClassOfAxiomsBySubClassIndex p2,
                                                                                OWLDataFactory p3) {
        return new MoveClassesChangeListGeneratorFactory(p1, p2, p3);
    }

    @Bean
    EntityGraphBuilderFactory entityGraphBuilderFactory(Provider<RenderingManager> p1,
                                                        Provider<ProjectOntologiesIndex> p2,
                                                        Provider<ObjectPropertyAssertionAxiomsBySubjectIndex> p3,
                                                        Provider<SubClassOfAxiomsBySubClassIndex> p4,
                                                        Provider<ClassAssertionAxiomsByIndividualIndex> p5,
                                                        Provider<EquivalentClassesAxiomsIndex> p6,
                                                        @EntityGraphEdgeLimit Integer p7) {
        return new EntityGraphBuilderFactory(p1, p2, p3, p4, p5, p6, p7);
    }

    @Bean
    EdgeMatcherFactory edgeMatcherFactory(MatcherFactory p1) {
        return new EdgeMatcherFactory(p1);
    }

    @Bean
    ManchesterSyntaxEntityFrameRenderer manchesterSyntaxEntityFrameRenderer(ShortFormProvider p1,
                                                                            OntologyIRIShortFormProvider p2,
                                                                            ManchesterSyntaxObjectRenderer p3,
                                                                            HighlightedEntityChecker p4,
                                                                            DeprecatedEntityChecker p5,
                                                                            ItemStyleProvider p6,
                                                                            NestedAnnotationStyle p7,
                                                                            ProjectOntologiesIndex p8,
                                                                            OntologyAnnotationsSectionRenderer p9,
                                                                            ClassFrameRenderer p10,
                                                                            ObjectPropertyFrameRenderer p11,
                                                                            NamedIndividualFrameRenderer p12,
                                                                            AnnotationPropertyFrameRenderer p13,
                                                                            DataPropertyFrameRenderer p14) {
        return new ManchesterSyntaxEntityFrameRenderer(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14);
    }

    @Bean
    ItemStyleProvider itemStyleProvider() {
        return new DefaultItemStyleProvider();
    }

    @Bean
    NestedAnnotationStyle nestedAnnotationStyle() {
        return NestedAnnotationStyle.MANCHESTER_SYNTAX;
    }

    @Bean
    OntologyAnnotationsSectionRenderer ontologyAnnotationsSectionRenderer(OntologyAnnotationsIndex p1) {
        return new OntologyAnnotationsSectionRenderer(p1);
    }

    @Bean
    ClassFrameRenderer classFrameRenderer(AnnotationsSectionRenderer<OWLClass> p1,
                                          ClassSubClassOfSectionRenderer p2,
                                          ClassEquivalentToSectionRenderer p3,
                                          ClassDisjointWithSectionRenderer p4) {
        return new ClassFrameRenderer(p1, p2, p3, p4);
    }

    @Bean
    ClassSubClassOfSectionRenderer classSubClassOfSectionRenderer(SubClassOfAxiomsBySubClassIndex p1) {
        return new ClassSubClassOfSectionRenderer(p1);
    }

    @Bean
    ClassEquivalentToSectionRenderer classEquivalentToSectionRenderer(EquivalentClassesAxiomsIndex p1) {
        return new ClassEquivalentToSectionRenderer(p1);
    }

    @Bean
    ClassDisjointWithSectionRenderer classDisjointWithSectionRenderer(DisjointClassesAxiomsIndex p1) {
        return new ClassDisjointWithSectionRenderer(p1);
    }

    @Bean
    ObjectPropertyFrameRenderer objectPropertyFrameRenderer(AnnotationsSectionRenderer<OWLObjectProperty> p1,
                                                            ObjectPropertyDomainSectionRenderer p2,
                                                            ObjectPropertyRangeSectionRenderer p3,
                                                            ObjectPropertyCharacteristicsSectionRenderer p4,
                                                            ObjectPropertySubPropertyOfRenderer p5,
                                                            ObjectPropertyEquivalentToSectionRenderer p6,
                                                            ObjectPropertyDisjointWithSectionRenderer p7,
                                                            ObjectPropertyInverseOfSectionRenderer p8,
                                                            ObjectPropertySubPropertyChainSectionRenderer p9) {
        return new ObjectPropertyFrameRenderer(p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }

    @Bean
    ObjectPropertyDomainSectionRenderer objectPropertyDomainSectionRenderer(ObjectPropertyDomainAxiomsIndex p1) {
        return new ObjectPropertyDomainSectionRenderer(p1);
    }

    @Bean
    ObjectPropertyRangeSectionRenderer objectPropertyRangeSectionRenderer(ObjectPropertyRangeAxiomsIndex p1) {
        return new ObjectPropertyRangeSectionRenderer(p1);
    }

    @Bean
    ObjectPropertyCharacteristicsSectionRenderer objectPropertyCharacteristicsSectionRenderer(AxiomsByTypeIndex p1) {
        return new ObjectPropertyCharacteristicsSectionRenderer(p1);
    }

    @Bean
    ObjectPropertySubPropertyOfRenderer objectPropertySubPropertyOfRenderer(SubObjectPropertyAxiomsBySubPropertyIndex p1) {
        return new ObjectPropertySubPropertyOfRenderer(p1);
    }

    @Bean
    ObjectPropertyEquivalentToSectionRenderer objectPropertyEquivalentToSectionRenderer(
            EquivalentObjectPropertiesAxiomsIndex p1) {
        return new ObjectPropertyEquivalentToSectionRenderer(p1);
    }

    @Bean
    ObjectPropertyDisjointWithSectionRenderer objectPropertyDisjointWithSectionRenderer(
            DisjointObjectPropertiesAxiomsIndex p1) {
        return new ObjectPropertyDisjointWithSectionRenderer(p1);
    }

    @Bean
    ObjectPropertyInverseOfSectionRenderer objectPropertyInverseOfSectionRenderer(InverseObjectPropertyAxiomsIndex p1) {
        return new ObjectPropertyInverseOfSectionRenderer(p1);
    }

    @Bean
    ObjectPropertySubPropertyChainSectionRenderer ObjectPropertySubPropertyChainSectionRenderer(AxiomsByTypeIndex p1) {
        return new ObjectPropertySubPropertyChainSectionRenderer(p1);
    }


    @Bean
    DataPropertyFrameRenderer dataPropertyFrameRenderer(AnnotationsSectionRenderer<OWLDataProperty> p1,
                                                        DataPropertyDomainSectionRenderer p2,
                                                        DataPropertyRangeSectionRenderer p3,
                                                        DataPropertyCharacteristicsSectionRenderer p4,
                                                        DataPropertySubPropertyOfSectionRenderer p5,
                                                        DataPropertyEquivalentToSectionRenderer p6,
                                                        DataPropertyDisjointWithSectionRenderer p7) {
        return new DataPropertyFrameRenderer(p1, p2, p3, p4, p5, p6, p7);
    }

    @Bean
    DataPropertyDomainSectionRenderer DataPropertyDomainSectionRenderer(DataPropertyDomainAxiomsIndex p1) {
        return new DataPropertyDomainSectionRenderer(p1);
    }

    @Bean
    DataPropertyRangeSectionRenderer DataPropertyRangeSectionRenderer(DataPropertyRangeAxiomsIndex p1) {
        return new DataPropertyRangeSectionRenderer(p1);
    }

    @Bean
    DataPropertyCharacteristicsSectionRenderer DataPropertyCharacteristicsSectionRenderer(AxiomsByTypeIndex p1) {
        return new DataPropertyCharacteristicsSectionRenderer(p1);
    }

    @Bean
    DataPropertySubPropertyOfSectionRenderer DataPropertySubPropertyOfSectionRenderer(
            SubDataPropertyAxiomsBySubPropertyIndex p1) {
        return new DataPropertySubPropertyOfSectionRenderer(p1);
    }

    @Bean
    DataPropertyEquivalentToSectionRenderer DataPropertyEquivalentToSectionRenderer(EquivalentDataPropertiesAxiomsIndex p1) {
        return new DataPropertyEquivalentToSectionRenderer(p1);
    }

    @Bean
    DataPropertyDisjointWithSectionRenderer DataPropertyDisjointWithSectionRenderer(DisjointDataPropertiesAxiomsIndex p1) {
        return new DataPropertyDisjointWithSectionRenderer(p1);
    }

    @Bean
    AnnotationPropertyFrameRenderer annotationPropertyFrameRenderer(AnnotationsSectionRenderer<OWLAnnotationProperty> p1,
                                                                    AnnotationPropertyDomainSectionRenderer p2,
                                                                    AnnotationPropertyRangeSectionRenderer p3,
                                                                    AnnotationPropertySubPropertyOfSectionRenderer p4) {
        return new AnnotationPropertyFrameRenderer(p1, p2, p3, p4);
    }

    @Bean
    AnnotationPropertyDomainSectionRenderer AnnotationPropertyDomainSectionRenderer(AnnotationPropertyDomainAxiomsIndex p1,
                                                                                    EntitiesInProjectSignatureByIriIndex p2) {
        return new AnnotationPropertyDomainSectionRenderer(p1, p2);
    }

    @Bean
    AnnotationPropertyRangeSectionRenderer AnnotationPropertyRangeSectionRenderer(AnnotationPropertyRangeAxiomsIndex p1,
                                                                                  EntitiesInProjectSignatureByIriIndex p2) {
        return new AnnotationPropertyRangeSectionRenderer(p1, p2);
    }

    @Bean
    AnnotationPropertySubPropertyOfSectionRenderer AnnotationPropertySubPropertyOfSectionRenderer(
            SubAnnotationPropertyAxiomsBySubPropertyIndex p1) {
        return new AnnotationPropertySubPropertyOfSectionRenderer(p1);
    }

    @Bean
    NamedIndividualFrameRenderer namedIndividualFrameRenderer(NamedIndividualTypesSectionRenderer p1,
                                                              AnnotationsSectionRenderer<OWLNamedIndividual> p2,
                                                              NamedIndividualFactsSectionRenderer p3,
                                                              NamedIndividualSameAsSectionRenderer p4,
                                                              NamedIndividualDifferentFromSectionRenderer p5) {
        return new NamedIndividualFrameRenderer(p1, p2, p3, p4, p5);
    }

    @Bean
    NamedIndividualFactsSectionRenderer NamedIndividualFactsSectionRenderer(ObjectPropertyAssertionAxiomsBySubjectIndex p1,
                                                                            DataPropertyAssertionAxiomsBySubjectIndex p2) {
        return new NamedIndividualFactsSectionRenderer(p1, p2);
    }

    @Bean
    NamedIndividualSameAsSectionRenderer NamedIndividualSameAsSectionRenderer(SameIndividualAxiomsIndex p1) {
        return new NamedIndividualSameAsSectionRenderer(p1);
    }

    @Bean
    NamedIndividualDifferentFromSectionRenderer NamedIndividualDifferentFromSectionRenderer(
            DifferentIndividualsAxiomsIndex p1) {
        return new NamedIndividualDifferentFromSectionRenderer(p1);
    }

    @Bean
    NamedIndividualTypesSectionRenderer namedIndividualTypesSectionRenderer(ClassAssertionAxiomsByIndividualIndex p1) {
        return new NamedIndividualTypesSectionRenderer(p1);
    }

    @Bean
    EntityFormManager entityFormManager(EntityFormRepository p1, EntityFormSelectorRepository p2, MatchingEngine p3) {
        return new EntityFormManager(p1, p2, p3);
    }

    @Bean
    EntityFrameFormDataDtoBuilderFactory entityFrameFormDataDtoBuilderFactory() {
        return new EntityFrameFormDataDtoBuilderFactoryImpl();
    }


    @Bean
    @EntityGraphEdgeLimit
    Integer entityGraphEdgeLimit(@Value("${webprotege.entityGraph.edgeLimit}") Integer edgeLimit) {
        return edgeLimit;
    }

    @Bean
    BindingValuesExtractor bindingValuesExtractor(ProjectOntologiesIndex p1, ClassAssertionAxiomsByIndividualIndex p2,
                                                  ClassHierarchyProvider p3,
                                                  ObjectPropertyAssertionAxiomsBySubjectIndex p4,
                                                  DataPropertyAssertionAxiomsBySubjectIndex p5,
                                                  AnnotationAssertionAxiomsBySubjectIndex p6,
                                                  ClassAssertionAxiomsByClassIndex p7, ClassFrameProvider p8,
                                                  MatcherFactory p9) {
        return new BindingValuesExtractor(p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }

    @Bean
    LogicalDefinitionExtractor logicalDefinitionExtractor(@Nonnull ProjectId projectId,
                                                          @Nonnull RenderingManager renderingManager,
                                                          @Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                                                          @Nonnull EquivalentClassesAxiomsIndex equivalentClassesAxiomsIndex
    ) {
        return new LogicalDefinitionExtractor(projectId, renderingManager, projectOntologiesIndex, equivalentClassesAxiomsIndex);
    }

    @Bean
    NecessaryConditionsExtractor necessaryConditionsExtractor(@Nonnull ProjectId projectId,
                                                              @Nonnull RenderingManager renderingManager,
                                                              @Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                                                              @Nonnull SubClassOfAxiomsBySubClassIndex subClassOfAxiomsIndex
    ) {
        return new NecessaryConditionsExtractor(projectId, renderingManager, projectOntologiesIndex, subClassOfAxiomsIndex);
    }


    @Bean
    UpdateLogicalDefinitionsChangeListGeneratorFactory updateLogicalDefinitionsChangeListGeneratorFactory(@Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                                                                                                          @Nonnull OWLDataFactory dataFactory) {
        return new UpdateLogicalDefinitionsChangeListGeneratorFactory(projectOntologiesIndex, dataFactory);
    }

    @Bean
    ProjectOrderedChildrenManager projectOrderedChildrenManager(@Nonnull ProjectId projectId,
                                                                @Nonnull ProjectOrderedChildrenService projectOrderedChildrenService,
                                                                @Nonnull ReadWriteLockService readWriteLockService,
                                                                @Nonnull NewRevisionsEventEmitterService newRevisionsEventEmitterService) {
        return new ProjectOrderedChildrenManager(projectId, projectOrderedChildrenService, readWriteLockService, newRevisionsEventEmitterService);
    }

    @Bean
    AncestorHierarchyNodeMapper ancestorHierarchyNodeMapper(RenderingManager renderingManager){
        return new AncestorHierarchyNodeMapper(renderingManager);
    }

    @Bean
    CommandExecutor<UpdateEntityChildrenRequest, UpdateEntityChildrenResponse> executorForUpdateEntityChildrenInBackupService() {
        return new CommandExecutorImpl<>(UpdateEntityChildrenResponse.class);
    }

}
