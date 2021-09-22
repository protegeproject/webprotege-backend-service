package edu.stanford.protege.webprotege.sharing;

import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.authorization.ProjectResource;
import edu.stanford.protege.webprotege.authorization.RoleId;
import edu.stanford.protege.webprotege.authorization.Subject;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.user.UserDetailsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.*;

import static edu.stanford.protege.webprotege.authorization.Subject.forAnySignedInUser;
import static edu.stanford.protege.webprotege.authorization.Subject.forUser;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toMap;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 05/02/15
 */
public class ProjectSharingSettingsManagerImpl implements ProjectSharingSettingsManager {

    private final Logger logger = LoggerFactory.getLogger(ProjectSharingSettingsManagerImpl.class);

    private final AccessManager accessManager;

    private final UserDetailsManager userLookup;

    @Inject
    public ProjectSharingSettingsManagerImpl(AccessManager accessManager,
                                             UserDetailsManager userLookup) {
        this.accessManager = accessManager;
        this.userLookup = userLookup;
    }

    @Override
    public ProjectSharingSettings getProjectSharingSettings(ProjectId projectId) {
        List<SharingSetting> sharingSettings = new ArrayList<>();
        ProjectResource projectResource = new ProjectResource(projectId);
        Collection<Subject> subjects = accessManager.getSubjectsWithAccessToResource(projectResource);
        subjects.stream()
                .filter(s -> !s.isGuest())
                .filter(s -> s.getUserName().isPresent())
                .map(s -> UserId.valueOf(s.getUserName().get()))
                .forEach(u -> {
                    Collection<RoleId> roles = accessManager.getAssignedRoles(forUser(u), projectResource);
                    Roles.toSharingPermission(roles).ifPresent(
                            p -> sharingSettings.add(new SharingSetting(PersonId.of(u), p)));

                });
        Collection<RoleId> signedInUserRoles = accessManager.getAssignedRoles(forAnySignedInUser(), projectResource);
        Optional<SharingPermission> linkSharing = Roles.toSharingPermission(signedInUserRoles);
        return new ProjectSharingSettings(projectId, linkSharing, sharingSettings);
    }


    @Override
    public void setProjectSharingSettings(ProjectSharingSettings settings) {
        ProjectId projectId = settings.getProjectId();
        ProjectResource projectResource = new ProjectResource(projectId);

        // Remove existing assignments
        accessManager.getSubjectsWithAccessToResource(projectResource)
                .forEach(subject -> accessManager.setAssignedRoles(subject, projectResource, Collections.emptySet()));

        Map<PersonId, SharingSetting> map = settings.getSharingSettings().stream()
                                                    .collect(toMap(SharingSetting::getPersonId, s -> s, (s1, s2) -> s1));
        Optional<SharingPermission> linkSharingPermission = settings.getLinkSharingPermission();
        linkSharingPermission.ifPresent(permission -> {
            Collection<RoleId> roleId = Roles.fromSharingPermission(permission);
            accessManager.setAssignedRoles(forAnySignedInUser(), projectResource, roleId);
        });
        if(!linkSharingPermission.isPresent()) {
            accessManager.setAssignedRoles(forAnySignedInUser(), projectResource, emptySet());
        }
        for (SharingSetting setting : map.values()) {
            PersonId personId = setting.getPersonId();
            Optional<UserId> userId = userLookup.getUserByUserIdOrEmail(personId.getId());
            if (userId.isPresent()) {
                ImmutableSet<RoleId> roles = Roles.fromSharingPermission(setting.getSharingPermission());
                accessManager.setAssignedRoles(forUser(userId.get()),
                                               projectResource,
                                               roles);
            }
            else {
                logger.info("User in sharing setting not found.  An email invitation needs to be sent");
                // TODO
                // We need to send the user an email invitation
            }
        }
    }
}
