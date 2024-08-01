package edu.stanford.protege.webprotege.forms;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.ProjectRequest;
import edu.stanford.protege.webprotege.common.Request;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-28
 */


@JsonTypeName("webprotege.forms.GetEntityCreationForms")
public record GetEntityCreationFormsAction(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                           @JsonProperty("parentEntity") @Nonnull OWLEntity parentEntity,
                                           @JsonProperty("entityType") @Nonnull EntityType<?> entityType) implements ProjectRequest<GetEntityCreationFormsResult> {

    public static final String CHANNEL = "webprotege.forms.GetEntityCreationForms";

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
