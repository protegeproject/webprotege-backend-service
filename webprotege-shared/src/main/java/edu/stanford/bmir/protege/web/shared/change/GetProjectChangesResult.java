package edu.stanford.bmir.protege.web.shared.change;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.pagination.Page;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24/02/15
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("GetProjectChanges")
public abstract class GetProjectChangesResult implements Result, HasProjectChanges {

    @Nonnull
    @JsonCreator
    public static GetProjectChangesResult create(@JsonProperty("projectChanges") Page<ProjectChange> changes) {
        return new AutoValue_GetProjectChangesResult(changes);
    }

    @Nonnull
    public abstract Page<ProjectChange> getProjectChanges();
}
