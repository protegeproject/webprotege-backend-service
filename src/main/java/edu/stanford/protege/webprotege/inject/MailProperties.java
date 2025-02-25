package edu.stanford.protege.webprotege.inject;

import jakarta.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface MailProperties {
}
