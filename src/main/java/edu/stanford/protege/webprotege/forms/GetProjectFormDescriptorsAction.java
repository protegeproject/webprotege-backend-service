package edu.stanford.protege.webprotege.forms;

import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.ProjectRequest;
import edu.stanford.protege.webprotege.common.Request;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-20
 */
@JsonTypeName("webprotege.forms.GetProjectFormDescriptors")
public record GetProjectFormDescriptorsAction(ProjectId projectId) implements ProjectRequest<GetProjectFormDescriptorsResult> {

    public static final String CHANNEL = "webprotege.forms.GetProjectFormDescriptors";

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
