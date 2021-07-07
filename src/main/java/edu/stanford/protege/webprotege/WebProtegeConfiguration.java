package edu.stanford.protege.webprotege;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheFactory;
import com.google.common.collect.ImmutableList;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.AccessManagerImpl;
import edu.stanford.protege.webprotege.access.RoleOracle;
import edu.stanford.protege.webprotege.access.RoleOracleImpl;
import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.app.*;
import edu.stanford.protege.webprotege.auth.*;
import edu.stanford.protege.webprotege.change.OntologyChangeRecordTranslator;
import edu.stanford.protege.webprotege.change.OntologyChangeRecordTranslatorImpl;
import edu.stanford.protege.webprotege.chgpwd.PasswordResetEmailTemplate;
import edu.stanford.protege.webprotege.chgpwd.ResetPasswordMailer;
import edu.stanford.protege.webprotege.dispatch.ApplicationActionHandler;
import edu.stanford.protege.webprotege.dispatch.DispatchServiceExecutor;
import edu.stanford.protege.webprotege.dispatch.impl.ApplicationActionHandlerRegistry;
import edu.stanford.protege.webprotege.dispatch.impl.DispatchServiceExecutorImpl;
import edu.stanford.protege.webprotege.filemanager.FileContents;
import edu.stanford.protege.webprotege.inject.*;
import edu.stanford.protege.webprotege.inject.project.ProjectDirectoryFactory;
import edu.stanford.protege.webprotege.issues.CommentNotificationEmailTemplate;
import edu.stanford.protege.webprotege.jackson.ObjectMapperProvider;
import edu.stanford.protege.webprotege.lang.DefaultDisplayNameSettingsFactory;
import edu.stanford.protege.webprotege.mail.MessageIdGenerator;
import edu.stanford.protege.webprotege.mail.MessagingExceptionHandlerImpl;
import edu.stanford.protege.webprotege.mail.SendMailImpl;
import edu.stanford.protege.webprotege.permissions.ProjectPermissionsManager;
import edu.stanford.protege.webprotege.permissions.ProjectPermissionsManagerImpl;
import edu.stanford.protege.webprotege.perspective.*;
import edu.stanford.protege.webprotege.project.*;
import edu.stanford.protege.webprotege.revision.RevisionStoreFactory;
import edu.stanford.protege.webprotege.templates.TemplateEngine;
import edu.stanford.protege.webprotege.upload.DocumentResolver;
import edu.stanford.protege.webprotege.upload.DocumentResolverImpl;
import edu.stanford.protege.webprotege.upload.UploadedOntologiesProcessor;
import edu.stanford.protege.webprotege.user.*;
import edu.stanford.protege.webprotege.util.DisposableObjectManager;
import edu.stanford.protege.webprotege.util.TempFileFactory;
import edu.stanford.protege.webprotege.util.TempFileFactoryImpl;
import edu.stanford.protege.webprotege.util.ZipInputStreamChecker;
import edu.stanford.protege.webprotege.watches.WatchNotificationEmailTemplate;
import edu.stanford.protege.webprotege.webhook.*;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import javax.inject.Provider;
import javax.inject.Singleton;
import java.io.File;
import java.util.Properties;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-07-07
 */
@Configuration
public class WebProtegeConfiguration {

    private static final String DATABASE_NAME = "webprotege";


    @Bean
    @DataDirectory
    public File provideDataDirectory(DataDirectoryProvider provider) {
        return provider.get();
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
    public TempFileFactory provideTempFileFactory() {
        return new TempFileFactoryImpl();
    }

    @Bean
    public MustacheFactory providesMustacheFactory() {
        return new DefaultMustacheFactory();
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
    @PasswordResetEmailTemplate
    public OverridableFile providePasswordResetEmailTemplate(OverridableFileFactory factory) {
        return factory.getOverridableFile("templates/password-reset-email-template.html");
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
    @PasswordResetEmailTemplate
    public FileContents providesPasswordResetEmailTemplate(@PasswordResetEmailTemplate OverridableFile file) {
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
    ProjectComponentFactory getProjectComponentFactory(ServerComponent serverComponent) {
        return new ProjectComponentFactoryImpl(serverComponent);
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
    OntologyChangeRecordTranslator getOntologyChangeRecordTranslator() {
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
    ProjectImporterFactory getProjectImporterFactory(Provider<UploadedOntologiesProcessor> uploadedOntologiesProcessorProvider,
                                                     Provider<DocumentResolver> documentResolverProvider,
                                                     Provider<RevisionStoreFactory> revisionStoreFactoryProvider) {
        return new ProjectImporterFactory(uploadedOntologiesProcessorProvider,
                                          documentResolverProvider,
                                          revisionStoreFactoryProvider);
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
    ProjectAccessManager getProjectAccessManager(MongoDatabase mongoDatabase) {
        return new ProjectAccessManagerImpl(mongoDatabase);
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
    MongoDatabase getMongoDatabase(MongoClient mongoClient) {
        return mongoClient.getDatabase(DATABASE_NAME);
    }

    @Bean
    @Singleton
    RoleOracle getRoleOracle() {
        return RoleOracleImpl.get();
    }

    @Bean
    Morphia getMorphia() {
        return new Morphia();
    }

    @Bean
    MongoClient getMongoClient() {
        return new MongoClient();
    }


    @Bean
    Datastore getDatastore(Morphia morphia, MongoClient mongoClient) {
        return morphia.createDatastore(mongoClient, DATABASE_NAME);
    }

    @Bean
    @Singleton
    AccessManager getAccessManager(RoleOracle roleOracle,
                                   Datastore datastore) {
        return new AccessManagerImpl(roleOracle, datastore);
    }

    @Bean
    UserRecordRepository getUserRecordRepository(MongoDatabase database) {
        return new UserRecordRepository(database, new UserRecordConverter());
    }

    @Bean
    UserDetailsManager getUserDetailsManager(UserRecordRepository userRecordRepository) {
        return new UserDetailsManagerImpl(userRecordRepository);
    }

    @Bean
    UserInSessionFactory getUserInSessionFactory(AccessManager accessManager,
                                                 UserDetailsManager userDetailsManager) {
        return new UserInSessionFactory(accessManager, userDetailsManager);
    }

    @Bean
    @Singleton
    DispatchServiceExecutor getDispatchServiceExecutorImpl(ApplicationActionHandlerRegistry actionHandlerRegistry,
                                                               ProjectManager projectManager,
                                                               UserInSessionFactory userInSessionFactory) {
        return new DispatchServiceExecutorImpl(actionHandlerRegistry,
                                               projectManager,
                                               userInSessionFactory);
    }


    @Bean
    @Singleton
    public ActionExecutor getActionExecutor(DispatchServiceExecutor dispatchServiceExecutor) {
        return new ActionExecutor(dispatchServiceExecutor);
    }

    @Bean
    @Singleton
    public ObjectMapper getObjectMapper() {
        return new ObjectMapperProvider().get();
    }

    @Bean
    @Singleton
    public ServerComponent getServerComponent() {
        return DaggerServerComponent.create();
    }

    @Bean
    @Singleton
    public ApplicationDisposablesManager getApplicationDisposablesManager() {
        return getServerComponent().getApplicationDisposablesManager();
    }

    @Bean
    public DisposableObjectManager getDisposableObjectManager() {
        return new DisposableObjectManager();
    }

    @Singleton
    @Bean
    ProjectDetailsRepository getProjectDetailsRepository(MongoDatabase mongoDatabase,
                                                         ObjectMapper objectMapper) {
        return new ProjectDetailsRepository(mongoDatabase, objectMapper);
    }

    @Bean
    @Singleton
    SlackWebhookRepository getSlackWebhookRepository(Datastore datastore) {
        return new SlackWebhookRepositoryImpl(datastore);
    }

    @Bean
    @Singleton
    WebhookRepository getWebhookRepository(Datastore datastore) {
        return new WebhookRepositoryImpl(datastore);
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
    @Singleton
    AuthenticationManager getAuthenticationManager(UserRecordRepository userRecordRepository) {
        return new AuthenticationManagerImpl(userRecordRepository, new PasswordDigestAlgorithm(Md5MessageDigestAlgorithm::new),
                                             new SaltProvider());
    }

    @Bean
    ProjectPermissionsManager getProjectPermissionsManager(AccessManager accessManager,
                                                           ProjectDetailsRepository projectDetailsRepository) {
        return new ProjectPermissionsManagerImpl(accessManager,
                                                 projectDetailsRepository);
    }

    @Bean
    UserActivityManager getUserActivityManager(Datastore datastore) {
        return new UserActivityManager(datastore);
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
    ResetPasswordMailer getResetPasswordMailer(SendMailImpl sendMail,
                                               TemplateEngine templateEngine,
                                               PlaceUrl placeUrl,
                                               ApplicationNameSupplier applicationNameSupplier,
                                               @PasswordResetEmailTemplate FileContents templateContents) {
        return new ResetPasswordMailer(sendMail,
                                       templateEngine,
                                       templateContents,
                                       placeUrl,
                                       applicationNameSupplier);
    }

    @Bean
    @Singleton
    ApplicationPreferencesStore getApplicationPreferencesStore(Datastore datastore) {
        return new ApplicationPreferencesStore(datastore);
    }

    @Bean
    @Singleton
    ApplicationSettingsManager getApplicationSettingsManager(AccessManager accessManager,
                                                             ApplicationPreferencesStore applicationPreferencesStore) {
        return new ApplicationSettingsManager(accessManager,
                                              applicationPreferencesStore);
    }

    @Bean
    ApplicationNameSupplier getApplicationNameSupplier(ApplicationPreferencesStore preferencesStore) {
        return new ApplicationNameSupplier(preferencesStore);
    }

    @Bean
    ApplicationSchemeSupplier getApplicationSchemeSupplier(ApplicationPreferencesStore preferencesStore) {
        return new ApplicationSchemeSupplier(preferencesStore);
    }

    @Bean
    ApplicationHostSupplier getApplicationHostSupplier(ApplicationPreferencesStore preferencesStore) {
        return new ApplicationHostSupplier(preferencesStore);
    }

    @Bean
    ApplicationPortSupplier getApplicationPortSupplier(ApplicationPreferencesStore preferencesStore) {
        return new ApplicationPortSupplier(preferencesStore);
    }

    @Bean
    ApplicationPathSupplier getApplicationPathSupplier(ApplicationPreferencesStore preferencesStore) {
        return new ApplicationPathSupplier(preferencesStore);
    }

    @Bean
    EntityTypePerspectiveMapper getEntityTypePerspectiveMapper() {
        return new EntityTypePerspectiveMapper();
    }

    @Bean
    @Singleton
    PerspectiveDescriptorRepository getPerspectiveDescriptorRepository(MongoDatabase database,
                                                                       ObjectMapper objectMapper) {
        return new PerspectiveDescriptorRepositoryImpl(database, objectMapper);
    }

    @Bean
    @Singleton
    PerspectiveLayoutRepository getPerspectiveLayoutRepository(MongoDatabase database, ObjectMapper objectMapper) {
        return new PerspectiveLayoutRepositoryImpl(database, objectMapper);
    }

    @Bean
    @Singleton
    BuiltInPerspectiveLoader getBuiltInPerspectiveLoader(ObjectMapper objectMapper) {
        return new BuiltInPerspectiveLoader(objectMapper);
    }

    @Bean
    @Singleton
    BuiltInPerspectivesProvider getBuiltInPerspectivesProvider(Provider<BuiltInPerspectiveLoader> builtInPerspectiveLoaderProvider) {
        return new BuiltInPerspectivesProvider(builtInPerspectiveLoaderProvider);
    }

    @Bean
    @Singleton
    ImmutableList<BuiltInPerspective> getBuiltInPerspectives(BuiltInPerspectivesProvider builtInPerspectivesProvider) {
        return builtInPerspectivesProvider.getBuiltInPerspectives();
    }

    @Bean
    PerspectivesManager getPerspectivesManager(ImmutableList<BuiltInPerspective> builtInPerspectives,
                                               PerspectiveDescriptorRepository perspectiveDescriptorRepository,
                                               PerspectiveLayoutRepository perspectiveLayoutRepository) {
        return new PerspectivesManagerImpl(builtInPerspectives,
                                           perspectiveDescriptorRepository,
                                           perspectiveLayoutRepository);
    }
}
