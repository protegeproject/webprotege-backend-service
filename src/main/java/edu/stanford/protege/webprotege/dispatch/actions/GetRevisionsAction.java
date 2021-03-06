package edu.stanford.protege.webprotege.dispatch.actions;

import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.revision.RevisionNumber;
import edu.stanford.protege.webprotege.common.UserId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23 Apr 2018
 */
public class GetRevisionsAction implements ProjectAction<GetRevisionsResult> {

    public static final String CHANNEL = "webprotege.history.GetRevisions";

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final RevisionNumber from;

    @Nonnull
    private final RevisionNumber to;

    @Nonnull
    private final Optional<UserId> author;

    public GetRevisionsAction(@Nonnull ProjectId projectId,
                              @Nonnull RevisionNumber from,
                              @Nonnull RevisionNumber to,
                              @Nullable UserId author) {
        this.projectId = checkNotNull(projectId);
        this.from = checkNotNull(from);
        this.to = checkNotNull(to);
        this.author = Optional.ofNullable(author);
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    @Nonnull
    @Override
    public ProjectId projectId() {
        return projectId;
    }

    @Nonnull
    public RevisionNumber getFrom() {
        return from;
    }

    @Nonnull
    public RevisionNumber getTo() {
        return to;
    }

    @Nonnull
    public Optional<UserId> getAuthor() {
        return author;
    }
}
