package edu.stanford.protege.webprotege.usage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nullable;
import java.util.Optional;

import static edu.stanford.protege.webprotege.pagination.PageRequest.DEFAULT_PAGE_SIZE;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/07/2013
 */
@AutoValue

@JsonTypeName("GetUsage")
public abstract class GetUsageAction implements ProjectAction<GetUsageResult> {

    public static GetUsageAction create(OWLEntity subject,
                                        ProjectId projectId) {
        return new AutoValue_GetUsageAction(projectId, subject, null);
    }

    public static GetUsageAction create(OWLEntity subject,
                                        ProjectId projectId,
                                        Optional<UsageFilter> usageFilter) {
        return new AutoValue_GetUsageAction(projectId, subject, usageFilter.orElse(null));
    }

    @JsonCreator
    public static GetUsageAction create(@JsonProperty("subject") OWLEntity subject,
                                        @JsonProperty("projectId") ProjectId projectId,
                                        @JsonProperty("usageFilter") @Nullable UsageFilter usageFilter) {
        return new AutoValue_GetUsageAction(projectId, subject, usageFilter);
    }

    @Override
    public abstract ProjectId getProjectId();

    public abstract OWLEntity getSubject();

    @Nullable
    public abstract UsageFilter getUsageFilterInternal();

    public Optional<UsageFilter> getUsageFilter() {
        return Optional.ofNullable(getUsageFilterInternal());
    }

    public int getPageSize() {
        return DEFAULT_PAGE_SIZE;
    }
}
