package edu.stanford.protege.webprotege.frame;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.entity.OWLAnnotationPropertyData;
import edu.stanford.protege.webprotege.entity.OWLEntityData;
import edu.stanford.protege.webprotege.entity.OWLPrimitiveData;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/11/2012
 */
@AutoValue

@JsonTypeName("PropertyAnnotationValue")
public abstract class PropertyAnnotationValue extends PropertyValue {

    @JsonCreator
    @Nonnull
    public static PropertyAnnotationValue get(@JsonProperty("property") @Nonnull OWLAnnotationPropertyData property,
                                              @JsonProperty("value") @Nonnull OWLPrimitiveData value,
                                              @JsonProperty("state") @Nonnull State state) {
        return new AutoValue_PropertyAnnotationValue(property, value, state);
    }


    @Override
    public abstract OWLAnnotationPropertyData getProperty();

    @Override
    public abstract OWLPrimitiveData getValue();

    @Override
    public abstract State getState();

    @JsonIgnore
    @Nonnull
    public Optional<OWLEntityData> getValueAsEntity() {
        OWLPrimitiveData primitiveData = getValue();
        if(primitiveData instanceof OWLEntityData) {
            return Optional.of((OWLEntityData) primitiveData);
        }
        else {
            return Optional.empty();
        }
    }

    @Override
    public boolean isValueMostSpecific() {
        return true;
    }


    @Override
    public boolean isAnnotation() {
        return true;
    }

    @Override
    public boolean isLogical() {
        return false;
    }

    @Override
    protected PropertyValue duplicateWithState(State state) {
        return PropertyAnnotationValue.get(getProperty(), getValue(), state);
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Nonnull
    @Override
    public PlainPropertyAnnotationValue toPlainPropertyValue() {
        return PlainPropertyAnnotationValue.get(
                getProperty().getEntity(),
                getValue().asAnnotationValue().get(),
                getState()
        );
    }

    @Override
    public <R, E extends Throwable> R accept(PropertyValueVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }
}
