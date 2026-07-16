package edu.stanford.protege.webprotege.sharing;

import com.google.common.collect.ImmutableMap;
import edu.stanford.protege.webprotege.app.PlaceUrl;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.filemanager.FileContents;
import edu.stanford.protege.webprotege.mail.SendMail;
import edu.stanford.protege.webprotege.project.ProjectDetailsRepository;
import edu.stanford.protege.webprotege.project.ProjectDetails;
import edu.stanford.protege.webprotege.templates.TemplateEngine;
import edu.stanford.protege.webprotege.templates.TemplateObjectsBuilder;
import edu.stanford.protege.webprotege.user.UserDetails;
import edu.stanford.protege.webprotege.user.UserDetailsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.PreDestroy;
import jakarta.inject.Inject;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Sends an email to a person who has been shared with but does not yet have a WebProtege account,
 * inviting them to sign in.  Emails are dispatched on a single-thread executor so a slow or failing
 * mail server never blocks or breaks the save that triggered the invitation.
 */
public class SharingInvitationEmailer {

    private static final String INVITER = "inviter";

    private static final Logger logger = LoggerFactory.getLogger(SharingInvitationEmailer.class);

    private final ProjectDetailsRepository projectDetailsRepository;

    private final UserDetailsManager userDetailsManager;

    private final FileContents templateFile;

    private final TemplateEngine templateEngine;

    private final PlaceUrl placeUrl;

    private final SendMail sendMail;

    private final ExecutorService executor = Executors.newSingleThreadExecutor(runnable -> {
        Thread thread = Executors.defaultThreadFactory().newThread(runnable);
        thread.setName(thread.getName().replace("thread", "sharing-invitation-email-thread"));
        return thread;
    });

    @Inject
    public SharingInvitationEmailer(@Nonnull ProjectDetailsRepository projectDetailsRepository,
                                    @Nonnull UserDetailsManager userDetailsManager,
                                    @Nonnull FileContents templateFile,
                                    @Nonnull TemplateEngine templateEngine,
                                    @Nonnull PlaceUrl placeUrl,
                                    @Nonnull SendMail sendMail) {
        this.projectDetailsRepository = checkNotNull(projectDetailsRepository);
        this.userDetailsManager = checkNotNull(userDetailsManager);
        this.templateFile = checkNotNull(templateFile);
        this.templateEngine = checkNotNull(templateEngine);
        this.placeUrl = checkNotNull(placeUrl);
        this.sendMail = checkNotNull(sendMail);
    }

    /**
     * Submits an invitation email for asynchronous delivery.  Returns immediately; delivery
     * failures are logged but never surfaced to the caller.
     *
     * @param projectId         The project the person was invited to.
     * @param recipientEmail    The email address as the owner typed it.
     * @param invitedBy         The user who shared the project.
     */
    public void sendInvitationEmail(@Nonnull ProjectId projectId,
                                    @Nonnull String recipientEmail,
                                    @Nonnull UserId invitedBy) {
        executor.submit(() -> {
            try {
                doSendInvitationEmail(projectId, recipientEmail, invitedBy);
            } catch (RuntimeException e) {
                logger.error("Could not send a sharing invitation email for project {}: {}",
                             projectId, e.toString());
            }
        });
    }

    private void doSendInvitationEmail(ProjectId projectId, String recipientEmail, UserId invitedBy) {
        // Structural guard on the template file rather than string-matching FileContents' error text:
        // skip the send if the file is absent or its contents are empty.
        if (!templateFile.exists()) {
            logger.error("Not sending a sharing invitation email for project {} - the email template " +
                         "file does not exist.", projectId);
            return;
        }
        String template = templateFile.getContents();
        if (template.isBlank()) {
            logger.error("Not sending a sharing invitation email for project {} - the email template " +
                         "is empty.", projectId);
            return;
        }
        String recipient = recipientEmail.trim();
        // Sanitize the values that flow into the (unescaped) subject header - a display name or project
        // name carrying a CR/LF could otherwise inject headers. The Mustache body is HTML-escaped.
        String projectName = sanitizeForHeader(projectDetailsRepository.findOne(projectId)
                                                                       .map(ProjectDetails::getDisplayName)
                                                                       .orElse("a project"));
        String inviterName = sanitizeForHeader(userDetailsManager.getUserDetails(invitedBy)
                                                                 .map(UserDetails::getDisplayName)
                                                                 .orElse(invitedBy.id()));
        ImmutableMap<String, Object> objects =
                TemplateObjectsBuilder.builder()
                                      .withProjectDisplayName(projectName)
                                      .withApplicationUrl(placeUrl.getApplicationUrl())
                                      .with(INVITER, inviterName)
                                      .build();
        String body = templateEngine.populateTemplate(template, objects);
        String subject = String.format("[%s] You have been invited to collaborate on WebProtege", projectName);
        sendMail.sendMail(List.of(recipient), subject, body);
    }

    private static String sanitizeForHeader(String value) {
        // Remove CR/LF and any other control characters so they cannot break out of the subject line.
        return value.replaceAll("\\p{Cntrl}", "");
    }

    @PreDestroy
    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
