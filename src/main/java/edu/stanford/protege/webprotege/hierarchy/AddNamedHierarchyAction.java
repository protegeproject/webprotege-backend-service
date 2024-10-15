package edu.stanford.protege.webprotege.hierarchy;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.LanguageMap;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;

@JsonTypeName(AddNamedHierarchyAction.CHANNEL)
public record AddNamedHierarchyAction(@JsonProperty("projectId") ProjectId projectId,
                                      @JsonProperty("label") LanguageMap label,
                                      @JsonProperty("description") LanguageMap description,
                                      @JsonProperty("hierarchyDescriptor") HierarchyDescriptor hierarchyDescriptor) implements ProjectAction<AddNamedHierarchyResponse> {

    public static final String CHANNEL = "webprotege.hierarchies.AddNamedHierarchy";

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
