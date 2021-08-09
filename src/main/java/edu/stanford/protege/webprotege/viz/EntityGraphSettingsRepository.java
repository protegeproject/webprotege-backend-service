package edu.stanford.protege.webprotege.viz;

import edu.stanford.protege.webprotege.persistence.Repository;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-06
 */
public interface EntityGraphSettingsRepository extends Repository {

    void saveSettings(@Nonnull ProjectUserEntityGraphSettings settings);

    @Nonnull
    ProjectUserEntityGraphSettings getProjectDefaultSettings(@Nonnull ProjectId projectId);

    /**
     * Gets the entity graph settings, or the project default if a user does
     * not have specific settings associated with them.
     * @param projectId The project Id
     * @param userId The user Id
     * @return The settings
     */
    @Nonnull
    ProjectUserEntityGraphSettings getSettingsForUserOrProjectDefault(@Nonnull ProjectId projectId,
                                                                      @Nonnull UserId userId);


}
