package edu.stanford.protege.webprotege.access;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.authorization.Capability;
import edu.stanford.protege.webprotege.authorization.RoleId;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.List;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Jan 2017
 *
 * A role associates a role Id with a set of actions that can be performed.  Hierarchical roles are supported.
 */
@Immutable
public final class Role {

    @Nonnull
    private final RoleId roleId;

    @Nonnull
    private final List<RoleId> parents;

    @Nonnull
    private final List<Capability> capabilities;


    public Role(@Nonnull RoleId roleId,
                @Nonnull List<RoleId> parents,
                @Nonnull List<Capability> capabilities) {
        this.roleId = checkNotNull(roleId);
        this.parents = ImmutableList.copyOf(checkNotNull(parents));
        this.capabilities = ImmutableList.copyOf(checkNotNull(capabilities));
    }

    @Nonnull
    public RoleId getRoleId() {
        return roleId;
    }

    @Nonnull
    public List<RoleId> getParents() {
        return parents;
    }

    @Nonnull
    public List<Capability> getCapabilities() {
        return capabilities;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(roleId, parents, capabilities);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Role)) {
            return false;
        }
        Role other = (Role) obj;
        return this.roleId.equals(other.roleId)
                && this.parents.equals(other.parents)
                && this.capabilities.equals(other.capabilities);
    }


    @Override
    public String toString() {
        return toStringHelper("Role")
                .addValue(roleId)
                .add("parents", parents)
                .add("actions", capabilities)
                .toString();
    }
}
