package edu.stanford.protege.webprotege.linearization;

import com.fasterxml.jackson.annotation.*;
import edu.stanford.protege.webprotege.common.Request;
import org.semanticweb.owlapi.model.IRI;

@JsonTypeName(CreateLinearizationFromParentRequest.CHANNEL)
public record CreateLinearizationFromParentRequest(@JsonProperty("newEntityIri") IRI newEntityIri,
                                                   @JsonProperty("parentEntityIri") IRI parentEntityIri)
        implements Request<CreateLinearizationFromParentResponse> {

    public final static String CHANNEL = "webprotege.linearization.CreateFromParentEntity";

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    public static CreateLinearizationFromParentRequest create(IRI newEntityIri, IRI parentEntityIri) {
        return new CreateLinearizationFromParentRequest(newEntityIri, parentEntityIri);
    }

}
