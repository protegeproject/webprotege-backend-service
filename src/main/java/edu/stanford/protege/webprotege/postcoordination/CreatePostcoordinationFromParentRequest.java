package edu.stanford.protege.webprotege.postcoordination;

import com.fasterxml.jackson.annotation.*;
import edu.stanford.protege.webprotege.common.*;
import org.semanticweb.owlapi.model.IRI;

@JsonTypeName(CreatePostcoordinationFromParentRequest.CHANNEL)
public record CreatePostcoordinationFromParentRequest(
        @JsonProperty("newEntityIri") IRI newEntityIri,
        @JsonProperty("parentEntityIri") IRI parentEntityIri,
        @JsonProperty("projectId") ProjectId projectId
) implements Request<CreatePostcoordinationFromParentResponse> {

    public final static String CHANNEL = "webprotege.postcoordination.CreateFromParentEntity";

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    public static CreatePostcoordinationFromParentRequest create(IRI newEntityIri, IRI parentEntityIri, ProjectId projectId) {
        return new CreatePostcoordinationFromParentRequest(newEntityIri, parentEntityIri, projectId);
    }

}
