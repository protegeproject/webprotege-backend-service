package edu.stanford.protege.webprotege.usage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.entity.EntityNode;
import edu.stanford.protege.webprotege.project.HasProjectId;
import edu.stanford.protege.webprotege.common.ProjectId;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/07/2013
 */
@AutoValue

@JsonTypeName("GetUsage")
public abstract class GetUsageResult implements Result, HasProjectId {

    @JsonCreator
    public static GetUsageResult create(@JsonProperty("projectId") ProjectId projectId,
                                        @JsonProperty("entityNode") EntityNode entityNode,
                                        @JsonProperty("usageReferences") List<UsageReference> usageReferences,
                                        @JsonProperty("totalUsageCount") int totalUsageCount) {
        return new AutoValue_GetUsageResult(projectId, entityNode, usageReferences, totalUsageCount);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @Nonnull
    public abstract EntityNode getEntityNode();

    @Nonnull
    public abstract List<UsageReference> getUsageReferences();

    public abstract int getTotalUsageCount();
}
