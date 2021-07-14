package edu.stanford.protege.webprotege.frame;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.entity.OWLClassData;
import edu.stanford.protege.webprotege.entity.OWLObjectPropertyData;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/11/2012
 */
@AutoValue

@JsonTypeName("PropertyClassValue")
public abstract class PropertyClassValue extends ObjectPropertyValue {

    @JsonCreator
    @Nonnull
    public static PropertyClassValue get(@JsonProperty("property") @Nonnull OWLObjectPropertyData property,
                                         @JsonProperty("value") @Nonnull OWLClassData value,
                                         @JsonProperty("state") @Nonnull State state) {
        return new AutoValue_PropertyClassValue(property, value, state);
    }

    @Override
    public abstract OWLObjectPropertyData getProperty();

    @Override
    public abstract OWLClassData getValue();

    @Override
    public abstract State getState();

    @Override
    public boolean isValueMostSpecific() {
        return false;
    }

    @Override
    public <R, E extends Throwable> R accept(PropertyValueVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    @Override
    public boolean isAnnotation() {
        return false;
    }

    @Override
    public boolean isLogical() {
        return true;
    }

    @Override
    protected PropertyValue duplicateWithState(State state) {
        return PropertyClassValue.get(getProperty(), getValue(), state);
    }

    @Nonnull
    @Override
    public PlainPropertyClassValue toPlainPropertyValue() {
        return PlainPropertyClassValue.get(
                getProperty().getEntity(),
                getValue().getEntity(),
                getState()
        );
    }
}
