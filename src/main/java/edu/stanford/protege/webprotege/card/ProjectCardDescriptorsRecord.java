package edu.stanford.protege.webprotege.card;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.stanford.protege.webprotege.common.ProjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record ProjectCardDescriptorsRecord(@JsonProperty("projectId") ProjectId projectId,
                                           @JsonProperty("cardDescriptors") List<CardDescriptor> cardDescriptors) {

    public ProjectCardDescriptorsRecord(@JsonProperty("projectId") ProjectId projectId, @JsonProperty("cardDescriptors") List<CardDescriptor> cardDescriptors) {
        this.projectId = Objects.requireNonNull(projectId);
        this.cardDescriptors = new ArrayList<>(cardDescriptors);
    }
}
