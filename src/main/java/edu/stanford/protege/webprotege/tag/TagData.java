package edu.stanford.protege.webprotege.tag;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.color.Color;
import edu.stanford.protege.webprotege.criteria.RootCriteria;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23 Mar 2018
 */
@AutoValue

public abstract class TagData {

    public static TagData get(@Nonnull String label,
                              @Nonnull String description,
                              @Nonnull Color color,
                              @Nonnull Color backgroundColor,
                              @Nonnull ImmutableList<RootCriteria> criteria,
                              int usageCount) {
        return get(null, label, description, color, backgroundColor, criteria, usageCount);
    }

    @JsonCreator
    public static TagData get(@JsonProperty("tagId") @Nullable TagId tagId,
                              @JsonProperty("label") @Nonnull String label,
                              @JsonProperty("description") @Nonnull String description,
                              @JsonProperty("color") @Nonnull Color color,
                              @JsonProperty("backgroundColor") @Nonnull Color backgroundColor,
                              @JsonProperty("criteria") @Nonnull ImmutableList<RootCriteria> criteria,
                              @JsonProperty("usageCount") int usageCount) {
        return new AutoValue_TagData(tagId, label, description, color, backgroundColor, criteria, usageCount);
    }

    @Nonnull
    public Optional<TagId> getTagId() {
        return Optional.ofNullable(_getTagId());
    }

    @Nullable
    protected abstract TagId _getTagId();

    @Nonnull
    public abstract String getLabel();

    @Nonnull
    public abstract String getDescription();

    @Nonnull
    public abstract Color getColor();

    @Nonnull
    public abstract Color getBackgroundColor();

    @Nonnull
    public abstract ImmutableList<RootCriteria> getCriteria();

    public abstract int getUsageCount();

    public TagData withCriteria(ImmutableList<RootCriteria> criteria) {
        return get(_getTagId(),
                   getLabel(),
                   getDescription(),
                   getColor(),
                   getBackgroundColor(),
                   criteria,
                   getUsageCount());
    }


}
