package edu.stanford.protege.webprotege.inject.project;



import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 02/06/15
 */
@Qualifier

@Retention(RetentionPolicy.RUNTIME)
public @interface ChangeHistoryFile {
}
