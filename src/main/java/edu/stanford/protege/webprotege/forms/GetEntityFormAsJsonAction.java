package edu.stanford.protege.webprotege.forms;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.ProjectRequest;

import static edu.stanford.protege.webprotege.forms.GetEntityFormAsJsonAction.CHANNEL;

@JsonTypeName(CHANNEL)
public record GetEntityFormAsJsonAction(@JsonProperty("projectId") ProjectId projectId,
                                        @JsonProperty("formId") FormId formId) implements ProjectRequest<GetEntityFormAsJsonResult> {

    public static final String CHANNEL = "webprotege.forms.GetEntityFormAsJson";


    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
