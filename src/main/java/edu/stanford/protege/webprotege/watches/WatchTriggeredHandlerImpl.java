package edu.stanford.protege.webprotege.watches;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.app.ApplicationNameSupplier;
import edu.stanford.protege.webprotege.app.PlaceUrl;
import edu.stanford.protege.webprotege.entity.OWLEntityData;
import edu.stanford.protege.webprotege.filemanager.FileContents;
import edu.stanford.protege.webprotege.mail.MessageHeader;
import edu.stanford.protege.webprotege.mail.SendMail;
import edu.stanford.protege.webprotege.project.ProjectDetailsManager;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.renderer.RenderingManager;
import edu.stanford.protege.webprotege.templates.TemplateEngine;
import edu.stanford.protege.webprotege.templates.TemplateObjectsBuilder;
import edu.stanford.protege.webprotege.user.UserDetailsManager;
import edu.stanford.protege.webprotege.user.UserId;
import org.semanticweb.owlapi.model.OWLEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static edu.stanford.protege.webprotege.access.BuiltInAction.VIEW_PROJECT;
import static edu.stanford.protege.webprotege.access.ProjectResource.forProject;
import static edu.stanford.protege.webprotege.access.Subject.forUser;
import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/03/15
 */
public class WatchTriggeredHandlerImpl implements WatchTriggeredHandler {

    private final static Logger logger = LoggerFactory.getLogger(WatchTriggeredHandler.class);

    private final ProjectId projectId;

    private final RenderingManager renderingManager;

    private final ApplicationNameSupplier applicationNameSupplier;

    private final PlaceUrl placeUrl;

    private final SendMail sendMail;

    private final UserDetailsManager userDetailsManager;

    private final ProjectDetailsManager projectDetailsManager;

    private final AccessManager accessManager;

    private final TemplateEngine templateEngine;

    private final FileContents watchTemplate;

    @Inject
    public WatchTriggeredHandlerImpl(ProjectId projectId,
                                     RenderingManager renderingManager,
                                     ApplicationNameSupplier applicationNameSupplier,
                                     AccessManager accessManager,
                                     PlaceUrl placeUrl,
                                     SendMail sendMail,
                                     UserDetailsManager userDetailsManager,
                                     ProjectDetailsManager projectDetailsManager,
                                     TemplateEngine templateEngine,
                                     @WatchNotificationEmailTemplate FileContents watchTemplate) {
        this.projectId = projectId;
        this.renderingManager = renderingManager;
        this.applicationNameSupplier = applicationNameSupplier;
        this.accessManager = accessManager;
        this.placeUrl = placeUrl;
        this.sendMail = sendMail;
        this.userDetailsManager = userDetailsManager;
        this.projectDetailsManager = projectDetailsManager;
        this.templateEngine = templateEngine;
        this.watchTemplate = watchTemplate;
    }

    @Override
    public void handleWatchTriggered(@Nonnull Set<UserId> usersToNotify,
                                     @Nonnull OWLEntity modifiedEntity,
                                     @Nonnull UserId byUser) {
        logger.info("{} [WatchTriggeredHandlerImpl] Handling watch triggered for {} by {}, notifying {}",
                    projectId,
                    modifiedEntity,
                    byUser);
        List<String> emailAddresses = usersToNotify.stream()
                                                   // The user should have view permissions to be notified
                                                   .filter(u -> accessManager.hasPermission(forUser(u),
                                                                                            forProject(projectId),
                                                                                            VIEW_PROJECT))
                                                   .map(userDetailsManager::getEmail)
                                                   .filter(Optional::isPresent)
                                                   .map(Optional::get)
                                                   .distinct()
                                                   .collect(toList());
        OWLEntityData modifiedEntityData = renderingManager.getRendering(modifiedEntity);
        Map<String, Object> templateObjects =
                TemplateObjectsBuilder.builder()
                                      .withUserId(byUser)
                                      .withEntity(modifiedEntityData)
                                      .withEntityUrl(placeUrl.getEntityUrl(projectId, modifiedEntity))
                                      .withProjectDetails(projectDetailsManager.getProjectDetails(projectId))
                                      .withApplicationName(applicationNameSupplier.get())
                                      .withProjectUrl(placeUrl.getProjectUrl(projectId))
                                      .build();

        String displayName = projectDetailsManager.getProjectDetails(projectId).getDisplayName();
        String emailSubject = String.format("[%s] Changes made to %s in %s by %s",
                                            displayName,
                                            renderingManager.getShortForm(modifiedEntity),
                                            displayName,
                                            userDetailsManager.getUserDetails(byUser).map(d -> "by " + d.getDisplayName()).orElse(""));
        String emailBody = templateEngine.populateTemplate(watchTemplate.getContents(), templateObjects);
        logger.info("{} Watch triggered by {} on {}.  Notifying {}", projectId, byUser, modifiedEntity, usersToNotify);
        sendMail.sendMail(emailAddresses, emailSubject, emailBody,
                          MessageHeader.inReplyTo(projectId.getId()),
                          MessageHeader.references(projectId.getId()));
    }
}
