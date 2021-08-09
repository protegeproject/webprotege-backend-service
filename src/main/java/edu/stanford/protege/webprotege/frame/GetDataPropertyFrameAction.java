package edu.stanford.protege.webprotege.frame;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.HasSubject;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.semanticweb.owlapi.model.OWLDataProperty;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
@AutoValue

@JsonTypeName("GetDataPropertyFrame")
public abstract class GetDataPropertyFrameAction implements ProjectAction<GetDataPropertyFrameResult>, HasSubject<OWLDataProperty> {

    @JsonCreator
    public static GetDataPropertyFrameAction create(@JsonProperty("projectId") ProjectId projectId,
                                                    @JsonProperty("subject") OWLDataProperty subject) {
        return new AutoValue_GetDataPropertyFrameAction(subject, projectId);
    }

    public abstract OWLDataProperty getSubject();

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();
}
