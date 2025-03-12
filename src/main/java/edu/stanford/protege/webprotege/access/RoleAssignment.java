package edu.stanford.protege.webprotege.access;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.authorization.BasicCapability;
import edu.stanford.protege.webprotege.authorization.Capability;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Jan 2017
 *
 * A persistence structure for role assignments.  This assumes the persistence is provided by
 * MongoDb, which is access via Morphia.
 */
@Document(collection = "RoleAssignments")
@CompoundIndexes({
        @CompoundIndex(def = "{'userName':1, 'projectId':1}", unique = true)
})
public class RoleAssignment {

    public static final String USER_NAME = "userName";

    public static final String PROJECT_ID = "projectId";

    public static final String ACTION_CLOSURE = "actionClosure";

    public static final String ROLE_CLOSURE = "roleClosure";

    @Nullable
    @SuppressWarnings("unused")
    private ObjectId id;

    @Nullable
    private String userName;

    @Nullable
    private String projectId;

    private List<String> assignedRoles = ImmutableList.of();

    private List<String> roleClosure = ImmutableList.of();

    private List<Capability> capabilityClosure = ImmutableList.of();


    public RoleAssignment(@Nullable String userName,
                          @Nullable String projectId,
                          @Nonnull List<String> assignedRoles,
                          @Nonnull List<String> roleClosure,
                          @Nonnull List<Capability> capabilityClosure) {
        this.userName = userName;
        this.projectId = projectId;
        this.assignedRoles = ImmutableList.copyOf(checkNotNull(assignedRoles));
        this.roleClosure = ImmutableList.copyOf(checkNotNull(roleClosure));
        this.capabilityClosure = ImmutableList.copyOf(checkNotNull(capabilityClosure));
    }

    @JsonCreator
    public static RoleAssignment fromJson(@Nullable @JsonProperty("userName") String userName,
                                          @Nullable @JsonProperty("projectId") String projectId,
                                          @Nonnull @JsonProperty("assignedRoles") List<String> assignedRoles,
                                          @Nonnull @JsonProperty("roleClosure") List<String> roleClosure,
                                          @Nullable @JsonProperty("actionClosure") List<String> actionClosure,
                                          @Nullable @JsonProperty("capabilityClosure") List<Capability> capabilityClosure) {
        if(actionClosure != null) {
            return new RoleAssignment(userName,
                    projectId,
                    assignedRoles,
                    roleClosure,
                    actionClosure.stream().map(a -> (Capability) BasicCapability.valueOf(a)).toList());
        }
        else {
            return new RoleAssignment(
                    userName,
                    projectId,
                    assignedRoles,
                    roleClosure,
                    java.util.Objects.requireNonNullElse(capabilityClosure, ImmutableList.of())
            );
        }
    }

    @Nonnull
    @JsonProperty("projectId")
    public Optional<String> getProjectId() {
        return Optional.ofNullable(projectId);
    }

    @Nonnull
    @JsonProperty("userName")
    public Optional<String> getUserName() {
        return Optional.ofNullable(userName);
    }

    @Nonnull
    @JsonProperty("assignedRoles")
    public List<String> getAssignedRoles() {
        return ImmutableList.copyOf(assignedRoles);
    }

    @Nonnull
    @JsonProperty("roleClosure")
    public List<String> getRoleClosure() {
        return ImmutableList.copyOf(roleClosure);
    }

    @JsonProperty("capabilityClosure")
    public List<Capability> getCapabilityClosure() {
        return ImmutableList.copyOf(capabilityClosure);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userName, projectId, assignedRoles, roleClosure, capabilityClosure);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof RoleAssignment)) {
            return false;
        }
        RoleAssignment other = (RoleAssignment) obj;
        return Objects.equal(userName, other.userName)
                && Objects.equal(projectId, other.projectId)
                && this.assignedRoles.equals(other.assignedRoles)
                && this.roleClosure.equals(other.roleClosure)
                && this.capabilityClosure.equals(other.capabilityClosure);
    }

    @Override
    public String toString() {
        return toStringHelper("RoleAssignment")
                .add("userName", userName)
                .add("projectId", projectId)
                .add("assignedRoles", assignedRoles)
                .add("roleClosure", roleClosure)
                .add("actionClosure", capabilityClosure)
                .toString();
    }
}
