package edu.stanford.protege.webprotege.inject;

import javax.inject.Scope;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Oct 2016
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ApplicationSingleton {

}
