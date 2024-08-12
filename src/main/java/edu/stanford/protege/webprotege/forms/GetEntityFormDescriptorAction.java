package edu.stanford.protege.webprotege.forms;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.ProjectRequest;
import edu.stanford.protege.webprotege.common.Request;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-16
 */
@JsonTypeName("webprotege.forms.GetEntityFormDescriptor")
public record GetEntityFormDescriptorAction(@JsonProperty("projectId") ProjectId projectId,
                                            @JsonProperty("formId") FormId formId) implements ProjectRequest<GetEntityFormDescriptorResult> {

    public static final String CHANNEL = "webprotege.forms.GetEntityFormDescriptor";

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
