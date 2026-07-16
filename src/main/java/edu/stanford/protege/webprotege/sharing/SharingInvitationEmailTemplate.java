package edu.stanford.protege.webprotege.sharing;

import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Qualifier for the template file used to render sharing-invitation emails.
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface SharingInvitationEmailTemplate {

}
