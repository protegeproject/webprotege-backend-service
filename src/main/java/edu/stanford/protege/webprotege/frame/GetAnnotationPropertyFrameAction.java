package edu.stanford.protege.webprotege.frame;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.project.ProjectId;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
@AutoValue

@JsonTypeName("GetAnnotationPropertyFrame")
public abstract class GetAnnotationPropertyFrameAction implements ProjectAction<GetAnnotationPropertyFrameResult> {

    @JsonCreator
    public static GetAnnotationPropertyFrameAction create(@JsonProperty("subject") OWLAnnotationProperty subject,
                                                          @JsonProperty("projectId") ProjectId projectId) {
        return new AutoValue_GetAnnotationPropertyFrameAction(projectId, subject);
    }

    @Nonnull
    public abstract ProjectId getProjectId();

    public abstract OWLAnnotationProperty getSubject();

}
