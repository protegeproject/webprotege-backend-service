package edu.stanford.protege.webprotege.card;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.Response;
import edu.stanford.protege.webprotege.project.HasProjectId;

import java.util.List;

@JsonTypeName("webprotege.cards.GetEntityCardDescriptors")
public record GetEntityCardDescriptorsResponse(@JsonProperty("projectId") ProjectId projectId,
                                               @JsonProperty("descriptors") List<CardDescriptor> descriptors,
                                               @JsonProperty("writableCards") List<CardId> writeableCards) implements Response, HasProjectId {


    public GetEntityCardDescriptorsResponse(@JsonProperty("projectId") ProjectId projectId,
                                            @JsonProperty("descriptors") List<CardDescriptor> descriptors,
                                            @JsonProperty("writableCards") List<CardId> writeableCards) {
        this.projectId = projectId;
        this.descriptors = List.copyOf(descriptors);
        this.writeableCards = List.copyOf(writeableCards);
    }
}
