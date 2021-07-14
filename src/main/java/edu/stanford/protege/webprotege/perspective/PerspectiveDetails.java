package edu.stanford.protege.webprotege.perspective;

import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.lang.LanguageMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-02
 */
@AutoValue
public abstract class PerspectiveDetails {


    @Nonnull
    public static PerspectiveDetails get(@Nonnull PerspectiveId perspectiveId,
                                         @Nonnull LanguageMap label,
                                         boolean favorite,
                                         @Nullable Node layout) {
        return new AutoValue_PerspectiveDetails(perspectiveId, label, favorite, layout);
    }

    @Nonnull
    public abstract PerspectiveId getPerspectiveId();

    @Nonnull
    public abstract LanguageMap getLabel();

    public abstract boolean isFavorite();

    @Nullable
    protected abstract Node getLayoutInternal();

    @Nonnull
    public Optional<Node> getLayout() {
        return Optional.ofNullable(getLayoutInternal());
    }

    public PerspectiveDescriptor toPerspectiveDescriptor() {
        return PerspectiveDescriptor.get(getPerspectiveId(),
                                         getLabel(),
                                         isFavorite());
    }
}
