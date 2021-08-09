package edu.stanford.protege.webprotege.entity;

import com.google.common.base.MoreObjects;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.common.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/11/2013
 */
public class LookupEntitiesAction implements ProjectAction<LookupEntitiesResult> {

    private ProjectId projectId;

    private EntityLookupRequest entityLookupRequest;

    @SuppressWarnings("unused")
    private LookupEntitiesAction() {
    }

    /**
     * Creates a LookupEntitiesAction to perform the specified lookup in the specified project.
     * @param projectId The {@link ProjectId} that identifies the project which entities should be looked up in.  Not {@code null}.
     * @param entityLookupRequest The lookup request. Not {@code null}.
     * @throws  NullPointerException if any parameters are {@code null}.
     */
    private LookupEntitiesAction(ProjectId projectId, EntityLookupRequest entityLookupRequest) {
        this.projectId = checkNotNull(projectId);
        this.entityLookupRequest = checkNotNull(entityLookupRequest);
    }

    public static LookupEntitiesAction create(ProjectId projectId, EntityLookupRequest entityLookupRequest) {
        return new LookupEntitiesAction(projectId, entityLookupRequest);
    }

    /**
     * Gets the {@link ProjectId} that identifies the project in which the lookup will be performed.
     * @return The {@link ProjectId}.  Not {@code null}.
     */
    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    /**
     * Gets the {@link EntityLookupRequest} that describes the lookup to be performed.
     * @return The {@link EntityLookupRequest}. Not {@code null}.
     */
    public EntityLookupRequest getEntityLookupRequest() {
        return entityLookupRequest;
    }


    @Override
    public int hashCode() {
        return "LookupEntitiesAction".hashCode() + projectId.hashCode() + entityLookupRequest.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof LookupEntitiesAction)) {
            return false;
        }
        LookupEntitiesAction other = (LookupEntitiesAction) o;
        return this.projectId.equals(other.projectId) && this.entityLookupRequest.equals(other.entityLookupRequest);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("LookupEntitiesAction")
                          .addValue(projectId)
                          .addValue(entityLookupRequest).toString();
    }
}
