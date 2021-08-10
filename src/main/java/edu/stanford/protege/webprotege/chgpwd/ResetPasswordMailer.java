package edu.stanford.protege.webprotege.chgpwd;

import edu.stanford.protege.webprotege.app.ApplicationNameSupplier;
import edu.stanford.protege.webprotege.app.PlaceUrl;
import edu.stanford.protege.webprotege.filemanager.FileContents;
import edu.stanford.protege.webprotege.mail.MessagingExceptionHandler;
import edu.stanford.protege.webprotege.mail.SendMailImpl;
import edu.stanford.protege.webprotege.templates.TemplateEngine;
import edu.stanford.protege.webprotege.templates.TemplateObjectsBuilder;
import edu.stanford.protege.webprotege.common.UserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Map;

import static java.util.Collections.singletonList;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 01/10/2014
 */
public class ResetPasswordMailer {

    public static final String SUBJECT = "Your password has been reset";

    private final SendMailImpl sendMailImpl;

    private static final Logger logger = LoggerFactory.getLogger(ResetPasswordMailer.class);

    private final TemplateEngine templateEngine;

    private final FileContents templateFile;

    private final PlaceUrl placeUrl;

    private final ApplicationNameSupplier applicationNameSupplier;

    @Inject
    public ResetPasswordMailer(SendMailImpl sendMailImpl,
                               TemplateEngine templateEngine,
                               @PasswordResetEmailTemplate FileContents templateFile,
                               PlaceUrl placeUrl,
                               ApplicationNameSupplier applicationNameSupplier) {
        this.sendMailImpl = sendMailImpl;
        this.templateEngine = templateEngine;
        this.templateFile = templateFile;
        this.placeUrl = placeUrl;
        this.applicationNameSupplier = applicationNameSupplier;
    }

    public void sendEmail(@Nonnull UserId userId,
                          @Nonnull String emailAddress,
                          @Nonnull String pwd,
                          @Nonnull MessagingExceptionHandler messagingExceptionHandler) {
        Map<String, Object> objects =
                TemplateObjectsBuilder.builder()
                                      .withApplicationName(applicationNameSupplier.get())
                                      .withApplicationUrl(placeUrl.getApplicationUrl())
                                      .withUserId(userId)
                                      .with("pwd", pwd)
                                      .build();

        String template = templateFile.getContents();
        String emailBody = templateEngine.populateTemplate(template, objects);

        sendMailImpl.sendMail(singletonList(emailAddress),
                              SUBJECT,
                              emailBody,
                              ex -> {
                                  logger.info("A password reset email could not be sent to user {} at {}.",
                                              userId.id(),
                                              emailAddress);
                                  messagingExceptionHandler.handleMessagingException(ex);
                              });

    }
}
