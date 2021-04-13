package edu.stanford.bmir.protege.web.shared.dispatch.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.HasSubject;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("GetNamedIndividualFrame")
public abstract class GetNamedIndividualFrameAction implements ProjectAction<GetNamedIndividualFrameResult>, HasSubject<OWLNamedIndividual> {


    @JsonCreator
    public static GetNamedIndividualFrameAction create(@JsonProperty("projectId") ProjectId projectId,
                                                       @JsonProperty("subject") OWLNamedIndividual subject) {
        return new AutoValue_GetNamedIndividualFrameAction(projectId, subject);
    }

    /**
     * Get the {@link edu.stanford.bmir.protege.web.shared.project.ProjectId}.
     *
     * @return The {@link edu.stanford.bmir.protege.web.shared.project.ProjectId}.  Not {@code null}.
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
