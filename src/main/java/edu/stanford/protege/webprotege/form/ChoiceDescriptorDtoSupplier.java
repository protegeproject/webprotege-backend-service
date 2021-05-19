package edu.stanford.protege.webprotege.form;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.form.field.ChoiceDescriptorDto;
import edu.stanford.protege.webprotege.form.field.ChoiceListSourceDescriptor;
import edu.stanford.protege.webprotege.form.field.DynamicChoiceListSourceDescriptor;
import edu.stanford.protege.webprotege.form.field.FixedChoiceListSourceDescriptor;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

public class ChoiceDescriptorDtoSupplier {

    @Nonnull
    private final FixedListChoiceDescriptorDtoSupplier fixedListSupplier;

    @Nonnull
    private final DynamicListChoiceDescriptorDtoSupplier dynamicListSupplier;

    @Inject
    public ChoiceDescriptorDtoSupplier(@Nonnull FixedListChoiceDescriptorDtoSupplier fixedListSupplier, @Nonnull DynamicListChoiceDescriptorDtoSupplier dynamicListSupplier, @Nonnull FormDataBuilderSessionRenderer sessionRenderer) {
        this.fixedListSupplier = checkNotNull(fixedListSupplier);
        this.dynamicListSupplier = checkNotNull(dynamicListSupplier);
    }

    @Nonnull
    public ImmutableList<ChoiceDescriptorDto> getChoices(@Nonnull ChoiceListSourceDescriptor descriptor) {
        checkNotNull(descriptor);
        if(descriptor instanceof FixedChoiceListSourceDescriptor) {
            return fixedListSupplier.getChoices((FixedChoiceListSourceDescriptor) descriptor);
        }
        else {
            return dynamicListSupplier.getChoices((DynamicChoiceListSourceDescriptor) descriptor);
        }
    }
}
