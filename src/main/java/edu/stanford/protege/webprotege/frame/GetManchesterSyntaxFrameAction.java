package edu.stanford.protege.webprotege.frame;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.stanford.protege.webprotege.HasSubject;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 18/03/2014
 */
public class GetManchesterSyntaxFrameAction implements ProjectAction<GetManchesterSyntaxFrameResult>, HasSubject<OWLEntity> {

    public static final String CHANNEL = "webprotege.frames.GetManchesterSyntaxFrame";

    private ProjectId projectId;

    private OWLEntity subject;

    /**
     * For serialization purposes only
     */
    private GetManchesterSyntaxFrameAction() {
    }

    private GetManchesterSyntaxFrameAction(ProjectId projectId, OWLEntity subject) {
        this.projectId = checkNotNull(projectId);
        this.subject = checkNotNull(subject);
    }

    @JsonCreator
    public static GetManchesterSyntaxFrameAction create(@JsonProperty("projectId") ProjectId projectId,@JsonProperty("subject") OWLEntity subject) {
        return new GetManchesterSyntaxFrameAction(projectId, subject);
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

    @Override
    public OWLEntity getSubject() {
        return subject;
    }

    @Override
    public int hashCode() {
        return "GetManchesterSyntaxFrameAction".hashCode() + projectId.hashCode() + subject.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof GetManchesterSyntaxFrameAction)) {
            return false;
        }
        GetManchesterSyntaxFrameAction other = (GetManchesterSyntaxFrameAction) o;
        return this.projectId.equals(other.projectId) && this.subject.equals(other.subject);
    }
}
