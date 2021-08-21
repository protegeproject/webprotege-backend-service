package edu.stanford.protege.webprotege.viz;

import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.common.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-07
 */
public class GetProjectEntityGraphDefaultEdgeCriteriaAction implements ProjectAction<GetProjectEntityGraphDefaultEdgeCriteriaResult> {

    public static final String CHANNEL = "webprotege.graph.GetProjectEntityGraphDefaultEdgeCriteria";

    private ProjectId projectId;

    public GetProjectEntityGraphDefaultEdgeCriteriaAction(@Nonnull ProjectId projectId) {
        this.projectId = checkNotNull(projectId);
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Override
    public int hashCode() {
        return projectId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof GetProjectEntityGraphDefaultEdgeCriteriaAction)) {
            return false;
        }
        GetProjectEntityGraphDefaultEdgeCriteriaAction other = (GetProjectEntityGraphDefaultEdgeCriteriaAction) obj;
        return this.projectId.equals(other.projectId);
    }
}
