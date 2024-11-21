package edu.stanford.protege.webprotege.icd.actions;

import com.fasterxml.jackson.annotation.*;
import edu.stanford.protege.webprotege.common.*;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;

@JsonTypeName(GetEntityCommentsAction.CHANNEL)
public record GetEntityCommentsAction(@JsonProperty("projectId") ProjectId projectId,
                                      @JsonProperty("entityIri") String entityIri) implements ProjectAction<GetEntityCommentsResponse> {
    public static final String CHANNEL = "icatx.webprotege.discussions.GetEntityComments";

    public static GetEntityCommentsAction create(ProjectId projectId,
                                                 String entityIri) {
        return new GetEntityCommentsAction(projectId, entityIri);
    }


    @Override
    public String getChannel() {
        return CHANNEL;
    }
}