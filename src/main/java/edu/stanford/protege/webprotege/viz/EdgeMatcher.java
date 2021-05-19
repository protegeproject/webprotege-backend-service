package edu.stanford.protege.webprotege.viz;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-05
 */
public interface EdgeMatcher {

    boolean matches(@Nonnull Edge edge);
}
