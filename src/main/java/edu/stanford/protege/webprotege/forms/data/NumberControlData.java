package edu.stanford.protege.webprotege.forms.data;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.forms.PropertyNames;
import edu.stanford.protege.webprotege.forms.field.NumberControlDescriptor;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-08
 */
@AutoValue

@JsonTypeName("NumberControlData")
public abstract class NumberControlData implements FormControlData {

    @JsonCreator
    public static NumberControlData get(@JsonProperty(PropertyNames.CONTROL) @JsonAlias(PropertyNames.DESCRIPTOR) @Nonnull NumberControlDescriptor descriptor,
                                        @JsonProperty(PropertyNames.VALUE) @Nullable OWLLiteral value) {
        return new AutoValue_NumberControlData(descriptor, value);
    }

    @Override
    public <R> R accept(@Nonnull FormControlDataVisitorEx<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public void accept(@Nonnull FormControlDataVisitor visitor) {
        visitor.visit(this);
    }

    @JsonProperty(PropertyNames.CONTROL)
    @Nonnull
    public abstract NumberControlDescriptor getDescriptor();

    @JsonProperty(PropertyNames.VALUE)
    @Nullable
    protected abstract OWLLiteral getValueInternal();

    @Nonnull
    @JsonIgnore
    public Optional<OWLLiteral> getValue() {
        return Optional.ofNullable(getValueInternal());
    }
}
