package edu.stanford.protege.webprotege.frame;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.entity.OWLDataPropertyData;
import edu.stanford.protege.webprotege.entity.OWLLiteralData;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/11/2012
 */
@AutoValue

@JsonTypeName("PropertyLiteralValue")
public abstract class PropertyLiteralValue extends DataPropertyValue {

    @JsonCreator
    @Nonnull
    public static PropertyLiteralValue get(@JsonProperty("property") @Nonnull OWLDataPropertyData property,
                                           @JsonProperty("value") @Nonnull OWLLiteralData value,
                                           @JsonProperty("state") @Nonnull State state) {
        return new AutoValue_PropertyLiteralValue(property, value, state);
    }

    @Override
    public abstract OWLDataPropertyData getProperty();

    @Override
    public abstract OWLLiteralData getValue();

    @Override
    public abstract State getState();


    @Override
    public boolean isValueMostSpecific() {
        return true;
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
    public <R, E extends Throwable> R accept(PropertyValueVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    @Override
    protected PropertyValue duplicateWithState(State state) {
        return PropertyLiteralValue.get(getProperty(), getValue(), state);
    }

    @Nonnull
    @Override
    public PlainPropertyLiteralValue toPlainPropertyValue() {
        return PlainPropertyLiteralValue.get(getProperty().getEntity(), getValue().getLiteral(), getState());
    }
}
