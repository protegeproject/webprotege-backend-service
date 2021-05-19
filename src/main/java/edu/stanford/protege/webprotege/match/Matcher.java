package edu.stanford.protege.webprotege.match;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Jun 2018
 */
public interface Matcher<T>  {

    boolean matches(@Nonnull T value);

    static <T> Matcher<T> matchesAny() {
        return o -> true;
    }
}
