package edu.stanford.protege.webprotege;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheFactory;
import com.google.common.base.Ticker;
import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.RoleOracle;
import edu.stanford.protege.webprotege.access.RoleOracleImpl;
import edu.stanford.protege.webprotege.api.*;
import edu.stanford.protege.webprotege.app.*;
import edu.stanford.protege.webprotege.auth.*;
import edu.stanford.protege.webprotege.change.OntologyChangeRecordTranslator;
import edu.stanford.protege.webprotege.change.OntologyChangeRecordTranslatorImpl;
import edu.stanford.protege.webprotege.dispatch.ApplicationActionHandler;
import edu.stanford.protege.webprotege.dispatch.DispatchServiceExecutor;
import edu.stanford.protege.webprotege.dispatch.impl.ApplicationActionHandlerRegistry;
import edu.stanford.protege.webprotege.dispatch.impl.DispatchServiceExecutorImpl;
import edu.stanford.protege.webprotege.filemanager.FileContents;
import edu.stanford.protege.webprotege.forms.EntityFormRepositoryImpl;
import edu.stanford.protege.webprotege.forms.EntityFormSelectorRepositoryImpl;
import edu.stanford.protege.webprotege.index.*;
import edu.stanford.protege.webprotege.inject.*;
import edu.stanford.protege.webprotege.inject.project.ProjectDirectoryFactory;
import edu.stanford.protege.webprotege.issues.CommentNotificationEmailTemplate;
import edu.stanford.protege.webprotege.issues.EntityDiscussionThreadRepository;
import edu.stanford.protege.webprotege.lang.DefaultDisplayNameSettingsFactory;
import edu.stanford.protege.webprotege.mail.MessageIdGenerator;
import edu.stanford.protege.webprotege.mail.MessagingExceptionHandlerImpl;
import edu.stanford.protege.webprotege.mail.SendMailImpl;
import edu.stanford.protege.webprotege.mansyntax.render.DefaultHttpLinkRenderer;
import edu.stanford.protege.webprotege.mansyntax.render.HttpLinkRenderer;
import edu.stanford.protege.webprotege.mansyntax.render.LiteralStyle;
import edu.stanford.protege.webprotege.mansyntax.render.MarkdownLiteralRenderer;
import edu.stanford.protege.webprotege.permissions.ProjectPermissionsManager;
import edu.stanford.protege.webprotege.permissions.ProjectPermissionsManagerImpl;
import edu.stanford.protege.webprotege.perspective.*;
import edu.stanford.protege.webprotege.project.*;
import edu.stanford.protege.webprotege.revision.RevisionStoreFactory;
import edu.stanford.protege.webprotege.search.EntitySearchFilterRepositoryImpl;
import edu.stanford.protege.webprotege.sharing.ProjectSharingSettingsManagerImpl;
import edu.stanford.protege.webprotege.tag.EntityTagsRepositoryImpl;
import edu.stanford.protege.webprotege.tag.TagRepositoryImpl;
import edu.stanford.protege.webprotege.templates.TemplateEngine;
import edu.stanford.protege.webprotege.upload.*;
import edu.stanford.protege.webprotege.user.*;
import edu.stanford.protege.webprotege.util.DisposableObjectManager;
import edu.stanford.protege.webprotege.util.TempFileFactory;
import edu.stanford.protege.webprotege.util.TempFileFactoryImpl;
import edu.stanford.protege.webprotege.util.ZipInputStreamChecker;
import edu.stanford.protege.webprotege.viz.EntityGraphSettingsRepositoryImpl;
import edu.stanford.protege.webprotege.watches.WatchNotificationEmailTemplate;
import edu.stanford.protege.webprotege.watches.WatchRecordRepositoryImpl;
import edu.stanford.protege.webprotege.webhook.*;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import javax.inject.Provider;
import javax.inject.Singleton;
import java.io.File;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-07-07
 */
@Configuration
@EnableMongoRepositories
public class ApplicationBeansConfiguration {

    private static final String DATABASE_NAME = "webprotege";


    @Bean
    @DataDirectory
    public File provideDataDirectory(DataDirectoryProvider provider) {
        return provider.get();
    }


    @Bean
    UploadsDirectoryProvider provideUploadsDirectoryProvider() {
        return new UploadsDirectoryProvider();
    }

    @Bean
    @UploadsDirectory
    public File provideUploadsDirectory(UploadsDirectoryProvider provider) {
        return provider.get();
    }

    @Bean
    public RootOntologyDocumentFileMatcher provideRootOntologyDocumentFileMatcher() {
        return new RootOntologyDocumentMatcherImpl();
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
    DocumentResolver getDocumentResolver(@UploadsDirectory File uploadsDirectory) {
        return new DocumentResolverImpl(uploadsDirectory);
    }

    @Bean
    ZipInputStreamChecker getZipInputStreamChecker() {
        return new ZipInputStreamChecker();
    }

    @Bean
    ZipArchiveProjectSourcesExtractor getZipArchiveProjectSourcesExtractor(TempFileFactory tempFileFactory) {
        return new ZipArchiveProjectSourcesExtractor(tempFileFactory,
                                                     new RootOntologyDocumentMatcherImpl());
    }

    @Bean
    SingleDocumentProjectSourcesExtractor getSingleDocumentProjectSourcesExtractor() {
        return new SingleDocumentProjectSourcesExtractor();
    }

    @Bean
    UploadedProjectSourcesExtractor getUploadedProjectSourcesExtractor(ZipInputStreamChecker zipInputStreamChecker,
                                                                       ZipArchiveProjectSourcesExtractor zipArchiveProjectSourcesExtractor,
                                                                       SingleDocumentProjectSourcesExtractor singleDocumentProjectSourcesExtractor) {
        return new UploadedProjectSourcesExtractor(zipInputStreamChecker,
                                                   zipArchiveProjectSourcesExtractor,
                                                   singleDocumentProjectSourcesExtractor);
    }

    @Bean
    UploadedOntologiesProcessor getUploadedOntologiesProcessor(DocumentResolver documentResolver,
                                                               Provider<UploadedProjectSourcesExtractor> uploadedProjectSourcesExtractor) {
        return new UploadedOntologiesProcessor(documentResolver,
                                               uploadedProjectSourcesExtractor);
    }

    @Bean
    @Singleton
    OWLDataFactory getOwlDataFactory() {
        return new OWLDataFactoryImpl();
    }

    @Bean
    ProjectDirectoryFactory getProjectDirectoryFactory() {
        return new ProjectDirectoryFactory(getDataDirectory());
    }

    private File getDataDirectory() {
        return null;
    }

    @Bean
    ChangeHistoryFileFactory getChangeHistoryFileFactory(ProjectDirectoryFactory projectDirectoryFactory) {
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
    @Singleton
    ProjectImporterFactory getProjectImporterFactory(UploadedOntologiesProcessor p1,
                                                     DocumentResolver p2,
                                                     RevisionStoreFactory p3) {
        return new ProjectImporterFactory(p1,
                                          p2,
                                          p3);
    }

    @Bean
    @Singleton
    ProjectCache getProjectCache(ProjectComponentFactory projectComponentFactory,
                                 ProjectImporterFactory projectImporterFactory) {
        return new ProjectCache(projectComponentFactory,
                                projectImporterFactory,
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
    ApplicationActionHandlerRegistry getApplicationActionHandlerRegistry(Set<ApplicationActionHandler> handlers) {
        return new ApplicationActionHandlerRegistry(handlers);
    }

    @Bean
    @Singleton
    RoleOracle getRoleOracle() {
        return RoleOracleImpl.get();
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
    UserDetailsManager getUserDetailsManager(UserRecordRepository userRecordRepository) {
        return new UserDetailsManagerImpl(userRecordRepository);
    }

    @Bean
    @Singleton
    DispatchServiceExecutor getDispatchServiceExecutor(ApplicationActionHandlerRegistry actionHandlerRegistry,
                                                               ProjectManager projectManager) {
        return new DispatchServiceExecutorImpl(actionHandlerRegistry,
                                               projectManager);
    }


    @Bean
    @Singleton
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
    UserApiKeyStore userApiKeyStore(MongoTemplate mongoTemplate) {
        return new UserApiKeyStoreImpl(mongoTemplate);
    }

    @Bean
    ApiKeyManager apiKeyManager(UserApiKeyStore userApiKeyStore) {
        return new ApiKeyManager(new ApiKeyHasher(),
                                 userApiKeyStore);
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
    UploadedOntologiesCache uploadedOntologiesCache(UploadedOntologiesProcessor p1,
                                                    @UploadedOntologiesCacheTicker Ticker p2,
                                                    @UploadedOntologiesCacheService ScheduledExecutorService p3) {
        return new UploadedOntologiesCache(p1, p2, p3);
    }

    @Bean
    @UploadedOntologiesCacheTicker
    Ticker uploadedOntologiesCacheTicker() {
        return Ticker.systemTicker();
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
}
