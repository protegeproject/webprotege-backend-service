package edu.stanford.protege.webprotege.renderer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 25/02/2014
 */
@AutoValue

@JsonTypeName("GetEntityRendering")
public abstract class GetEntityRenderingAction implements ProjectAction<GetEntityRenderingResult> {

    public static final String CHANNEL = "webprotege.entities.GetEntityRendering";

    @JsonCreator
    public static GetEntityRenderingAction create(@JsonProperty("projectId") ProjectId projectId,
                                                  @JsonProperty("entity") OWLEntity entity) {
        return new AutoValue_GetEntityRenderingAction(projectId, entity);
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    @Nonnull
    public abstract ProjectId projectId();

    public abstract OWLEntity getEntity();
}
