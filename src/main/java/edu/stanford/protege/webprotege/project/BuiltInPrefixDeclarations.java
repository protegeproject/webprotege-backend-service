package edu.stanford.protege.webprotege.project;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-04-30
 */
@AutoValue
public abstract class BuiltInPrefixDeclarations {

    @Nonnull
    public static BuiltInPrefixDeclarations get(@Nonnull ImmutableList<PrefixDeclaration> prefixDeclarations) {
        return new AutoValue_BuiltInPrefixDeclarations(prefixDeclarations);
    }

    /**
     * Gets a list of prefix declarations that are considered to be built in prefix
     * declarations.
     */
    @Nonnull
    public abstract ImmutableList<PrefixDeclaration> getPrefixDeclarations();
}
