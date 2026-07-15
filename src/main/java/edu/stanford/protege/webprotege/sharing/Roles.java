package edu.stanford.protege.webprotege.sharing;

import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.authorization.RoleId;

import java.util.Collection;
import java.util.Optional;

import static edu.stanford.protege.webprotege.access.BuiltInRole.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13/03/16
 */
public class Roles {

    /**
     * The four {@link RoleId}s that represent a sharing permission (i.e. the ones that
     * {@link #toSharingPermission(Collection)} and {@link #fromSharingPermission(SharingPermission)}
     * translate to and from a {@link SharingPermission}). Used to compute the subset of a subject's
     * roles that should be preserved (as opposed to replaced) when their sharing permission changes.
     */
    public static final ImmutableSet<RoleId> SHARING_ROLE_IDS = ImmutableSet.of(
            CAN_VIEW.getRoleId(), CAN_COMMENT.getRoleId(), CAN_EDIT.getRoleId(), CAN_MANAGE.getRoleId());

    public  static Optional<SharingPermission> toSharingPermission(Collection<RoleId> roles) {
        if(roles.contains(CAN_MANAGE.getRoleId())) {
            return Optional.of(SharingPermission.MANAGE);
        }
        else if(roles.contains(CAN_EDIT.getRoleId())) {
            return Optional.of(SharingPermission.EDIT);
        }
        else if(roles.contains(CAN_COMMENT.getRoleId())) {
            return Optional.of(SharingPermission.COMMENT);
        }
        else if(roles.contains(CAN_VIEW.getRoleId())) {
            return Optional.of(SharingPermission.VIEW);
        }
        else {
            return Optional.empty();
        }
    }

    public static ImmutableSet<RoleId> fromSharingPermission(SharingPermission sharingPermission) {
        switch (sharingPermission) {
            case MANAGE:
                return ImmutableSet.of(CAN_MANAGE.getRoleId());
            case EDIT:
                return ImmutableSet.of(CAN_EDIT.getRoleId());
            case COMMENT:
                return ImmutableSet.of(CAN_COMMENT.getRoleId());
            case VIEW:
                return ImmutableSet.of(CAN_VIEW.getRoleId());
            default:
                return ImmutableSet.of();
        }
    }
}
