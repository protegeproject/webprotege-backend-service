package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;

@GwtCompatible(serializable = true)
@AutoValue
@JsonTypeName("MultiChoiceControlDescriptorDto")
public abstract class MultiChoiceControlDescriptorDto implements FormControlDescriptorDto {

    @JsonCreator
    @Nonnull
    public static MultiChoiceControlDescriptorDto get(@JsonProperty("choiceListSourceDescriptor") @Nonnull ChoiceListSourceDescriptor choiceListSourceDescriptor,
                                                      @JsonProperty("availableChoices") @Nonnull ImmutableList<ChoiceDescriptorDto> choices) {
        return new AutoValue_MultiChoiceControlDescriptorDto(choiceListSourceDescriptor, choices);
    }

    @Nonnull
    public abstract ChoiceListSourceDescriptor getChoiceListSourceDescriptor();

    @Nonnull
    public abstract ImmutableList<ChoiceDescriptorDto> getAvailableChoices();

    @Override
    public <R> R accept(FormControlDescriptorDtoVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public MultiChoiceControlDescriptor toFormControlDescriptor() {
        return MultiChoiceControlDescriptor.get(
            getChoiceListSourceDescriptor(),
            ImmutableList.of()
        );
    }
}
