package edu.stanford.protege.webprotege.forms;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.*;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-22
 */


@JsonTypeName("webprotege.forms.UpdateFormDescriptor")
public record UpdateFormDescriptorAction(@JsonProperty("changeRequestId") ChangeRequestId changeRequestId,
                                         @JsonProperty("projectId") @Nonnull ProjectId projectId,
                                         @JsonProperty("formDescriptor") @Nonnull FormDescriptor descriptor) implements ProjectRequest<UpdateFormDescriptorResult>, ChangeRequest {

    public static final String CHANNEL = "webprotege.forms.UpdateFormDescriptor";

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
