package edu.stanford.protege.webprotege.issues;

import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.project.HasProjectId;
import edu.stanford.protege.webprotege.common.ProjectId;

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
