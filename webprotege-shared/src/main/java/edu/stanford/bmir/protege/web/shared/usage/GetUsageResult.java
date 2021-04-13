package edu.stanford.bmir.protege.web.shared.usage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableCollection;
import edu.stanford.bmir.protege.web.shared.HasSignature;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.project.HasProjectId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/07/2013
 */
@AutoValue
@GwtCompatible(serializable = true)
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
