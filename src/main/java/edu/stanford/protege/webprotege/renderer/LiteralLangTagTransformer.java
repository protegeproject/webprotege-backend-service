package edu.stanford.protege.webprotege.renderer;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2018-12-10
 */
public interface LiteralLangTagTransformer {

    @Nonnull
    String transformLangTag(@Nonnull String langTag);
}
