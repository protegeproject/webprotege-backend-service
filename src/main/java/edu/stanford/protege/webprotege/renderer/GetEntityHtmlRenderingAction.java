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
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-03-27
 */
@AutoValue

@JsonTypeName("GetEntityHtmlRendering")
public abstract class GetEntityHtmlRenderingAction implements ProjectAction<GetEntityHtmlRenderingResult> {


    public static final String CHANNEL = "webprotege.html.GetEntityHtmlRendering";

    @JsonCreator
    public static GetEntityHtmlRenderingAction create(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                                      @JsonProperty("entity") @Nonnull OWLEntity entity) {
        return new AutoValue_GetEntityHtmlRenderingAction(projectId, entity);
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    @Nonnull
    @Override
    public abstract ProjectId projectId();

    @Nonnull
    public abstract OWLEntity getEntity();
}
