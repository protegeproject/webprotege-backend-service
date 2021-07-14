package edu.stanford.protege.webprotege.search;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.pagination.Page;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Apr 2017
 */
@AutoValue

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
