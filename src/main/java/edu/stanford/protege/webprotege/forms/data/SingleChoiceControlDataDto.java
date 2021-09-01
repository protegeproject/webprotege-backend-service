package edu.stanford.protege.webprotege.forms.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.forms.field.SingleChoiceControlDescriptor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

@AutoValue

@JsonTypeName("SingleChoiceControlDataDto")
public abstract class SingleChoiceControlDataDto implements FormControlDataDto {

    @JsonCreator
    @Nonnull
    public static SingleChoiceControlDataDto get(@JsonProperty("descriptor") @Nonnull SingleChoiceControlDescriptor descriptor,
                                                 @JsonProperty("choice") @Nullable PrimitiveFormControlDataDto choice,
                                                 @JsonProperty("depth") int depth) {
        return new AutoValue_SingleChoiceControlDataDto(depth, descriptor, choice);
    }

    @Nonnull
    public abstract SingleChoiceControlDescriptor getDescriptor();

    @JsonProperty("choice")
    @Nullable
    protected abstract PrimitiveFormControlDataDto getChoiceInternal();

    @Nonnull
    public Optional<PrimitiveFormControlDataDto> getChoice() {
        return Optional.ofNullable(getChoiceInternal());
    }

    @Override
    public <R> R accept(FormControlDataDtoVisitorEx<R> visitor) {
        return visitor.visit(this);
    }

    @Nonnull
    @Override
    public SingleChoiceControlData toFormControlData() {
        return SingleChoiceControlData.get(getDescriptor(),
                getChoice().map(PrimitiveFormControlDataDto::toPrimitiveFormControlData).orElse(null));
    }
}
