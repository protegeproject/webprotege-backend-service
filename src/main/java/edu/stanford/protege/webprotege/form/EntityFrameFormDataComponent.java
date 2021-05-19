package edu.stanford.protege.webprotege.form;

import dagger.Subcomponent;

import javax.annotation.Nonnull;

@Subcomponent(modules = {EntityFrameFormDataModule.class})
@FormDataBuilderSession
public interface EntityFrameFormDataComponent {

    @Nonnull
    EntityFrameFormDataDtoBuilder formDataBuilder();
}
