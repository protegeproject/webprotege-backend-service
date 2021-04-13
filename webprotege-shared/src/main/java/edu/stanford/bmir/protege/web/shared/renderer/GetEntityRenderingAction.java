package edu.stanford.bmir.protege.web.shared.renderer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 25/02/2014
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("GetEntityRendering")
public abstract class GetEntityRenderingAction implements ProjectAction<GetEntityRenderingResult> {

    @JsonCreator
    public static GetEntityRenderingAction create(@JsonProperty("projectId") ProjectId projectId,
                                                  @JsonProperty("entity") OWLEntity entity) {
        return new AutoValue_GetEntityRenderingAction(projectId, entity);
    }

    @Nonnull
    public abstract ProjectId getProjectId();

    public abstract OWLEntity getEntity();
}
