package edu.stanford.protege.webprotege.ontology;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.project.HasProjectId;
import edu.stanford.protege.webprotege.project.ProjectId;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 */
@AutoValue

@JsonTypeName("GetRootOntologyId")
public abstract class GetRootOntologyIdResult implements Result, HasProjectId {

    @JsonCreator
    public static GetRootOntologyIdResult create(@JsonProperty("projectId") ProjectId projectId,
                                                 @JsonProperty("ontologyId") OWLOntologyID owlOntologyID) {
        return new AutoValue_GetRootOntologyIdResult(projectId, owlOntologyID);
    }

    /**
     * Get the {@link ProjectId}.
     *
     * @return The {@link ProjectId}.  Not {@code null}.
     */
    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    public abstract OWLOntologyID getOntologyId();
}
