package edu.stanford.protege.webprotege.forms.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.forms.PropertyNames;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-11
 */
@JsonTypeName(FixedChoiceListSourceDescriptor.TYPE)
@AutoValue
public abstract class FixedChoiceListSourceDescriptor implements ChoiceListSourceDescriptor {

    public static final String TYPE = "Fixed";

    @JsonCreator
    public static FixedChoiceListSourceDescriptor get(@JsonProperty(PropertyNames.CHOICES) @Nullable List<ChoiceDescriptor> choices) {
        return new AutoValue_FixedChoiceListSourceDescriptor(choices == null ? ImmutableList.of() : ImmutableList.copyOf(choices));
    }

    @JsonProperty(PropertyNames.CHOICES)
    @Nonnull
    public abstract List<ChoiceDescriptor> getChoices();
}
