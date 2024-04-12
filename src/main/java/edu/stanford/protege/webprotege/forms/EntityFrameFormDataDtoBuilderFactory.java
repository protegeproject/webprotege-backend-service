package edu.stanford.protege.webprotege.forms;

import edu.stanford.protege.webprotege.common.LangTagFilter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.annotation.Nonnull;

/**
 * The EntityFrameFormDataDtoBuilderFactory interface provides a way to obtain an instance of the EntityFrameFormDataDtoBuilder.
 * It defines a single method for obtaining the EntityFrameFormDataDtoBuilder instance.
 */
public interface EntityFrameFormDataDtoBuilderFactory {

    EntityFrameFormDataDtoBuilder getFormDataDtoBuilder(ApplicationContext projectContext,
                                                        EntityFormDataRequestSpec formDataRequestSpec);
}
