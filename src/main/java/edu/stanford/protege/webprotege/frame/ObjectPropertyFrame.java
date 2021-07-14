package edu.stanford.protege.webprotege.frame;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.entity.OWLClassData;
import edu.stanford.protege.webprotege.entity.OWLObjectPropertyData;

import javax.annotation.Nonnull;
import java.io.Serializable;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/12/2012
 */
@AutoValue

@JsonTypeName("ObjectPropertyFrame")
public abstract class ObjectPropertyFrame implements EntityFrame<OWLObjectPropertyData>, HasAnnotationPropertyValues, Serializable {

    @JsonCreator
    @Nonnull
    public static ObjectPropertyFrame get(@JsonProperty("subject") @Nonnull OWLObjectPropertyData subject,
                                          @JsonProperty("propertyValues") @Nonnull ImmutableSet<PropertyAnnotationValue> annotationValues,
                                          @JsonProperty("domains") @Nonnull ImmutableSet<OWLClassData> domains,
                                          @JsonProperty("ranges") @Nonnull ImmutableSet<OWLClassData> ranges,
                                          @JsonProperty("inverseProperties") @Nonnull ImmutableSet<OWLObjectPropertyData> inverseProperties,
                                          @JsonProperty("characteristics") @Nonnull ImmutableSet<ObjectPropertyCharacteristic> characteristics) {

        return new AutoValue_ObjectPropertyFrame(subject,
                                                 annotationValues,
                                                 domains,
                                                 ranges,
                                                 characteristics,
                                                 inverseProperties);
    }

    @Nonnull
    public static ObjectPropertyFrame empty(@Nonnull OWLObjectPropertyData subject) {
        return get(subject,
                   ImmutableSet.of(),
                   ImmutableSet.of(),
                   ImmutableSet.of(),
                   ImmutableSet.of(),
                   ImmutableSet.of());
    }

    @Nonnull
    public abstract OWLObjectPropertyData getSubject();

    @JsonProperty("propertyValues")
    @Nonnull
    @Override
    public abstract ImmutableSet<PropertyAnnotationValue> getAnnotationPropertyValues();

    @Nonnull
    public abstract ImmutableSet<OWLClassData> getDomains();

    @Nonnull
    public abstract ImmutableSet<OWLClassData> getRanges();

    @Nonnull
    public abstract ImmutableSet<ObjectPropertyCharacteristic> getCharacteristics();

    @Nonnull
    public abstract ImmutableSet<OWLObjectPropertyData> getInverseProperties();

    @Nonnull
    @Override
    public PlainObjectPropertyFrame toPlainFrame() {
        return PlainObjectPropertyFrame.get(
                getSubject().getEntity(),
                getAnnotationPropertyValues().stream().map(PropertyAnnotationValue::toPlainPropertyValue).collect(toImmutableSet()),
                getCharacteristics(),
                getDomains().stream().map(OWLClassData::getEntity).collect(toImmutableSet()),
                getRanges().stream().map(OWLClassData::getEntity).collect(toImmutableSet()),
                getInverseProperties().stream().map(OWLObjectPropertyData::getEntity).collect(toImmutableSet())
        );
    }
}
