package edu.stanford.protege.webprotege.viz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Oct 2018
 */
@AutoValue

@JsonTypeName("GetEntityGraph")
public abstract class GetEntityGraphAction implements ProjectAction<GetEntityGraphResult> {

    @JsonCreator
    public static GetEntityGraphAction create(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                              @JsonProperty("entity") @Nonnull OWLEntity entity) {
        return new AutoValue_GetEntityGraphAction(projectId, entity);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @Nonnull
    public abstract OWLEntity getEntity();
}
