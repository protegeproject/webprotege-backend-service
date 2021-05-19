package edu.stanford.protege.webprotege.access;

import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-03-27
 */
@AutoValue
public abstract class AccessManagerCacheKey {

    @Nonnull
    public static AccessManagerCacheKey get(@Nonnull Subject subject,
                                            @Nonnull Resource resource,
                                            @Nonnull ActionId actionId) {
        return new AutoValue_AccessManagerCacheKey(subject, resource, actionId);
    }

    @Nonnull
    public abstract Subject getSubject();

    @Nonnull
    public abstract Resource getResource();

    @Nonnull
    public abstract ActionId getActionId();
}
