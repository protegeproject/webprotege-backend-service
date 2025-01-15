package edu.stanford.protege.webprotege.linearization;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.Request;
import org.semanticweb.owlapi.model.IRI;

import java.util.Set;

@JsonTypeName(GetParentThatIsLinearizationPathParentRequest.CHANNEL)
public record GetParentThatIsLinearizationPathParentRequest(
        @JsonProperty("currentEntityIri") IRI currentEntityIri,
        @JsonProperty("parentEntityIris") Set<IRI> parentEntityIris,
        @JsonProperty("projectId") ProjectId projectId
) implements Request<GetParentThatIsLinearizationPathParentResponse> {

    public final static String CHANNEL = "webprotege.linearization.GetParentThatIsLinearizationPathParent";

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    public static GetParentThatIsLinearizationPathParentRequest create(IRI currentEntityIri, Set<IRI> parentEntityIris, ProjectId projectId) {
        return new GetParentThatIsLinearizationPathParentRequest(currentEntityIri, parentEntityIris, projectId);
    }

}
