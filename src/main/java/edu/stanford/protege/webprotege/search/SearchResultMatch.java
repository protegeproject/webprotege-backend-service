package edu.stanford.protege.webprotege.search;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import edu.stanford.protege.webprotege.entity.EntityNode;
import edu.stanford.protege.webprotege.shortform.DictionaryLanguage;
import edu.stanford.protege.webprotege.shortform.ShortForm;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-17
 */

@AutoValue
public abstract class SearchResultMatch {

    public static final String ENTITY = "entity";

    public static final String LANGUAGE = "language";

    public static final String VALUE = "value";

    public static final String POSITIONS = "positions";

    @JsonCreator
    public static SearchResultMatch get(@JsonProperty(ENTITY) @Nonnull EntityNode entity,
                                        @JsonProperty(LANGUAGE) @Nonnull DictionaryLanguage matchedDictionaryLanguage,
                                        @JsonProperty(VALUE) @Nonnull String matchedString,
                                        @JsonProperty(POSITIONS) @Nonnull ImmutableList<SearchResultMatchPosition> searchResultMatchPositions) {
        return new AutoValue_SearchResultMatch(entity,
                                               matchedDictionaryLanguage,
                                               matchedString,
                                               searchResultMatchPositions);
    }

    @JsonProperty(ENTITY)
    @Nonnull
    public abstract EntityNode getEntity();

    @JsonProperty(LANGUAGE)
    @Nonnull
    public abstract DictionaryLanguage getLanguage();

    @JsonIgnore
    @Nonnull
    public ImmutableMap<DictionaryLanguage, String> getLanguageRendering() {
        return getEntity().getShortForms();
    }

    @JsonIgnore
    public ImmutableList<ShortForm> getShortForms() {
        return getEntity().getShortFormsList();
    }

    @JsonProperty(VALUE)
    @Nonnull
    public abstract String getValue();

    @JsonProperty(POSITIONS)
    @Nonnull
    public abstract ImmutableList<SearchResultMatchPosition> getPositions();

}
