package edu.stanford.protege.webprotege.linearization;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.Response;
import org.semanticweb.owlapi.model.IRI;

import java.util.Optional;

@JsonTypeName(GetParentThatIsLinearizationPathParentRequest.CHANNEL)
public record GetParentThatIsLinearizationPathParentResponse(
        @JsonProperty("parentAsLinearizationPathParent") Optional<IRI> parentAsLinearizationPathParent
) implements Response {
}
