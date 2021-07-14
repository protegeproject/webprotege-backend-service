package edu.stanford.protege.webprotege.lang;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableSet;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-26
 */
@AutoValue

public abstract class LangTagFilter {

    public static final String FILTERED_LANG_TAGS = "filteredLangTags";

    @JsonCreator
    @Nonnull
    public static LangTagFilter get(@JsonProperty(FILTERED_LANG_TAGS) @Nonnull ImmutableSet<LangTag> filteredLangTags) {
        return new AutoValue_LangTagFilter(filteredLangTags);
    }

    @Nonnull
    @JsonProperty(FILTERED_LANG_TAGS)
    public abstract ImmutableSet<LangTag> getFilteringTags();

    public boolean isAnyLangTagIncluded() {
        return getFilteringTags().isEmpty();
    }

    /**
     * Determines whether there is at least one filtered lang tag
     * @return true if there is at least one filtered lang tag, otherwise false
     */
    public boolean isFilterActive() {
        return getFilteringTags().size() > 0;
    }

    /**
     * Determines if the specified langtag is included by this filter.
     * @param langTag The lang tag.
     */
    public boolean isIncluded(@Nonnull LangTag langTag) {
        ImmutableSet<LangTag> filteringTags = getFilteringTags();
        return filteringTags.isEmpty() || filteringTags.contains(langTag);
    }

    public boolean isIncluded(@Nonnull String langTag) {
        if(getFilteringTags().isEmpty()) {
            return true;
        }
        LangTag tag = LangTag.get(langTag);
        return isIncluded(tag);
    }
}
