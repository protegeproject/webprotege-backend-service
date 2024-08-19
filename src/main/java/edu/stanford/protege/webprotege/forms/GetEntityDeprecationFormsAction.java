package edu.stanford.protege.webprotege.forms;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.ProjectRequest;
import edu.stanford.protege.webprotege.common.Request;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-10-21
 */


@JsonTypeName("webprotege.forms.GetEntityDeprecationForms")
public record GetEntityDeprecationFormsAction(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                              @JsonProperty("term") @Nonnull OWLEntity entity) implements ProjectRequest<GetEntityDeprecationFormsResult> {

    public static final String CHANNEL = "webprotege.forms.GetEntityDeprecationForms";

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
