package edu.stanford.protege.webprotege.form;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.form.data.PrimitiveFormControlData;
import edu.stanford.protege.webprotege.form.data.PrimitiveFormControlDataDto;
import edu.stanford.protege.webprotege.form.field.ChoiceDescriptor;
import edu.stanford.protege.webprotege.form.field.ChoiceDescriptorDto;
import edu.stanford.protege.webprotege.form.field.FixedChoiceListSourceDescriptor;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

public class FixedListChoiceDescriptorDtoSupplier {

    @Nonnull
    private final PrimitiveFormControlDataDtoRenderer renderer;

    @Inject
    public FixedListChoiceDescriptorDtoSupplier(@Nonnull PrimitiveFormControlDataDtoRenderer renderer) {
        this.renderer = checkNotNull(renderer);
    }

    @Nonnull
    public ImmutableList<ChoiceDescriptorDto> getChoices(@Nonnull FixedChoiceListSourceDescriptor descriptor) {
        return descriptor.getChoices()
                         .stream()
                         .flatMap(this::toChoiceDescriptorDto)
                         .collect(toImmutableList());
    }

    @Nonnull
    private Stream<ChoiceDescriptorDto> toChoiceDescriptorDto(ChoiceDescriptor choiceDescriptor) {
        return toPrimitiveFormControlDataDto(choiceDescriptor.getValue())
                .map(dto -> ChoiceDescriptorDto.get(dto, choiceDescriptor.getLabel()));
    }

    @Nonnull
    private Stream<PrimitiveFormControlDataDto> toPrimitiveFormControlDataDto(@Nonnull PrimitiveFormControlData data) {
        var primitive = data.getPrimitive();
        return renderer.toFormControlDataDto(primitive);
    }
}