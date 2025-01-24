package edu.stanford.protege.webprotege.index;

import jakarta.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-09-10
 */
@Retention(RetentionPolicy.RUNTIME)
@Qualifier
public @interface IndexUpdatingService {

}
