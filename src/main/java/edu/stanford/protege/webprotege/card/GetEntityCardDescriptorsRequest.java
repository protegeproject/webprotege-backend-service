package edu.stanford.protege.webprotege.card;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.ProjectRequest;
import org.semanticweb.owlapi.model.OWLEntity;

@JsonTypeName(GetEntityCardDescriptorsRequest.CHANNEL)
public record GetEntityCardDescriptorsRequest(@JsonProperty("projectId") ProjectId projectId,
                                              @JsonProperty("subject") OWLEntity subject) implements ProjectRequest<GetEntityCardDescriptorsResponse> {


    public static final String CHANNEL = "webprotege.cards.GetEntityCardDescriptors";

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
