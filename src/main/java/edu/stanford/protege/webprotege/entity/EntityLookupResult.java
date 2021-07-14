package edu.stanford.protege.webprotege.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.search.SearchResultMatch;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/05/2012
 */
@AutoValue

public abstract class EntityLookupResult {

    @JsonCreator
    public static EntityLookupResult get(@JsonProperty("matchResult") @Nonnull SearchResultMatch matchResult,
                                         @JsonProperty("directLink") @Nonnull String directLink) {
        return new AutoValue_EntityLookupResult(matchResult, directLink);
    }

    @Nonnull
    public abstract SearchResultMatch getMatchResult();

    @Nonnull
    public abstract String getDirectLink();
}
