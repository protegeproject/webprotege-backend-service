package edu.stanford.protege.webprotege.issues;

import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.project.HasProjectId;
import edu.stanford.protege.webprotege.common.ProjectId;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Jul 16
 */
public class GetIssuesResult implements Result, HasProjectId {

    private ProjectId projectId;

    private List<Issue> issues;

    private GetIssuesResult() {
    }

    public GetIssuesResult(ProjectId projectId, List<Issue> issueCurClients) {
        this.projectId = projectId;
        this.issues = new ArrayList<>(issueCurClients);
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    public List<Issue> getIssues() {
        return new ArrayList<>(issues);
    }
}
