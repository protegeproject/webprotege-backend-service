package edu.stanford.protege.webprotege.forms;

import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.*;
import edu.stanford.protege.webprotege.entity.FreshEntityIri;
import edu.stanford.protege.webprotege.forms.data.FormData;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-30
 */
@JsonTypeName("webprotege.forms.CreateEntityFromFormData")
public record CreateEntityFromFormDataAction(@Nonnull ChangeRequestId changeRequestId,
                                             @Nonnull ProjectId projectId,
                                             @Nonnull EntityType<?> entityType,
                                             @Nonnull FreshEntityIri freshEntityIri,
                                             @Nonnull FormData formData) implements ProjectRequest<CreateEntityFromFormDataResult>, ContentChangeRequest {

    public static final String CHANNEL = "webprotege.forms.CreateEntityFromFormData";

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
