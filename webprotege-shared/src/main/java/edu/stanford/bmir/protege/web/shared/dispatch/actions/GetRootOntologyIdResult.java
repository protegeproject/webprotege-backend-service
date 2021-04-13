package edu.stanford.bmir.protege.web.shared.dispatch.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.project.HasProjectId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("GetRootOntologyId")
public abstract class GetRootOntologyIdResult implements Result, HasProjectId {

    @JsonCreator
    public static GetRootOntologyIdResult create(@JsonProperty("projectId") ProjectId projectId,
                                                 @JsonProperty("ontologyId") OWLOntologyID owlOntologyID) {
        return new AutoValue_GetRootOntologyIdResult(projectId, owlOntologyID);
    }

    /**
     * Get the {@link edu.stanford.bmir.protege.web.shared.project.ProjectId}.
     *
     * @return The {@link edu.stanford.bmir.protege.web.shared.project.ProjectId}.  Not {@code null}.
     */
    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    public abstract OWLOntologyID getOntologyId();
}
