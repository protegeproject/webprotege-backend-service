package edu.stanford.protege.webprotege.frame;

import com.google.common.base.Objects;
import edu.stanford.protege.webprotege.HasSubject;
import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.common.ContentChangeRequest;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.entity.OWLEntityData;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 18/03/2014
 */
public class SetManchesterSyntaxFrameAction implements ProjectAction<SetManchesterSyntaxFrameResult>, HasSubject<OWLEntity>, HasFreshEntities, ContentChangeRequest {

    public static final String CHANNEL = "webprotege.mansyntax.SetManchesterSyntaxFrame";

    private final ProjectId projectId;

    private final OWLEntity subject;

    private final String fromRendering;

    private final String toRendering;

    private final Set<OWLEntityData> freshEntities;

    private final String commitMessage;

    private ChangeRequestId changeRequestId;

    private SetManchesterSyntaxFrameAction(ProjectId projectId,
                                           OWLEntity subject,
                                           String fromRendering,
                                           String toRendering,
                                           Set<OWLEntityData> freshEntities,
                                           Optional<String> commitMessage, ChangeRequestId changeRequestId) {
        this.projectId = checkNotNull(projectId);
        this.subject = checkNotNull(subject);
        this.fromRendering = checkNotNull(fromRendering);
        this.toRendering = checkNotNull(toRendering);
        this.freshEntities = new HashSet<>(freshEntities);
        this.commitMessage = checkNotNull(commitMessage).orElse(null);
        this.changeRequestId = checkNotNull(changeRequestId);
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    public static SetManchesterSyntaxFrameAction create(ProjectId projectId,
                                                        OWLEntity subject,
                                                        String fromRendering,
                                                        String toRendering,
                                                        Set<OWLEntityData> freshEntities,
                                                        Optional<String> commitMessage) {
        return new SetManchesterSyntaxFrameAction(projectId,
                                                  subject,
                                                  fromRendering,
                                                  toRendering,
                                                  freshEntities,
                                                  commitMessage,
                                                  ChangeRequestId.generate());
    }

    @Override
    public ChangeRequestId changeRequestId() {
        return changeRequestId;
    }

    @Nonnull
    @Override
    public ProjectId projectId() {
        return projectId;
    }

    @Override
    public OWLEntity getSubject() {
        return subject;
    }

    public String getFromRendering() {
        return fromRendering;
    }

    public String getToRendering() {
        return toRendering;
    }

    public Optional<String> getCommitMessage() {
        return Optional.ofNullable(commitMessage);
    }

    public Set<OWLEntityData> freshEntities() {
        return new HashSet<OWLEntityData>(freshEntities);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId, subject, fromRendering, toRendering, freshEntities, commitMessage);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SetManchesterSyntaxFrameAction)) {
            return false;
        }
        SetManchesterSyntaxFrameAction other = (SetManchesterSyntaxFrameAction) obj;
        return this.projectId.equals(other.projectId)
                && this.subject.equals(other.subject)
                && this.fromRendering.equals(other.fromRendering)
                && this.toRendering.equals(other.toRendering)
                && this.freshEntities.equals(other.freshEntities)
                && this.getCommitMessage().equals(other.getCommitMessage());
    }


    @Override
    public String toString() {
        return toStringHelper("SetManchesterSyntaxFrameAction")
                .addValue(projectId)
                .add("subject", subject)
                .add("from", fromRendering)
                .add("to", toRendering)
                .add("freshEntities", freshEntities)
                .add("commitMessage", getCommitMessage())
                .toString();
    }
}


