package edu.stanford.protege.webprotege.persistence;

import jakarta.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 6 Oct 2016
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface DbName {

}
