package edu.stanford.protege.webprotege.forms;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.common.*;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-23
 */
@JsonTypeName("webprotege.forms.SetProjectFormDescriptors")
public record SetProjectFormDescriptorsAction(@Nonnull ChangeRequestId changeRequestId,
                                              ProjectId projectId,
                                              ImmutableList<FormDescriptor> formDescriptors) implements ProjectRequest<SetProjectFormDescriptorsResult>, ChangeRequest {

    public static final String CHANNEL = "webprotege.forms.SetProjectFormDescriptors";

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
