package edu.stanford.protege.webprotege.frame;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.HasSubject;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.project.HasProjectId;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.semanticweb.owlapi.model.OWLClass;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 */
@AutoValue

@JsonTypeName("GetClassFrame")
public abstract class GetClassFrameAction implements ProjectAction<GetClassFrameResult>, HasProjectId, HasSubject<OWLClass> {

    @JsonCreator
    public static GetClassFrameAction create(@JsonProperty("subject") OWLClass subject,
                                             @JsonProperty("projectId") ProjectId projectId) {
        return new AutoValue_GetClassFrameAction(projectId, subject);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    /**
     * Gets the subject of this object.
     *
     * @return The subject.  Not {@code null}.
     */
    @Override
    public abstract OWLClass getSubject();
}
