package edu.stanford.protege.webprotege.inject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Ticker;
import com.google.common.collect.ImmutableList;
import com.mongodb.client.MongoClients;
import dagger.Module;
import dagger.Provides;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.AccessManagerImpl;
import edu.stanford.protege.webprotege.access.RoleOracle;
import edu.stanford.protege.webprotege.access.RoleOracleImpl;
import edu.stanford.protege.webprotege.api.UserApiKeyStore;
import edu.stanford.protege.webprotege.api.UserApiKeyStoreImpl;
import edu.stanford.protege.webprotege.app.ApplicationDisposablesManager;
import edu.stanford.protege.webprotege.app.ApplicationSettingsManager;
import edu.stanford.protege.webprotege.app.WebProtegeProperties;
import edu.stanford.protege.webprotege.auth.AuthenticationManager;
import edu.stanford.protege.webprotege.auth.AuthenticationManagerImpl;
import edu.stanford.protege.webprotege.change.OntologyChangeRecordTranslator;
import edu.stanford.protege.webprotege.change.OntologyChangeRecordTranslatorImpl;
import edu.stanford.protege.webprotege.dispatch.ActionHandlerRegistry;
import edu.stanford.protege.webprotege.dispatch.DispatchServiceExecutor;
import edu.stanford.protege.webprotege.dispatch.impl.ActionHandlerRegistryImpl;
import edu.stanford.protege.webprotege.dispatch.impl.DispatchServiceExecutorImpl;
import edu.stanford.protege.webprotege.download.DownloadGeneratorExecutor;
import edu.stanford.protege.webprotege.download.FileTransferExecutor;
import edu.stanford.protege.webprotege.form.EntityFormRepository;
import edu.stanford.protege.webprotege.form.EntityFormRepositoryImpl;
import edu.stanford.protege.webprotege.form.EntityFormSelectorRepository;
import edu.stanford.protege.webprotege.form.EntityFormSelectorRepositoryImpl;
import edu.stanford.protege.webprotege.index.IndexUpdatingService;
import edu.stanford.protege.webprotege.jackson.ObjectMapperProvider;
import edu.stanford.protege.webprotege.mail.*;
import edu.stanford.protege.webprotege.mansyntax.render.*;
import edu.stanford.protege.webprotege.owlapi.NonCachingDataFactory;
import edu.stanford.protege.webprotege.permissions.ProjectPermissionsManager;
import edu.stanford.protege.webprotege.permissions.ProjectPermissionsManagerImpl;
import edu.stanford.protege.webprotege.perspective.*;
import edu.stanford.protege.webprotege.project.*;
import edu.stanford.protege.webprotege.search.EntitySearchFilterRepository;
import edu.stanford.protege.webprotege.search.EntitySearchFilterRepositoryImpl;
import edu.stanford.protege.webprotege.sharing.ProjectSharingSettingsManager;
import edu.stanford.protege.webprotege.sharing.ProjectSharingSettingsManagerImpl;
import edu.stanford.protege.webprotege.upload.*;
import edu.stanford.protege.webprotege.user.*;
import edu.stanford.protege.webprotege.util.DisposableObjectManager;
import edu.stanford.protege.webprotege.viz.EntityGraphEdgeLimit;
import edu.stanford.protege.webprotege.viz.EntityGraphSettingsRepository;
import edu.stanford.protege.webprotege.viz.EntityGraphSettingsRepositoryImpl;
import edu.stanford.protege.webprotege.watches.WatchRecordRepository;
import edu.stanford.protege.webprotege.watches.WatchRecordRepositoryImpl;
import edu.stanford.protege.webprotege.webhook.SlackWebhookRepository;
import edu.stanford.protege.webprotege.webhook.SlackWebhookRepositoryImpl;
import edu.stanford.protege.webprotege.webhook.WebhookRepository;
import edu.stanford.protege.webprotege.webhook.WebhookRepositoryImpl;
import edu.stanford.protege.webprotege.app.ApplicationSettings;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntityProvider;
import org.springframework.data.mongodb.core.MongoTemplate;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17/02/16
 */
@Module
public class ApplicationModule {

    private static final int MAX_FILE_DOWNLOAD_THREADS = 5;

    private static final int INDEX_UPDATING_THREADS = 10;


    @ApplicationSingleton
    @Provides
    public ObjectMapper provideObjectMapper(ObjectMapperProvider provider) {
        return provider.get();
    }

    @Provides
    @ApplicationSingleton
    public AuthenticationManager provideAuthenticationManager(AuthenticationManagerImpl impl) {
        return impl;
    }

    @Provides
    @ApplicationSingleton
    public ProjectDetailsManager provideProjectDetailsManager(ProjectDetailsManagerImpl impl) {
        return impl;
    }

    @Provides
    @ApplicationSingleton
    public ProjectPermissionsManager provideProjectPermissionsManager(ProjectPermissionsManagerImpl impl) {
        return impl;
    }

    @Provides
    @ApplicationSingleton
    public ProjectSharingSettingsManager provideProjectSharingSettingsManager(ProjectSharingSettingsManagerImpl impl) {
        return impl;
    }

    @Provides
    @ApplicationSingleton
    public UserDetailsManager provideUserDetailsManager(UserDetailsManagerImpl impl) {
        return impl;
    }

    @Provides
    @ApplicationSingleton
    public HasGetUserIdByUserIdOrEmail provideHasGetUserIdByUserIdOrEmail(UserDetailsManager manager) {
        return manager;
    }

    @ApplicationSingleton
    @Provides
    public PerspectivesManager providesPerspectivesManager(PerspectivesManagerImpl impl) {
        return impl;
    }

    @Provides
    public HasUserIds providesHasUserIds() {
        return Collections::emptySet;
    }

    @Provides
    @ApplicationSingleton
    public ProjectManager provideOWLAPIProjectManager(ProjectCache projectCache, ProjectAccessManager projectAccessManager) {
        return new ProjectManager(projectCache, projectAccessManager);
    }

    @ApplicationSingleton
    @Provides
    public ProjectAccessManager provideProjectAccessManager(ProjectAccessManagerImpl projectAccessManager) {
        projectAccessManager.ensureIndexes();
        return projectAccessManager;
    }

    @Provides
    @ApplicationSingleton
    public ActionHandlerRegistry provideActionHandlerRegistry(ActionHandlerRegistryImpl impl) {
        return impl;
    }

    @Provides
    public DispatchServiceExecutor provideDispatchServiceExecutor(DispatchServiceExecutorImpl impl) {
        return impl;
    }

    @Provides
    @ApplicationSingleton
    @ApplicationDataFactory
    public OWLDataFactory provideOWLDataFactory() {
        return new NonCachingDataFactory(new OWLDataFactoryImpl());
    }

    @Provides
    @ApplicationDataFactory
    @ApplicationSingleton
    public OWLEntityProvider provideOWLProvider(@ApplicationDataFactory OWLDataFactory dataFactory) {
        return dataFactory;
    }

    @ApplicationSingleton
    public UserActivityManager provideUserActivityManager(UserActivityManagerProvider provider) {
        return provider.get();
    }

    @Provides
    @ApplicationSingleton
    public WebProtegeProperties provideWebProtegeProperties(WebProtegePropertiesProvider povider) {
        return povider.get();
    }

    @Provides
    @MailProperties
    @ApplicationSingleton
    public Properties provideMailProperties(MailPropertiesProvider provider) {
        return provider.get();
    }

    @Provides
    public SendMail provideSendMail(SendMailImpl manager) {
        return manager;
    }

    @Provides
    public MessagingExceptionHandler provideMessagingExceptionHandler(MessagingExceptionHandlerImpl handler) {
        return handler;
    }

    @Provides
    @ApplicationSingleton
    public WatchRecordRepository provideWatchRecordRepository(WatchRecordRepositoryImpl impl) {
        impl.ensureIndexes();
        return impl;
    }

    @Provides
    @ApplicationSingleton
    public AccessManager provideAccessManager(AccessManagerImpl impl) {
        return impl;
    }

    @Provides
    @ApplicationSingleton
    public RoleOracle provideRoleOracle() {
        return RoleOracleImpl.get();
    }

    @Provides
    @DownloadGeneratorExecutor
    @ApplicationSingleton
    public ExecutorService provideDownloadGeneratorExecutorService(ApplicationExecutorsRegistry executorsRegistry) {
        // Might prove to be too much of a bottle neck.  For now, this limits the memory we need
        // to generate downloads
        var executor = Executors.newSingleThreadExecutor(r -> {
            Thread thread = Executors.defaultThreadFactory().newThread(r);
            thread.setName(thread.getName().replace("thread", "Download-Generator"));
            return thread;
        });
        executorsRegistry.registerService(executor, "Download-Generator-Service");
        return executor;
    }

    @Provides
    @FileTransferExecutor
    @ApplicationSingleton
    public ExecutorService provideFileTransferExecutorService(ApplicationExecutorsRegistry executorsRegistry) {
        var executor = Executors.newFixedThreadPool(MAX_FILE_DOWNLOAD_THREADS, r -> {
            Thread thread = Executors.defaultThreadFactory().newThread(r);
            thread.setName(thread.getName().replace("thread", "Download-Streamer"));
            return thread;
        });
        executorsRegistry.registerService(executor, "Download-Streaming-Service");
        return executor;
    }

    @Provides
    @IndexUpdatingService
    @ApplicationSingleton
    public ExecutorService provideIndexUpdatingExecutorService(ApplicationExecutorsRegistry executorsRegistry) {
        var executor = Executors.newFixedThreadPool(INDEX_UPDATING_THREADS, r -> {
            Thread thread = Executors.defaultThreadFactory().newThread(r);
            thread.setName(thread.getName().replace("thread", "Index-Updater"));
            return thread;
        });
        executorsRegistry.registerService(executor, "Index-Updater");
        return executor;
    }

    @Provides
    @UploadedOntologiesCacheService
    @ApplicationSingleton
    public ScheduledExecutorService provideUploadedOntologiesCacheService(ApplicationExecutorsRegistry executorsRegistry) {
        var executor = Executors.newSingleThreadScheduledExecutor();
        executorsRegistry.registerService(executor, "Uploaded-Ontologies-Cache-Service");
        return executor;
    }

    @Provides
    public WebhookRepository providesWebhookRepository(WebhookRepositoryImpl impl) {
        return impl;
    }

    @Provides
    @ApplicationSingleton
    public SlackWebhookRepository provideSlackWebhookRepository(SlackWebhookRepositoryImpl impl) {
        impl.ensureIndexes();
        return impl;
    }

    @Provides
    public ApplicationSettings provideApplicationSettings(ApplicationSettingsManager manager) {
        return manager.getApplicationSettings();
    }

    @Provides
    LiteralStyle provideDefaultLiteralStyle() {
        return LiteralStyle.REGULAR;
    }

    @Provides
    HttpLinkRenderer provideDefaultHttpLinkRenderer(DefaultHttpLinkRenderer renderer) {
        return renderer;
    }

    @Provides
    LiteralRenderer provideLiteralRenderer(MarkdownLiteralRenderer renderer) {
        return renderer;
    }

    @Provides
    ItemStyleProvider provideItemStyleProvider(DefaultItemStyleProvider provider) {
        return provider;
    }

    @Provides
    NestedAnnotationStyle provideNestedAnnotationStyle() {
        return NestedAnnotationStyle.COMPACT;
    }

    @ApplicationSingleton
    @Provides
    UserApiKeyStore provideUserApiKeyStore(UserApiKeyStoreImpl impl) {
        impl.ensureIndexes();
        return impl;
    }

    @ApplicationSingleton
    @Provides
    ApplicationDisposablesManager provideApplicationDisposableObjectManager(DisposableObjectManager disposableObjectManager) {
        return new ApplicationDisposablesManager(disposableObjectManager);
    }

    @ApplicationSingleton
    @Provides
    BuiltInPrefixDeclarations provideBuiltInPrefixDeclarations(@Nonnull BuiltInPrefixDeclarationsLoader loader) {
        return loader.getBuiltInPrefixDeclarations();
    }

    @Provides
    @DormantProjectTime
    @ApplicationSingleton
    long providesProjectDormantTime(WebProtegeProperties properties) {
        return properties.getProjectDormantTime();
    }

    @Provides
    Ticker provideTicker() {
        return Ticker.systemTicker();
    }

    @Provides
    @ApplicationSingleton
    UploadedOntologiesCache provideUploadedOntologiesCache(UploadedOntologiesProcessor processor,
                                                           @UploadedOntologiesCacheService ScheduledExecutorService cacheService,
                                                           Ticker ticker,
                                                           ApplicationDisposablesManager disposableObjectManager) {
        var cache = new UploadedOntologiesCache(processor, ticker, cacheService);
        cache.start();
        disposableObjectManager.register(cache);
        return cache;
    }

    @Provides
    DocumentResolver provideDocumentResolver(DocumentResolverImpl impl) {
        return impl;
    }


    @Provides
    OntologyChangeRecordTranslator provideOntologyChangeRecordTranslator(OntologyChangeRecordTranslatorImpl impl) {
        return impl;
    }

    @Provides
    EntityFormRepository provideEntityFormRepository(EntityFormRepositoryImpl impl) {
        impl.ensureIndexes();
        return impl;
    }

    @Provides
    @ApplicationSingleton
    EntityGraphSettingsRepository provideProjectEntityGraphSettingsRepository(
            EntityGraphSettingsRepositoryImpl impl) {
        return impl;
    }

    @Provides
    EntityFormSelectorRepository provideFormSelectorRepository(EntityFormSelectorRepositoryImpl impl) {
        impl.ensureIndexes();
        return impl;
    }

    @Provides
    @EntityGraphEdgeLimit
    int provideEntityGraphEdgeLimit(WebProtegeProperties properties) {
        return properties.getEntityGraphEdgeLimit().orElse(3000);
    }

    @Provides
    @ApplicationSingleton
    EntitySearchFilterRepository provideEntitySearchFilterRepository(EntitySearchFilterRepositoryImpl impl) {
        impl.ensureIndexes();
        return impl;
    }

    @Provides
    @ApplicationSingleton
    ImmutableList<BuiltInPerspective> provideBuiltInProjectPerspectives(BuiltInPerspectivesProvider builtInPerspectivesProvider) {
        return builtInPerspectivesProvider.getBuiltInPerspectives();
    }

    @Provides
    @ApplicationSingleton
    PerspectiveDescriptorRepository providePerspectiveDescriptorsRepository(PerspectiveDescriptorRepositoryImpl impl) {
        impl.ensureIndexes();
        return impl;
    }

    @Provides
    @ApplicationSingleton
    PerspectiveLayoutRepository providePerspectiveLayoutsRepository(PerspectiveLayoutRepositoryImpl impl) {
        impl.ensureIndexes();
        return impl;
    }

    @Provides
    MongoTemplate mongoTemplate() {
        return new MongoTemplate(MongoClients.create(), "webprotege");
    }
}
