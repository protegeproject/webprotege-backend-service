package edu.stanford.protege.webprotege.inject.project;



import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/03/15
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface ProjectDirectory {
}
