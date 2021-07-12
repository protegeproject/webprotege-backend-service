package edu.stanford.protege.webprotege.chgpwd;

import com.github.mustachejava.DefaultMustacheFactory;
import edu.stanford.protege.webprotege.app.ApplicationNameSupplier;
import edu.stanford.protege.webprotege.app.PlaceUrl;
import edu.stanford.protege.webprotege.filemanager.FileContents;
import edu.stanford.protege.webprotege.inject.OverridableFile;
import edu.stanford.protege.webprotege.mail.MessagingExceptionHandler;
import edu.stanford.protege.webprotege.mail.SendMailImpl;
import edu.stanford.protege.webprotege.templates.TemplateEngine;
import edu.stanford.protege.webprotege.user.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 20 Mar 2017
 */
@RunWith(MockitoJUnitRunner.class)
public class PasswordResetMailer_IT {

    private final String applicationUrl = "The Application URL";

    private final String theAppName = "TheAppName";

    @Mock
    private SendMailImpl sendMailImpl;

    @Mock
    private PlaceUrl placeUrl;

    @Captor
    private ArgumentCaptor<String> bodyCaptor;

    private static final String TEMPLATE_PATH = "templates/password-reset-email-template.html";

    private final UserId userId = UserId.getUserId("John Smith" );

    private final String emailAddress = "john.smith@somewhere.com";

    private final String theNewPassword = "TheNewPassword";

    @Mock
    private ApplicationNameSupplier appNameProvider;

    @Before
    public void setUp() throws Exception {
        when(placeUrl.getApplicationUrl()).thenReturn(applicationUrl);
        when(appNameProvider.get()).thenReturn(theAppName);
        OverridableFile overridableFile = new OverridableFile(TEMPLATE_PATH, new File("/tmp/data"));
        FileContents templateFile = new FileContents(overridableFile);
        TemplateEngine templateEngine = new TemplateEngine(DefaultMustacheFactory::new);
        ResetPasswordMailer mailer = new ResetPasswordMailer(sendMailImpl,
                                                             templateEngine,
                                                             templateFile,
                                                             placeUrl,
                                                             appNameProvider);
        mailer.sendEmail(userId, emailAddress, theNewPassword, e -> {});
        verify(sendMailImpl, times(1)).sendMail(
                eq(singletonList(emailAddress)),
                eq("Your password has been reset"),
                bodyCaptor.capture(), any(MessagingExceptionHandler.class));
    }

    @Test
    public void shouldSendEmailContainingUserName() {
        assertThat(bodyCaptor.getValue(), containsString(userId.getUserName()));
    }

    @Test
    public void shouldSendEmailContainingPassword() {
        assertThat(bodyCaptor.getValue(), containsString(theNewPassword));
    }

    @Test
    public void shouldSendEmailContainingApplicationUrl() {
        assertThat(bodyCaptor.getValue(), containsString(applicationUrl));
    }

    @Test
    public void shouldSendEmailContainingApplicationName() {
        assertThat(bodyCaptor.getValue(), containsString(theAppName));
    }
}