package edu.stanford.protege.webprotege.renderer;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2018-12-02
 */
public interface LiteralLexicalFormTransformer {

    @Nonnull
    String transformLexicalForm(@Nonnull String lexicalForm);
}
