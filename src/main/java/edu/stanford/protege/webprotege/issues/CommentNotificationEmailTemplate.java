package edu.stanford.protege.webprotege.issues;

import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10 Mar 2017
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface CommentNotificationEmailTemplate {

}
