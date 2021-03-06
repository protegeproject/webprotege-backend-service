package edu.stanford.protege.webprotege.perspective;

import edu.stanford.protege.webprotege.persistence.Repository;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-31
 */
public interface PerspectiveDescriptorRepository extends Repository {

    void saveDescriptors(@Nonnull PerspectiveDescriptorsRecord perspectiveDescriptors);


    /**
     * Find the {@link PerspectiveDescriptor}s that are specific to a project and a user.
     * @param projectId The project
     * @param userId The user
     */
    @Nonnull
    Optional<PerspectiveDescriptorsRecord> findDescriptors(@Nonnull ProjectId projectId,
                                                           @Nonnull UserId userId);

    /**
     * Find the {@link PerspectiveDescriptor}s that are specific to a project.  There are not
     * user specific descriptors.
     * @param projectId The project
     */
    @Nonnull
    Optional<PerspectiveDescriptorsRecord> findDescriptors(@Nonnull ProjectId projectId);

    /**
     * Find the perspective descriptors that are not project and/or user specific.  These
     * are essentially the descriptors for the default perspectives for new projects.
     */
    @Nonnull
    Optional<PerspectiveDescriptorsRecord> findDescriptors();

    /**
     * Find the perspective descriptors that are not user specific.  This includes project level
     * desecriptors and system level descriptors.
     * @param projectId The project id
     */
    @Nonnull
    Stream<PerspectiveDescriptorsRecord> findProjectAndSystemDescriptors(@Nonnull ProjectId projectId);

    void dropAllDescriptors(@Nonnull ProjectId projectId, @Nonnull UserId userId);
}
