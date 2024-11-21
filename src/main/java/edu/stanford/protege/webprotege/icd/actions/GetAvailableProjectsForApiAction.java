package edu.stanford.protege.webprotege.icd.actions;

import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.dispatch.*;

@JsonTypeName(GetAvailableProjectsForApiAction.CHANNEL)
public record GetAvailableProjectsForApiAction() implements Action<GetAvailableProjectsForApiResult> {
    public static final String CHANNEL = "webprotege.projects.GetAvailableProjectsForApi";

    public static GetAvailableProjectsForApiAction create() {
        return new GetAvailableProjectsForApiAction();
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
