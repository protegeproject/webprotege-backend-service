package edu.stanford.protege.webprotege.match;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Jun 2018
 */
public class StringHasUntrimmedSpaceMatcher implements Matcher<String> {

    @Override
    public boolean matches(@Nonnull String value) {
        return value.endsWith(" ") || value.startsWith(" ");
    }
}
