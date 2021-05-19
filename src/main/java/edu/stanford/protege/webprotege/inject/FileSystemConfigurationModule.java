package edu.stanford.protege.webprotege.inject;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheFactory;
import dagger.Module;
import dagger.Provides;
import edu.stanford.protege.webprotege.chgpwd.PasswordResetEmailTemplate;
import edu.stanford.protege.webprotege.filemanager.FileContents;
import edu.stanford.protege.webprotege.issues.CommentNotificationEmailTemplate;
import edu.stanford.protege.webprotege.project.RootOntologyDocumentFileMatcher;
import edu.stanford.protege.webprotege.project.RootOntologyDocumentMatcherImpl;
import edu.stanford.protege.webprotege.util.TempFileFactory;
import edu.stanford.protege.webprotege.util.TempFileFactoryImpl;
import edu.stanford.protege.webprotege.watches.WatchNotificationEmailTemplate;
import edu.stanford.protege.webprotege.webhook.CommentNotificationSlackTemplate;

import java.io.File;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
@Module
public class FileSystemConfigurationModule {

    @Provides
    @DataDirectory
    public File provideDataDirectory(DataDirectoryProvider provider) {
        return provider.get();
    }


    @Provides
    @UploadsDirectory
    public File provideUploadsDirectory(UploadsDirectoryProvider provider) {
        return provider.get();
    }

    @Provides
    public RootOntologyDocumentFileMatcher provideRootOntologyDocumentFileMatcher(RootOntologyDocumentMatcherImpl impl) {
        return impl;
    }

    @Provides
    public TempFileFactory provideTempFileFactory(TempFileFactoryImpl impl) {
        return impl;
    }

    @Provides
    public MustacheFactory providesMustacheFactory() {
        return new DefaultMustacheFactory();
    }

    @Provides
    @CommentNotificationEmailTemplate
    public OverridableFile provideCommentNotificationTemplateFile(OverridableFileFactory factory) {
        return factory.getOverridableFile("templates/comment-notification-email-template.html");
    }

    @Provides
    @CommentNotificationEmailTemplate
    public FileContents providesCommentNotificationTemplate(@CommentNotificationEmailTemplate OverridableFile file) {
        return new FileContents(file);
    }

    @Provides
    @PasswordResetEmailTemplate
    public OverridableFile providePasswordResetEmailTemplate(OverridableFileFactory factory) {
        return factory.getOverridableFile("templates/password-reset-email-template.html");
    }

    @Provides
    @CommentNotificationSlackTemplate
    public OverridableFile provideCommentNotificationSlackTemplate(OverridableFileFactory factory) {
        return factory.getOverridableFile("templates/comment-notification-slack-template.json");
    }

    @Provides
    @CommentNotificationSlackTemplate
    public FileContents providesCommentNotificationSlackTemplate(@CommentNotificationSlackTemplate OverridableFile file) {
        return new FileContents(file);
    }

    @Provides
    @PasswordResetEmailTemplate
    public FileContents providesPasswordResetEmailTemplate(@PasswordResetEmailTemplate OverridableFile file) {
        return new FileContents(file);
    }

    @Provides
    @WatchNotificationEmailTemplate
    OverridableFile provideWatchNotificationEmailTemplateFile(OverridableFileFactory factory) {
        return factory.getOverridableFile("templates/watch-notification-email-template.html");
    }

    @Provides
    @WatchNotificationEmailTemplate
    FileContents provideWatchNotificationEmailTemplate(@WatchNotificationEmailTemplate OverridableFile file) {
        return new FileContents(file);
    }
}

