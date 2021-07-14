package edu.stanford.protege.webprotege.inject;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheFactory;
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
public class FileSystemConfigurationModule {

    
    @DataDirectory
    public File provideDataDirectory(DataDirectoryProvider provider) {
        return provider.get();
    }


    
    @UploadsDirectory
    public File provideUploadsDirectory(UploadsDirectoryProvider provider) {
        return provider.get();
    }

    
    public RootOntologyDocumentFileMatcher provideRootOntologyDocumentFileMatcher(RootOntologyDocumentMatcherImpl impl) {
        return impl;
    }

    
    public TempFileFactory provideTempFileFactory(TempFileFactoryImpl impl) {
        return impl;
    }

    
    public MustacheFactory providesMustacheFactory() {
        return new DefaultMustacheFactory();
    }

    
    @CommentNotificationEmailTemplate
    public OverridableFile provideCommentNotificationTemplateFile(OverridableFileFactory factory) {
        return factory.getOverridableFile("templates/comment-notification-email-template.html");
    }

    
    @CommentNotificationEmailTemplate
    public FileContents providesCommentNotificationTemplate(@CommentNotificationEmailTemplate OverridableFile file) {
        return new FileContents(file);
    }

    
    @PasswordResetEmailTemplate
    public OverridableFile providePasswordResetEmailTemplate(OverridableFileFactory factory) {
        return factory.getOverridableFile("templates/password-reset-email-template.html");
    }

    
    @CommentNotificationSlackTemplate
    public OverridableFile provideCommentNotificationSlackTemplate(OverridableFileFactory factory) {
        return factory.getOverridableFile("templates/comment-notification-slack-template.json");
    }

    
    @CommentNotificationSlackTemplate
    public FileContents providesCommentNotificationSlackTemplate(@CommentNotificationSlackTemplate OverridableFile file) {
        return new FileContents(file);
    }

    
    @PasswordResetEmailTemplate
    public FileContents providesPasswordResetEmailTemplate(@PasswordResetEmailTemplate OverridableFile file) {
        return new FileContents(file);
    }

    
    @WatchNotificationEmailTemplate
    OverridableFile provideWatchNotificationEmailTemplateFile(OverridableFileFactory factory) {
        return factory.getOverridableFile("templates/watch-notification-email-template.html");
    }

    
    @WatchNotificationEmailTemplate
    FileContents provideWatchNotificationEmailTemplate(@WatchNotificationEmailTemplate OverridableFile file) {
        return new FileContents(file);
    }
}

