package edu.stanford.protege.webprotege.frame;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.HasSubject;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.project.ProjectId;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 */
@AutoValue

@JsonTypeName("GetNamedIndividualFrame")
public abstract class GetNamedIndividualFrameAction implements ProjectAction<GetNamedIndividualFrameResult>, HasSubject<OWLNamedIndividual> {


    @JsonCreator
    public static GetNamedIndividualFrameAction create(@JsonProperty("projectId") ProjectId projectId,
                                                       @JsonProperty("subject") OWLNamedIndividual subject) {
        return new AutoValue_GetNamedIndividualFrameAction(projectId, subject);
    }

    /**
     * Get the {@link ProjectId}.
     *
     * @return The {@link ProjectId}.  Not {@code null}.
     */
    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    /**
     * Gets the subject of this object.
     *
     * @return The subject.  Not {@code null}.
     */
    @Override
    public abstract OWLNamedIndividual getSubject();
}
