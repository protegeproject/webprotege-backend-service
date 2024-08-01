package edu.stanford.protege.webprotege.forms.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.forms.PropertyNames;
import edu.stanford.protege.webprotege.forms.field.TextControlDescriptor;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.Optional;

@AutoValue

@JsonTypeName("TextControlDataDto")
public abstract class TextControlDataDto implements FormControlDataDto, Comparable<TextControlDataDto> {

    private static final Comparator<OWLLiteral> literalComparator = Comparator.nullsLast(Comparator.comparing(OWLLiteral::getLang)
                                                                                                   .thenComparing(
                                                                                                           OWLLiteral::getLiteral,
                                                                                                           String::compareToIgnoreCase)
                                                                                                   .thenComparing(
                                                                                                           OWLLiteral::getDatatype));

    @JsonCreator
    @Nonnull
    public static TextControlDataDto get(@JsonProperty(PropertyNames.CONTROL) @Nonnull TextControlDescriptor descriptor,
                                         @JsonProperty(PropertyNames.VALUE) @Nonnull OWLLiteral value,
                                         @JsonProperty(PropertyNames.DEPTH) int depth) {
        return new AutoValue_TextControlDataDto(depth, descriptor, value);
    }

    @JsonProperty(PropertyNames.CONTROL)
    @Nonnull
    public abstract TextControlDescriptor getDescriptor();

    @JsonProperty(PropertyNames.VALUE)
    @Nullable
    protected abstract OWLLiteral getValueInternal();

    @JsonIgnore
    @Nonnull
    public Optional<OWLLiteral> getValue() {
        return Optional.ofNullable(getValueInternal());
    }

    @Override
    public <R> R accept(FormControlDataDtoVisitorEx<R> visitor) {
        return visitor.visit(this);
    }

    @Nonnull
    @Override
    public TextControlData toFormControlData() {
        return TextControlData.get(getDescriptor(), getValueInternal());
    }

    @Override
    public int compareTo(@Nonnull TextControlDataDto o) {
        return literalComparator.compare(this.getValueInternal(), o.getValueInternal());
    }
}
