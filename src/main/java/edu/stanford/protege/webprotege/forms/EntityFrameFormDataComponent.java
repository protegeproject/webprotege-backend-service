package edu.stanford.protege.webprotege.forms;

import javax.annotation.Nonnull;

//@Subcomponent(modules = {EntityFrameFormDataModule.class})
//@FormDataBuilderSession
public interface EntityFrameFormDataComponent {

    @Nonnull
    EntityFrameFormDataDtoBuilder formDataBuilder();
}
