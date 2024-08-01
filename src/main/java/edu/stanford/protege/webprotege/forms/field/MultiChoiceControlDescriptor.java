package edu.stanford.protege.webprotege.forms.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.forms.PropertyNames;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-08
 */
@AutoValue

@JsonTypeName(MultiChoiceControlDescriptor.TYPE)
public abstract class MultiChoiceControlDescriptor implements FormControlDescriptor {

    @Nonnull
    public static final String TYPE = "MULTI_CHOICE";

    @JsonCreator
    public static MultiChoiceControlDescriptor get(@JsonProperty(PropertyNames.CHOICES_SOURCE) ChoiceListSourceDescriptor source,
                                                   @JsonProperty(PropertyNames.DEFAULT_CHOICE) @Nullable ImmutableList<ChoiceDescriptor> defaultChoices) {

        return new AutoValue_MultiChoiceControlDescriptor(source == null ? FixedChoiceListSourceDescriptor.get(
                ImmutableList.of()) : source, defaultChoices == null ? ImmutableList.of() : defaultChoices);
    }

    @JsonIgnore
    @Nonnull
    public static String getType() {
        return TYPE;
    }

    @Nonnull
    @JsonProperty(PropertyNames.CHOICES_SOURCE)
    public abstract ChoiceListSourceDescriptor getSource();

    @Nonnull
    @JsonProperty(PropertyNames.DEFAULT_CHOICE)
    public abstract ImmutableList<ChoiceDescriptor> getDefaultChoices();

    @Nonnull
    @Override
    public String getAssociatedType() {
        return TYPE;
    }

    @Override
    public <R> R accept(@Nonnull FormControlDescriptorVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
