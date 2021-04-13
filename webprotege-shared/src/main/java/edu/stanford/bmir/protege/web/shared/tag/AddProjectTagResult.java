package edu.stanford.bmir.protege.web.shared.tag;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Mar 2018
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("AddProjectTag")
public abstract class AddProjectTagResult implements Result {

    @JsonCreator
    public static AddProjectTagResult create(@JsonProperty("addedTag") @Nullable Tag addedTag) {
        return new AutoValue_AddProjectTagResult(addedTag);
    }

    /**
     * Gets the tag if it was added.
     * @return The tag.  If the tag was not added (likely due to a duplicate label, for example)
     * then the return value will be empty.
     */
    @Nonnull
    public Optional<Tag> getAddedTag() {
        return Optional.ofNullable(getTagInternal());
    }

    @JsonIgnore
    @Nullable
    protected abstract Tag getTagInternal();
}
