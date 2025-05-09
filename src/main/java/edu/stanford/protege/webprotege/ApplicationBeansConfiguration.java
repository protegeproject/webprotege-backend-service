package edu.stanford.protege.webprotege;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheFactory;
import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.api.*;
import edu.stanford.protege.webprotege.app.*;
import edu.stanford.protege.webprotege.authorization.GetProjectRoleDefinitionsRequest;
import edu.stanford.protege.webprotege.authorization.GetProjectRoleDefinitionsResponse;
import edu.stanford.protege.webprotege.authorization.SetProjectRoleDefinitionsRequest;
import edu.stanford.protege.webprotege.authorization.SetProjectRoleDefinitionsResponse;
import edu.stanford.protege.webprotege.card.CardDescriptorRepository;
import edu.stanford.protege.webprotege.card.CardDescriptorRepositoryImpl;
import edu.stanford.protege.webprotege.dispatch.ApplicationActionHandler;
import edu.stanford.protege.webprotege.dispatch.DispatchServiceExecutor;
import edu.stanford.protege.webprotege.dispatch.impl.ApplicationActionHandlerRegistry;
import edu.stanford.protege.webprotege.dispatch.impl.DispatchServiceExecutorImpl;
import edu.stanford.protege.webprotege.filemanager.FileContents;
import edu.stanford.protege.webprotege.forms.*;
import edu.stanford.protege.webprotege.hierarchy.*;
import edu.stanford.protege.webprotege.hierarchy.ordering.ProjectOrderedChildrenRepository;
import edu.stanford.protege.webprotege.hierarchy.ordering.ProjectOrderedChildrenRepositoryImpl;
import edu.stanford.protege.webprotege.hierarchy.ordering.ProjectOrderedChildrenService;
import edu.stanford.protege.webprotege.hierarchy.ordering.ProjectOrderedChildrenServiceImpl;
import edu.stanford.protege.webprotege.index.*;
import edu.stanford.protege.webprotege.inject.*;
import edu.stanford.protege.webprotege.inject.project.ProjectDirectoryFactory;
import edu.stanford.protege.webprotege.ipc.CommandExecutor;
import edu.stanford.protege.webprotege.ipc.impl.CommandExecutorImpl;
import edu.stanford.protege.webprotege.issues.CommentNotificationEmailTemplate;
import edu.stanford.protege.webprotege.issues.EntityDiscussionThreadRepository;
import edu.stanford.protege.webprotege.lang.DefaultDisplayNameSettingsFactory;
import edu.stanford.protege.webprotege.locking.*;
import edu.stanford.protege.webprotege.mail.MessageIdGenerator;
import edu.stanford.protege.webprotege.mail.MessagingExceptionHandlerImpl;
import edu.stanford.protege.webprotege.mail.SendMailImpl;
import edu.stanford.protege.webprotege.mansyntax.render.DefaultHttpLinkRenderer;
import edu.stanford.protege.webprotege.mansyntax.render.HttpLinkRenderer;
import edu.stanford.protege.webprotege.mansyntax.render.LiteralStyle;
import edu.stanford.protege.webprotege.mansyntax.render.MarkdownLiteralRenderer;
import edu.stanford.protege.webprotege.ontology.ProcessUploadedOntologiesRequest;
import edu.stanford.protege.webprotege.ontology.ProcessUploadedOntologiesResponse;
import edu.stanford.protege.webprotege.permissions.ProjectPermissionsManager;
import edu.stanford.protege.webprotege.permissions.ProjectPermissionsManagerImpl;
import edu.stanford.protege.webprotege.permissions.RebuildProjectPermissionsRequest;
import edu.stanford.protege.webprotege.permissions.RebuildProjectPermissionsResponse;
import edu.stanford.protege.webprotege.persistence.*;
import edu.stanford.protege.webprotege.perspective.*;
import edu.stanford.protege.webprotege.project.*;
import edu.stanford.protege.webprotege.revision.*;
import edu.stanford.protege.webprotege.search.EntitySearchFilterRepositoryImpl;
import edu.stanford.protege.webprotege.sharing.ProjectSharingSettingsManagerImpl;
import edu.stanford.protege.webprotege.storage.MinioProperties;
import edu.stanford.protege.webprotege.tag.EntityTagsRepositoryImpl;
import edu.stanford.protege.webprotege.tag.TagRepositoryImpl;
import edu.stanford.protege.webprotege.templates.TemplateEngine;
import edu.stanford.protege.webprotege.user.*;
import edu.stanford.protege.webprotege.util.DisposableObjectManager;
import edu.stanford.protege.webprotege.util.TempFileFactoryImpl;
import edu.stanford.protege.webprotege.viz.EntityGraphSettingsRepositoryImpl;
import edu.stanford.protege.webprotege.watches.WatchNotificationEmailTemplate;
import edu.stanford.protege.webprotege.watches.WatchRecordRepositoryImpl;
import edu.stanford.protege.webprotege.webhook.*;
import io.minio.MinioClient;
import jakarta.annotation.Nonnull;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import jakarta.inject.Provider;
import jakarta.inject.Singleton;
import java.io.File;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-07-07
 */
@Configuration
@EnableMongoRepositories
@EnableAsync
public class ApplicationBeansConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationBeansConfiguration.class);


    @Bean
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(10); // Customize the pool size as needed
    }

    @Bean
    public MongoCustomConversions mongoCustomConversions(DocumentToOwlEntityConverter documentToOwlEntityConverter) {
        var converters = new ArrayList<>();
        converters.add(new StringToIriConverter());
        converters.add(new IriToStringConverter());
        converters.add(new OwlEntityToDocumentConverter());
        converters.add(documentToOwlEntityConverter);
        converters.add(new ProjectId2StringConverter());
        converters.add(new String2ProjectIdConverter());
        converters.add(new UserId2StringConverter());
        converters.add(new String2UserIdConverter());
        converters.add(new String2ThreadIdConverter());
        converters.add(new ThreadId2StringConverter());
        converters.add(new CommentId2StringConverter());
        converters.add(new String2CommentIdConverter());
        return new MongoCustomConversions(converters);
    }

    @Bean
    DocumentToOwlEntityConverter documentToOwlEntityConverter(OWLDataFactory dataFactory) {
        return new DocumentToOwlEntityConverter(dataFactory);
    }

    @Bean
    @DataDirectory
    public File provideDataDirectory(DataDirectoryProvider provider) {
        return provider.get();
    }

    @Bean
    public TempFileFactoryImpl provideTempFileFactory() {
        return new TempFileFactoryImpl();
    }

    @Bean
    public DefaultMustacheFactory providesMustacheFactory() {
        return new DefaultMustacheFactory();
    }

    @Bean
    DataDirectoryProvider getDataDirectoryProvider() {
        return new DataDirectoryProvider();
    }

    @Bean
    OverridableFileFactory getOverridableFileFactory(DataDirectoryProvider dataDirectoryProvider) {
        return new OverridableFileFactory(dataDirectoryProvider.get());
    }

    @Bean
    @CommentNotificationEmailTemplate
    public OverridableFile provideCommentNotificationTemplateFile(OverridableFileFactory factory) {
        return factory.getOverridableFile("templates/comment-notification-email-template.html");
    }

    @Bean
    @CommentNotificationEmailTemplate
    public FileContents providesCommentNotificationTemplate(@CommentNotificationEmailTemplate OverridableFile file) {
        return new FileContents(file);
    }

    @Bean
    @CommentNotificationSlackTemplate
    public OverridableFile provideCommentNotificationSlackTemplate(OverridableFileFactory factory) {
        return factory.getOverridableFile("templates/comment-notification-slack-template.json");
    }

    @Bean
    @CommentNotificationSlackTemplate
    public FileContents providesCommentNotificationSlackTemplate(@CommentNotificationSlackTemplate OverridableFile file) {
        return new FileContents(file);
    }

    @Bean
    @WatchNotificationEmailTemplate
    OverridableFile provideWatchNotificationEmailTemplateFile(OverridableFileFactory factory) {
        return factory.getOverridableFile("templates/watch-notification-email-template.html");
    }

    @Bean
    @WatchNotificationEmailTemplate
    FileContents provideWatchNotificationEmailTemplate(@WatchNotificationEmailTemplate OverridableFile file) {
        return new FileContents(file);
    }

    @Bean
    @Singleton
    ProjectComponentFactory getProjectComponentFactory(ApplicationContext applicationContext) {
        return new ProjectComponentFactoryImpl(applicationContext);
    }

    @Bean
    @Singleton
    OWLDataFactory getOwlDataFactory() {
        return new OWLDataFactoryImpl();
    }

    @Bean
    ProjectDirectoryFactory getProjectDirectoryFactory(DataDirectoryProvider dataDirectoryProvider) {
        return new ProjectDirectoryFactory(dataDirectoryProvider.get());
    }

    @Bean
    edu.stanford.protege.webprotege.revision.ProjectDirectoryFactory projectDirectoryFactoryRev(@DataDirectory File dataDirectory) {
        return new edu.stanford.protege.webprotege.revision.ProjectDirectoryFactory(dataDirectory);
    }

    @Bean
    ChangeHistoryFileFactory getChangeHistoryFileFactory(edu.stanford.protege.webprotege.revision.ProjectDirectoryFactory projectDirectoryFactory) {
        return new ChangeHistoryFileFactory(projectDirectoryFactory);
    }

    @Bean
    OntologyChangeRecordTranslator ontologyChangeRecordTranslator() {
        return new OntologyChangeRecordTranslatorImpl();
    }

    @Bean
    RevisionStoreFactory getRevisionStoreFactory(ChangeHistoryFileFactory changeHistoryFileFactory,
                                                 OWLDataFactory dataFactory,
                                                 OntologyChangeRecordTranslator changeRecordTranslator) {
        return new RevisionStoreFactory(changeHistoryFileFactory,
                                        dataFactory,
                                        changeRecordTranslator);
    }

    @Bean
    ProjectCacheManager projectCacheManager(ProjectCache projectCache,
                                            ApplicationDisposablesManager disposablesManager) {
        var cacheManager = new ProjectCacheManager(projectCache, disposablesManager);
        return cacheManager;
    }

    @Bean
    @Singleton
    ProjectCache getProjectCache(ProjectComponentFactory projectComponentFactory) {
        return new ProjectCache(projectComponentFactory,
                                50000);
    }

    @Bean
    @Singleton
    ProjectAccessManager getProjectAccessManager(MongoTemplate mongoTemplate) {
        return new ProjectAccessManagerImpl(mongoTemplate);
    }


    @Bean
    @Singleton
    ProjectManager getProjectManager(ProjectCache projectCache, ProjectAccessManager projectAccessManager) {
        return new ProjectManager(projectCache,
                                  projectAccessManager);
    }

    @Bean
    ApplicationActionHandlerRegistry getApplicationActionHandlerRegistry(Set<? extends ApplicationActionHandler> handlers) {
        return new ApplicationActionHandlerRegistry(handlers);
    }

    @Bean
    UserRecordConverter provideUserRecordConverter() {
        return new UserRecordConverter();
    }

    @Bean
    UserRecordRepository getUserRecordRepository(MongoTemplate mongoTemplate) {
        return new UserRecordRepository(mongoTemplate, new UserRecordConverter());
    }

    @Bean
    @Singleton
    DispatchServiceExecutor getDispatchServiceExecutor(ApplicationActionHandlerRegistry actionHandlerRegistry,
                                                               ProjectManager projectManager) {
        return new DispatchServiceExecutorImpl(actionHandlerRegistry,
                                               projectManager);
    }


    @Bean
    public ActionExecutor getActionExecutor(DispatchServiceExecutor dispatchServiceExecutor) {
        return new ActionExecutor(dispatchServiceExecutor);
    }

    @Bean
    @Singleton
    public ApplicationDisposablesManager getApplicationDisposablesManager(DisposableObjectManager disposableObjectManager) {
        return new ApplicationDisposablesManager(disposableObjectManager);
    }

    @Bean
    public DisposableObjectManager getDisposableObjectManager() {
        return new DisposableObjectManager();
    }

    @Singleton
    @Bean
    ProjectDetailsRepository getProjectDetailsRepository(MongoTemplate mongoTemplate,
                                                         ObjectMapper objectMapper) {
        return new ProjectDetailsRepository(mongoTemplate, objectMapper);
    }

    @Bean
    @Singleton
    SlackWebhookRepository getSlackWebhookRepository(MongoTemplate mongoTemplate) {
        return new SlackWebhookRepositoryImpl(mongoTemplate);
    }

    @Bean
    @Singleton
    WebhookRepository getWebhookRepository(MongoTemplate mongoTemplate) {
        return new WebhookRepositoryImpl(mongoTemplate);
    }

    @Singleton
    @Bean
    DefaultDisplayNameSettingsFactory getDefaultDisplayNameSettingsFactory() {
        return new DefaultDisplayNameSettingsFactory();
    }

    @Singleton
    @Bean
    ProjectDetailsManager getProjectDetailsManager(ProjectDetailsRepository projectDetailsRepository,
                                                   SlackWebhookRepository slackWebhookRepository,
                                                   WebhookRepository webhookRepository,
                                                   DefaultDisplayNameSettingsFactory defaultDisplayNameSettings) {
        return new ProjectDetailsManagerImpl(projectDetailsRepository, slackWebhookRepository, webhookRepository, defaultDisplayNameSettings);
    }

    @Bean
    ProjectPermissionsManager getProjectPermissionsManager(AccessManager accessManager,
                                                           ProjectDetailsRepository projectDetailsRepository) {
        return new ProjectPermissionsManagerImpl(accessManager,
                                                 projectDetailsRepository);
    }

    @Bean
    UserActivityManager getUserActivityManager(MongoTemplate mongoTemplate) {
        return new UserActivityManager(mongoTemplate);
    }

    @Bean
    @MailProperties
    Properties getMailProperties() {
        return new Properties();
    }

    @Bean
    SendMailImpl getSendMail(ApplicationNameSupplier applicationNameSupplier,
                             ApplicationHostSupplier applicationHostSupplier,
                             @MailProperties Properties mailProperties) {
        return new SendMailImpl(applicationNameSupplier,
                                applicationHostSupplier,
                                mailProperties,
                                new MessagingExceptionHandlerImpl(),
                                new MessageIdGenerator(applicationHostSupplier));
    }

    @Bean
    MustacheFactory getMustacheFactory() {
        return new DefaultMustacheFactory();
    }

    @Bean
    TemplateEngine getTemplateEngine(Provider<MustacheFactory> mustacheFactory) {
        return new TemplateEngine(mustacheFactory);
    }

    @Bean
    PlaceUrl getPlaceUrl(ApplicationSchemeSupplier applicationSchemeSupplier,
                         ApplicationHostSupplier applicationHostSupplier,
                         ApplicationPortSupplier applicationPortSupplier,
                         ApplicationPathSupplier applicationPathSupplier,
                         ApplicationNameSupplier applicationNameSupplier,
                         EntityTypePerspectiveMapper entityTypePerspectiveMapper) {
        return new PlaceUrl(applicationSchemeSupplier,
                            applicationHostSupplier,
                            applicationPortSupplier,
                            applicationPathSupplier,
                            applicationNameSupplier,
                            entityTypePerspectiveMapper);
    }
    
    @Bean
    @Singleton
    ApplicationPreferencesStore getApplicationPreferencesStore(MongoTemplate mongoTemplate) {
        return new ApplicationPreferencesStore(mongoTemplate);
    }

    @Bean
    @Singleton
    ApplicationSettingsManager getApplicationSettingsManager(AccessManager accessManager,
                                                             ApplicationPreferencesStore applicationPreferencesStore) {
        return new ApplicationSettingsManager(accessManager,
                                              applicationPreferencesStore);
    }

    @Bean
    ApplicationSchemeSupplier applicationSchemeSupplier(ApplicationPreferencesStore preferencesStore) {
        return new ApplicationSchemeSupplier(preferencesStore);
    }

    @Bean
    ApplicationHostSupplier applicationHostSupplier(ApplicationPreferencesStore preferencesStore) {
        return new ApplicationHostSupplier(preferencesStore);
    }

    @Bean
    ApplicationPortSupplier applicationPortSupplier(ApplicationPreferencesStore preferencesStore) {
        return new ApplicationPortSupplier(preferencesStore);
    }

    @Bean
    ApplicationPathSupplier applicationPathSupplier(ApplicationPreferencesStore preferencesStore) {
        return new ApplicationPathSupplier(preferencesStore);
    }

    @Bean
    EntityTypePerspectiveMapper entityTypePerspectiveMapper() {
        return new EntityTypePerspectiveMapper();
    }

    @Bean
    @Singleton
    PerspectiveDescriptorRepository perspectiveDescriptorRepository(MongoTemplate mongoTemplate,
                                                                       ObjectMapper objectMapper) {
        return new PerspectiveDescriptorRepositoryImpl(mongoTemplate, objectMapper);
    }

    @Bean
    @Singleton
    PerspectiveLayoutRepository perspectiveLayoutRepository(MongoTemplate mongoTemplate, ObjectMapper objectMapper) {
        return new PerspectiveLayoutRepositoryImpl(mongoTemplate, objectMapper);
    }

    @Bean
    @Singleton
    BuiltInPerspectiveLoader builtInPerspectiveLoader(ObjectMapper objectMapper) {
        return new BuiltInPerspectiveLoader(objectMapper);
    }

    @Bean
    @Singleton
    BuiltInPerspectivesProvider builtInPerspectivesProvider(Provider<BuiltInPerspectiveLoader> builtInPerspectiveLoaderProvider) {
        return new BuiltInPerspectivesProvider(builtInPerspectiveLoaderProvider);
    }

    @Bean
    @Singleton
    ImmutableList<BuiltInPerspective> builtInPerspectives(BuiltInPerspectivesProvider builtInPerspectivesProvider) {
        return builtInPerspectivesProvider.getBuiltInPerspectives();
    }

    @Bean
    PerspectivesManager perspectivesManager(ImmutableList<BuiltInPerspective> builtInPerspectives,
                                               PerspectiveDescriptorRepository perspectiveDescriptorRepository,
                                               PerspectiveLayoutRepository perspectiveLayoutRepository) {
        return new PerspectivesManagerImpl(builtInPerspectives,
                                           perspectiveDescriptorRepository,
                                           perspectiveLayoutRepository);
    }
    
    @Bean
    EntityTagsRepositoryImpl entityTagsRepository(MongoTemplate mongoTemplate) {
        return new EntityTagsRepositoryImpl(mongoTemplate);
    }

    @Bean
    ApplicationExecutorsRegistry applicationExecutorsRegistry(ApplicationDisposablesManager p1) {
        return new ApplicationExecutorsRegistry(p1);
    }

    @Bean
    TagRepositoryImpl tagRepository(MongoTemplate p1, ObjectMapper p2) {
        return new TagRepositoryImpl(p1, p2);
    }

    @Bean
    WatchRecordRepositoryImpl watchRecordRepository(MongoTemplate p1, ObjectMapper p2) {
        return new WatchRecordRepositoryImpl(p1, p2);
    }

    @Bean
    @Singleton
    BuiltInPrefixDeclarations builtInPrefixDeclarations(BuiltInPrefixDeclarationsLoader builtInPrefixDeclarationsLoader) {
        return builtInPrefixDeclarationsLoader.getBuiltInPrefixDeclarations();
    }

    @Bean
    BuiltInPrefixDeclarationsLoader builtInPrefixDeclarationsLoader(OverridableFileFactory overridableFileFactory) {
        return new BuiltInPrefixDeclarationsLoader(overridableFileFactory);
    }

    @Bean
    @Singleton
    EntitySearchFilterRepositoryImpl entitySearchFilterRepository(MongoTemplate mongoTemplate,
                                                                  ObjectMapper objectMapper) {
        return new EntitySearchFilterRepositoryImpl(mongoTemplate, objectMapper);
    }

    @Bean
    @Singleton
    BuiltInOwlEntitiesIndexImpl builtInOwlEntitiesIndex(OWLDataFactory dataFactory) {
        return new BuiltInOwlEntitiesIndexImpl(dataFactory);
    }


    @Bean
    EntityDiscussionThreadRepository entityDiscussionThreadRepository(MongoTemplate p1) {
        return new EntityDiscussionThreadRepository(p1);
    }

    @Bean
    PrefixDeclarationsStore prefixDeclarationsStore(ObjectMapper p1, MongoTemplate p2) {
        return new PrefixDeclarationsStore(p1, p2);
    }

    @Bean
    edu.stanford.protege.webprotege.mansyntax.render.LiteralRenderer literalRenderer() {
        return new MarkdownLiteralRenderer();
    }


    @Bean
    LiteralStyle literalStyle() {
        return LiteralStyle.REGULAR;
    }

    @Bean
    HttpLinkRenderer httpLinkRenderer() {
        return new DefaultHttpLinkRenderer();
    }

    @Bean
    ApplicationNameSupplier applicationNameSupplier(ApplicationPreferencesStore p1) {
        return new ApplicationNameSupplier(p1);
    }

    @Bean
    ProjectSharingSettingsManagerImpl projectSharingSettingsManager(AccessManager p1, UserDetailsManager p2) {
        return new ProjectSharingSettingsManagerImpl(p1, p2);
    }


    @Bean
    EntityGraphSettingsRepositoryImpl entityGraphSettingsRepository(MongoTemplate p1, ObjectMapper p2) {
        return new EntityGraphSettingsRepositoryImpl(p1, p2);
    }

    @Bean
    EntityFormRepositoryImpl entityFormRepository(ObjectMapper p1, MongoTemplate p2) {
        return new EntityFormRepositoryImpl(p1, p2);
    }

    @Bean
    EntityFormSelectorRepositoryImpl entityFormSelectorRepository(MongoTemplate p1, ObjectMapper p2) {
        return new EntityFormSelectorRepositoryImpl(p1, p2);
    }

    @Bean
    CommandExecutor<ProcessUploadedOntologiesRequest, ProcessUploadedOntologiesResponse> executorForProcessUploadedOntologiesRequest() {
        return new CommandExecutorImpl<>(ProcessUploadedOntologiesResponse.class);
    }

    @Bean
    CommandExecutor<CreateInitialRevisionHistoryRequest, CreateInitialRevisionHistoryResponse> executorForCreateInitialRevisionHistory() {
        return new CommandExecutorImpl<>(CreateInitialRevisionHistoryResponse.class);
    }

    @Bean
    MinioClient minioClient(MinioProperties properties) {
        return MinioClient.builder()
                          .credentials(properties.getAccessKey(), properties.getSecretKey())
                          .endpoint(properties.getEndPoint())
                          .build();
    }

    @Bean
    CardDescriptorRepository cardDescriptorRepository(MongoTemplate p1, ObjectMapper p2) {
        return new CardDescriptorRepositoryImpl(p1, p2);
    }

    @Bean
    NamedHierarchyRepository namedHierarchyRepository(ObjectMapper p0, MongoTemplate p1) {
        return new NamedHierarchyRepository(p0, p1);
    }

    @Bean
    NamedHierarchyManager hierarchiesManager(OWLDataFactory dataFactory, NamedHierarchyRepository repository) {
        return new NamedHierarchyManagerImpl(dataFactory, repository);
    }

    @Bean
    public ReadWriteLock readWriteLock() {
        return new ReentrantReadWriteLock(true);
    }

    @Bean
    ReadWriteLockService readWriteLockService(@Nonnull ReadWriteLockConfig config,
                                              @Nonnull ReadWriteLock readWriteLock) {
        return new ReadWriteLockServiceImpl(config, readWriteLock);
    }

    @Bean
    ProjectOrderedChildrenRepositoryImpl projectOrderedChildrenRepositoryImpl(MongoTemplate mongoTemplate,
                                                                              ReadWriteLockService readWriteLock) {
        return new ProjectOrderedChildrenRepositoryImpl(mongoTemplate, readWriteLock);
    }

    @Bean
    ProjectOrderedChildrenService projectOrderedChildrenService(@Nonnull ObjectMapper objectMapper,
                                                                @Nonnull ProjectOrderedChildrenRepository repository,
                                                                @Nonnull ReadWriteLockService readWriteLock) {
        return new ProjectOrderedChildrenServiceImpl(objectMapper, repository, readWriteLock);
    }

    @Bean
    HierarchyDescriptorRulesRepositoryImpl projectHierarchyDescriptorRulesRepository(MongoTemplate p1, ObjectMapper p2) {
        var repo = new HierarchyDescriptorRulesRepositoryImpl(p1, p2);
        repo.ensureIndexes();
        return repo;
    }

    @Bean
    HierarchyDescriptorRuleSelector hierarchyDescriptorRuleSelector(HierarchyDescriptorRulesRepository p1, HierarchyDescriptorRuleDisplayContextMatcher p2) {
        return new HierarchyDescriptorRuleSelector(p1, p2);
    }

    @Bean
    HierarchyDescriptorRuleDisplayContextMatcher hierarchyDescriptorRuleDisplayContextMatcher() {
        return new HierarchyDescriptorRuleDisplayContextMatcher();
    }

    @Bean
    CommandExecutor<GetProjectRoleDefinitionsRequest, GetProjectRoleDefinitionsResponse> getProjectRoleDefinitionsCommand() {
        return new CommandExecutorImpl<>(GetProjectRoleDefinitionsResponse.class);
    }

    @Bean
    CommandExecutor<SetProjectRoleDefinitionsRequest, SetProjectRoleDefinitionsResponse> setProjectRoleDefinitionsCommand() {
        return new CommandExecutorImpl<>(SetProjectRoleDefinitionsResponse.class);
    }

    @Bean
    CommandExecutor<GetFormRegionAccessRestrictionsRequest, GetFormRegionAccessRestrictionsResponse> getFormRegionAccessRestrictionsCommand() {
        return new CommandExecutorImpl<>(GetFormRegionAccessRestrictionsResponse.class);
    }

    @Bean
    CommandExecutor<RebuildProjectPermissionsRequest, RebuildProjectPermissionsResponse> rebuildProjectPermissionsCommandExecutor() {
        return new CommandExecutorImpl<>(RebuildProjectPermissionsResponse.class);
    }
}
