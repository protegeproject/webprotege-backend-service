package edu.stanford.protege.webprotege.shortform;

import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-03
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface LuceneIndexesDirectory {
}
