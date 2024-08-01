package edu.stanford.protege.webprotege.forms;

import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.ChangeRequest;
import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.Request;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-14
 */


@JsonTypeName("webprotege.forms.CopyFormDescriptors")
public record CopyFormDescriptorsAction(ChangeRequestId changeRequestId,
                                        ProjectId toProject,
                                        ProjectId fromProject,
                                        List<FormId> formIds) implements Request<CopyFormDescriptorsResult>, ChangeRequest {

    public static final String CHANNEL = "webprotege.forms.CopyFormDescriptors";

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
