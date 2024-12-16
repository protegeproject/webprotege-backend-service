package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.revision.RevisionNumber;

public record ProjectRevision(ProjectId projectId, ChangeRequestId changeRequestId, UserId userId, RevisionNumber revisionNumber) {

}
