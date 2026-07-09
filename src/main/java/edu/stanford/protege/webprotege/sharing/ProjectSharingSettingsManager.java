package edu.stanford.protege.webprotege.sharing;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public interface ProjectSharingSettingsManager {

    /**
     * @param actingUserId The user making this change. Implementations must
     *                     not leave this user without the access they had
     *                     before the call, even if their entry in
     *                     {@code projectSharingSettings} (if present) fails
     *                     to resolve - otherwise a user can lock themselves
     *                     out of a project they were just editing.
     */
    void setProjectSharingSettings(@Nonnull UserId actingUserId, @Nonnull ProjectSharingSettings projectSharingSettings);

    ProjectSharingSettings getProjectSharingSettings(ProjectId projectId);
}
