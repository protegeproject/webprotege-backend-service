package edu.stanford.protege.webprotege.forms.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.forms.field.NumberControlDescriptor;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

@AutoValue

@JsonTypeName("NumberControlDataDto")
public abstract class NumberControlDataDto implements FormControlDataDto, Comparable<NumberControlDataDto> {

    private Double numericValue;

    @JsonCreator
    @Nonnull
    public static NumberControlDataDto get(@JsonProperty("descriptor") @Nonnull NumberControlDescriptor descriptor,
                                           @JsonProperty("value") @Nonnull OWLLiteral value,
                                           @JsonProperty("depth") int depth) {
        return new AutoValue_NumberControlDataDto(depth, descriptor, value);
    }

    @Nonnull
    public abstract NumberControlDescriptor getDescriptor();

    @JsonProperty("value")
    @Nullable
    protected abstract OWLLiteral getValueInternal();

    @Nonnull
    @JsonIgnore
    public Optional<OWLLiteral> getValue() {
        return Optional.ofNullable(getValueInternal());
    }

    @Override
    public <R> R accept(FormControlDataDtoVisitorEx<R> visitor) {
        return visitor.visit(this);
    }

    @Nonnull
    @Override
    public NumberControlData toFormControlData() {
        return NumberControlData.get(getDescriptor(),
                                     getValueInternal());
    }

    private Double getNumericValue() {
        if (numericValue == null) {
            OWLLiteral literal = getValueInternal();
            if (literal == null) {
                numericValue = Double.MIN_VALUE;
            } else {
                try {
                    numericValue = Double.parseDouble(literal.getLiteral().trim());
                } catch (NumberFormatException e) {
                    numericValue = Double.MIN_VALUE;
                }
            }
        }
        return numericValue;
    }

    @Override
    public int compareTo(@Nonnull NumberControlDataDto o) {
        return getNumericValue().compareTo(o.getNumericValue());
    }
}
