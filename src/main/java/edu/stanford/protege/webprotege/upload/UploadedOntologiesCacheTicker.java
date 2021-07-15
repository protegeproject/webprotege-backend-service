package edu.stanford.protege.webprotege.upload;

import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-07-14
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface UploadedOntologiesCacheTicker {

}
