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
@JsonTypeName("SingleChoiceControlDescriptorDto")
public abstract class SingleChoiceControlDescriptorDto implements FormControlDescriptorDto {

    @JsonCreator
    @Nonnull
    public static SingleChoiceControlDescriptorDto get(@JsonProperty("widgetType") @Nonnull SingleChoiceControlType widgetType,
                                                       @JsonProperty("availableChoices") @Nonnull ImmutableList<ChoiceDescriptorDto> availableChoices,
                                                       @JsonProperty("choiceListSourceDescriptor") @Nonnull ChoiceListSourceDescriptor choiceListSourceDescriptor) {
        return new AutoValue_SingleChoiceControlDescriptorDto(availableChoices, choiceListSourceDescriptor, widgetType);
    }

    @Nonnull
    public abstract ImmutableList<ChoiceDescriptorDto> getAvailableChoices();


    @Nonnull
    protected abstract ChoiceListSourceDescriptor getChoiceListSourceDescriptor();

    @Nonnull
    public abstract SingleChoiceControlType getWidgetType();

    @Override
    public <R> R accept(FormControlDescriptorDtoVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public SingleChoiceControlDescriptor toFormControlDescriptor() {
        return SingleChoiceControlDescriptor.get(getWidgetType(),
                                                 getChoiceListSourceDescriptor());
    }
}
