package edu.stanford.bmir.protege.web.shared.search;

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
 * 21 Apr 2017
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("PerformEntitySearch")
public abstract class PerformEntitySearchResult implements Result {

    @JsonCreator
    @Nonnull
    public static PerformEntitySearchResult create(@JsonProperty("searchString") String searchString,
                                                   @JsonProperty("results") Page<EntitySearchResult> results) {
        return new AutoValue_PerformEntitySearchResult(searchString, results);
    }

    @Nonnull
    public abstract String getSearchString();

    @Nonnull
    public abstract Page<EntitySearchResult> getResults();
}
