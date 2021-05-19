package edu.stanford.protege.webprotege.change;

import edu.stanford.protege.webprotege.permissions.PermissionDeniedException;
import edu.stanford.protege.webprotege.user.UserId;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 25/03/2014
 */
public interface HasApplyChanges {
    <R> ChangeApplicationResult<R> applyChanges(UserId userId, ChangeListGenerator<R> changeListGenerator) throws PermissionDeniedException;
}
