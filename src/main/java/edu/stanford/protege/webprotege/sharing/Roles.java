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

    public  static Optional<SharingPermission> toSharingPermission(Collection<RoleId> roles) {
        if(roles.contains(CAN_MANAGE.getRoleId())) {
            return Optional.of(SharingPermission.MANAGE);
        }
        else if(roles.contains(CAN_EDIT.getRoleId())) {
            return Optional.of(SharingPermission.EDIT);
        }
        else if(roles.contains(ICD_CAN_EDIT.getRoleId())) {
            return Optional.of(SharingPermission.ICD_EDIT);
        }else if(roles.contains(CAN_COMMENT.getRoleId())) {
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
        return switch (sharingPermission) {
            case MANAGE -> ImmutableSet.of(CAN_MANAGE.getRoleId());
            case EDIT -> ImmutableSet.of(CAN_EDIT.getRoleId());
            case ICD_EDIT -> ImmutableSet.of(ICD_CAN_EDIT.getRoleId());
            case COMMENT -> ImmutableSet.of(CAN_COMMENT.getRoleId());
            case VIEW -> ImmutableSet.of(CAN_VIEW.getRoleId());
            default -> ImmutableSet.of();
        };
    }
}
