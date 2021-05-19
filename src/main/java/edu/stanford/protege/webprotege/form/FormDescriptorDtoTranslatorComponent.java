package edu.stanford.protege.webprotege.form;

import dagger.Subcomponent;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-29
 */

@Subcomponent(modules = {EntityFrameFormDataModule.class})
@FormDataBuilderSession
public interface FormDescriptorDtoTranslatorComponent {

    FormDescriptorDtoTranslator getFormDescriptorDtoTranslator();
}
