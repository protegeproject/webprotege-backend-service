package edu.stanford.protege.webprotege.frame;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.entity.OWLClassData;
import edu.stanford.protege.webprotege.entity.OWLDataPropertyData;
import edu.stanford.protege.webprotege.entity.OWLDatatypeData;

import javax.annotation.Nonnull;
import java.io.Serializable;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
@AutoValue

@JsonTypeName("DataPropertyFrame")
public abstract class DataPropertyFrame implements EntityFrame<OWLDataPropertyData>, Serializable, HasPropertyValueList, HasPropertyValues, HasAnnotationPropertyValues, HasLogicalPropertyValues {

    @JsonCreator
    @Nonnull
    public static DataPropertyFrame get(@JsonProperty("subject") @Nonnull OWLDataPropertyData subject,
                                        @JsonProperty("propertyValues") @Nonnull ImmutableSet<PropertyValue> propertyValues,
                                        @JsonProperty("domains") @Nonnull ImmutableSet<OWLClassData> domains,
                                        @JsonProperty("ranges") @Nonnull ImmutableSet<OWLDatatypeData> ranges,
                                        @JsonProperty("functional") boolean functional) {
        return new AutoValue_DataPropertyFrame(subject,
                                               propertyValues,
                                               domains,
                                               ranges,
                                               functional);
    }

    @Nonnull
    public static DataPropertyFrame empty(@Nonnull OWLDataPropertyData subject) {
        return get(subject,
                   ImmutableSet.of(),
                   ImmutableSet.of(),
                   ImmutableSet.of(),
                   false);
    }

    @Override
    public abstract OWLDataPropertyData getSubject();

    @Override
    public abstract ImmutableSet<PropertyValue> getPropertyValues();

    @Nonnull
    public abstract ImmutableSet<OWLClassData> getDomains();

    @Nonnull
    public abstract ImmutableSet<OWLDatatypeData> getRanges();

    public abstract boolean isFunctional();

    @JsonIgnore
    @Override
    public PropertyValueList getPropertyValueList() {
        return new PropertyValueList(getPropertyValues());
    }

    @JsonIgnore
    @Override
    public ImmutableSet<PropertyAnnotationValue> getAnnotationPropertyValues() {
        return getPropertyValueList().getAnnotationPropertyValues();
    }

    @JsonIgnore
    @Override
    public ImmutableList<PropertyValue> getLogicalPropertyValues() {
        return getPropertyValueList().getLogicalPropertyValues();
    }

    @Nonnull
    @Override
    public PlainDataPropertyFrame toPlainFrame() {
        return PlainDataPropertyFrame.get(getSubject().getEntity(),
                                          getAnnotationPropertyValues().stream().map(PropertyAnnotationValue::toPlainPropertyValue).collect(toImmutableSet()),
                                          getDomains().stream().map(OWLClassData::getEntity).collect(toImmutableSet()),
                                          getRanges().stream().map(OWLDatatypeData::getEntity).collect(toImmutableSet()),
                                          isFunctional());
    }
}
