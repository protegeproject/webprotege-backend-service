package edu.stanford.bmir.protege.web.shared.renderer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-03-27
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("GetEntityHtmlRendering")
public abstract class GetEntityHtmlRenderingAction implements ProjectAction<GetEntityHtmlRenderingResult> {


    @JsonCreator
    public static GetEntityHtmlRenderingAction create(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                                      @JsonProperty("entity") @Nonnull OWLEntity entity) {
        return new AutoValue_GetEntityHtmlRenderingAction(projectId, entity);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @Nonnull
    public abstract OWLEntity getEntity();
}
