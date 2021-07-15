package edu.stanford.protege.webprotege.dispatch;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-07-14
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ProjectActionHandlerComponent {

}
