package edu.stanford.protege.webprotege.forms;

import com.fasterxml.jackson.annotation.*;
import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.common.*;

@JsonTypeName(SetProjectFormsRequest.CHANNEL)
public record SetProjectFormsRequest(@JsonProperty("changeRequestId") ChangeRequestId changeRequestId,
                                     @JsonProperty("projectId") ProjectId projectId,
                                     @JsonProperty("formDescriptors") ImmutableList<FormDescriptor> formDescriptors,
                                     @JsonProperty("formSelectors") ImmutableList<EntityFormSelector> formSelectors) implements ProjectRequest<SetProjectFormsResult> {

    public static final String CHANNEL = "webprotege.forms.SetProjectForms";

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
