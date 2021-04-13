package edu.stanford.bmir.protege.web.shared.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.search.SearchResultMatch;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/05/2012
 */
@AutoValue
@GwtCompatible(serializable = true)
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
