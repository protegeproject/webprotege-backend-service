package edu.stanford.protege.webprotege.icd.actions;

import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.*;
import edu.stanford.protege.webprotege.dispatch.Result;

import java.util.Set;

import static edu.stanford.protege.webprotege.icd.actions.CreateClassesFromApiAction.CHANNEL;

@JsonTypeName(CHANNEL)
public record CreateClassesFromApiResult(ChangeRequestId changeRequestId, ProjectId projectId, String newEntityIri) implements Response, Result {
}
