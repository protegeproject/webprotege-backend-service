package edu.stanford.bmir.protege.web.shared.search;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-17
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("GetSearchSettings")
public abstract class GetSearchSettingsResult implements Result {

    @JsonCreator
    @Nonnull
    public static GetSearchSettingsResult create(@JsonProperty("filters") @Nonnull ImmutableList<EntitySearchFilter> filters) {
        return new AutoValue_GetSearchSettingsResult(filters);
    }

    @Nonnull
    public abstract ImmutableList<EntitySearchFilter> getFilters();
}
