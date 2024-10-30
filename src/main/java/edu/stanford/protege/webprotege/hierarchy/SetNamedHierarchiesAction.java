package edu.stanford.protege.webprotege.hierarchy;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;

import java.util.List;

@JsonTypeName(SetNamedHierarchiesAction.CHANNEL)
public record SetNamedHierarchiesAction(@JsonProperty("projectId") ProjectId projectId,
                                        @JsonProperty("namedHierarchies") List<NamedHierarchy> namedHierarchies) implements ProjectAction<SetNamedHierarchiesResponse> {

    public static final String CHANNEL = "webprotege.hierarchies.SetNamedHierarchies";

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
