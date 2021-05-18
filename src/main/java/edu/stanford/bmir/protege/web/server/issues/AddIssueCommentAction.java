package edu.stanford.bmir.protege.web.server.issues;

import edu.stanford.bmir.protege.web.server.project.HasProjectId;

import edu.stanford.bmir.protege.web.server.dispatch.Action;
import edu.stanford.bmir.protege.web.server.project.ProjectId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28 Sep 2016
 */
public class AddIssueCommentAction implements Action<AddIssueCommentResult>, HasProjectId {


    private ProjectId projectId;

    private int issueNumber;

    private Comment comment;

    public AddIssueCommentAction(ProjectId projectId, int issueNumber, Comment comment) {
        this.projectId = projectId;
        this.issueNumber = issueNumber;
        this.comment = comment;
    }


    private AddIssueCommentAction() {
    }

    public Comment getComment() {
        return comment;
    }

    @Nonnull
    public ProjectId getProjectId() {
        return projectId;
    }

    public int getIssueNumber() {
        return issueNumber;
    }
}
