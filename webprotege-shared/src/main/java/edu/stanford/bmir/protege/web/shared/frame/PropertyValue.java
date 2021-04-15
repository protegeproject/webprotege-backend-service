package edu.stanford.bmir.protege.web.shared.frame;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPropertyData;

import javax.annotation.Nonnull;
import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/11/2012
 */
@JsonSubTypes({
        @Type(PropertyClassValue.class),
        @Type(PropertyIndividualValue.class),
        @Type(PropertyDatatypeValue.class),
        @Type(PropertyLiteralValue.class),
        @Type(PropertyAnnotationValue.class)
})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public abstract class PropertyValue implements Comparable<PropertyValue>, Serializable {


    public abstract OWLPropertyData getProperty();

    public abstract OWLPrimitiveData getValue();

    public abstract State getState();

    @JsonIgnore
    public abstract boolean isValueMostSpecific();

    @JsonIgnore
    public abstract boolean isAnnotation();

    @JsonIgnore
    public abstract boolean isLogical();

    public abstract <R, E extends Throwable>  R accept(PropertyValueVisitor<R, E> visitor) throws E;

    public PropertyValue setState(State state) {
        if(getState() == state) {
            return this;
        }
        else {
            return duplicateWithState(state);
        }
    }

    protected abstract PropertyValue duplicateWithState(State state);

    @Override
    public int compareTo(PropertyValue o) {
        int propertyDiff = getProperty().compareTo(o.getProperty());
        if(propertyDiff != 0) {
            return propertyDiff;
        }
        int valueDiff = getValue().compareTo(o.getValue());
        if(valueDiff != 0) {
            return valueDiff;
        }
        return 0;
    }

    @Nonnull
    public abstract PlainPropertyValue toPlainPropertyValue();
}
