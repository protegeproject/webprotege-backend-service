package edu.stanford.bmir.protege.web.shared.frame;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.HasSubject;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("GetObjectPropertyFrame")
public abstract class GetObjectPropertyFrameAction implements ProjectAction<GetObjectPropertyFrameResult>, HasSubject<OWLObjectProperty> {

    @JsonCreator
    public static GetObjectPropertyFrameAction create(@JsonProperty("projectId") ProjectId projectId,
                                                      @JsonProperty("subject") OWLObjectProperty subject) {
        return new AutoValue_GetObjectPropertyFrameAction(projectId, subject);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @Override
    public abstract OWLObjectProperty getSubject();
}
