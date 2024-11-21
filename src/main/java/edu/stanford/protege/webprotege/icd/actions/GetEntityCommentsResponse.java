package edu.stanford.protege.webprotege.icd.actions;

import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.icd.dtos.EntityComments;

import static edu.stanford.protege.webprotege.icd.actions.GetEntityCommentsAction.CHANNEL;

@JsonTypeName(CHANNEL)
public record GetEntityCommentsResponse(EntityComments comments) implements Result {

    public static GetEntityCommentsResponse create(EntityComments comments) {
        return new GetEntityCommentsResponse(comments);
    }
}
