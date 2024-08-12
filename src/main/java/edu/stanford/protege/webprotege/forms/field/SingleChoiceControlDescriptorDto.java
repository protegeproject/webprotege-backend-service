package edu.stanford.protege.webprotege.forms.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.forms.PropertyNames;

import javax.annotation.Nonnull;


@AutoValue
@JsonTypeName("SingleChoiceControlDescriptorDto")
public abstract class SingleChoiceControlDescriptorDto implements FormControlDescriptorDto {

    @JsonCreator
    @Nonnull
    public static SingleChoiceControlDescriptorDto get(@JsonProperty(PropertyNames.WIDGET_TYPE) @Nonnull SingleChoiceControlType widgetType,
                                                       @JsonProperty(PropertyNames.CHOICES) @Nonnull ImmutableList<ChoiceDescriptorDto> availableChoices,
                                                       @JsonProperty(PropertyNames.CHOICES_SOURCE) @Nonnull ChoiceListSourceDescriptor choiceListSourceDescriptor) {
        return new AutoValue_SingleChoiceControlDescriptorDto(availableChoices, choiceListSourceDescriptor, widgetType);
    }

    @Nonnull
    @JsonProperty(PropertyNames.CHOICES)
    public abstract ImmutableList<ChoiceDescriptorDto> getAvailableChoices();


    @JsonProperty(PropertyNames.CHOICES_SOURCE)
    @Nonnull
    public abstract ChoiceListSourceDescriptor getChoiceListSourceDescriptor();

    @Nonnull
    @JsonProperty(PropertyNames.WIDGET_TYPE)
    public abstract SingleChoiceControlType getWidgetType();

    @Override
    public <R> R accept(FormControlDescriptorDtoVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public SingleChoiceControlDescriptor toFormControlDescriptor() {
        return SingleChoiceControlDescriptor.get(getWidgetType(), getChoiceListSourceDescriptor());
    }
}
