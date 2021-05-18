package edu.stanford.bmir.protege.web.server.issues;

import edu.stanford.bmir.protege.web.server.project.HasProjectId;
import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.project.ProjectId;

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
