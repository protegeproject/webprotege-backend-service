package edu.stanford.protege.webprotege.linearization;

import com.fasterxml.jackson.annotation.*;
import edu.stanford.protege.webprotege.common.Response;
import org.semanticweb.owlapi.model.IRI;

import java.util.Set;

@JsonTypeName(GetParentsThatAreLinearizationPathParents.CHANNEL)
public record GetParentsThatAreLinearizationPathParentsResponse(
        @JsonProperty("parentsThatAreLinearizationPathParents") Set<IRI> parentsThatAreLinearizationPathParents,

        @JsonProperty("existingLinearizationParents") Set<IRI> existingLinearizationParents
) implements Response {
}
