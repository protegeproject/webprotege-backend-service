package edu.stanford.protege.webprotege.form;

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
 * 2020-10-21
 */
@AutoValue

@JsonTypeName("GetEntityDeprecationForms")
public abstract class GetEntityDeprecationFormsAction implements ProjectAction<GetEntityDeprecationFormsResult> {

    @JsonCreator
    public static GetEntityDeprecationFormsAction create(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                                         @JsonProperty("entity") @Nonnull OWLEntity entity) {
        return new AutoValue_GetEntityDeprecationFormsAction(projectId, entity);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    public abstract OWLEntity getEntity();
}
