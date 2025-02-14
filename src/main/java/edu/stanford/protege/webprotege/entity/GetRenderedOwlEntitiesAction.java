package edu.stanford.protege.webprotege.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;

import java.util.Set;

@JsonTypeName("webprotege.entities.RenderedOwlEntities")
public record GetRenderedOwlEntitiesAction(@JsonProperty("entityIris") Set<String> entityIris, @JsonProperty("projectId") ProjectId projectId) implements ProjectAction<GetRenderedOwlEntitiesResult> {

    public static String CHANNEL = "webprotege.entities.RenderedOwlEntities";

    @Override
    public ProjectId projectId() {
        return projectId;
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
