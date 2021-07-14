package edu.stanford.protege.webprotege.frame;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.entity.OWLDataPropertyData;
import edu.stanford.protege.webprotege.entity.OWLDatatypeData;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/11/2012
 */
@AutoValue

@JsonTypeName("PropertyDatatypeValue")
public abstract class PropertyDatatypeValue extends DataPropertyValue {


    @JsonCreator
    @Nonnull
    public static PropertyDatatypeValue get(@JsonProperty("property") @Nonnull OWLDataPropertyData property,
                                            @JsonProperty("value") @Nonnull OWLDatatypeData datatype,
                                            @JsonProperty("state") @Nonnull State state) {
        return new AutoValue_PropertyDatatypeValue(property, datatype, state);
    }

    @Override
    public abstract OWLDataPropertyData getProperty();

    @Override
    public abstract OWLDatatypeData getValue();

    @Override
    public abstract State getState();

    @Override
    public boolean isValueMostSpecific() {
        return false;
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
        return PropertyDatatypeValue.get(getProperty(), getValue(), state);
    }

    @Nonnull
    @Override
    public PlainPropertyDatatypeValue toPlainPropertyValue() {
        return PlainPropertyDatatypeValue.get(getProperty().getEntity(),
                                              getValue().getEntity(),
                                              getState());
    }
}
