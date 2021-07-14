package edu.stanford.protege.webprotege.frame;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.entity.OWLClassData;
import edu.stanford.protege.webprotege.entity.OWLNamedIndividualData;

import javax.annotation.Nonnull;
import java.io.Serializable;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/12/2012
 */
@AutoValue

@JsonTypeName("NamedIndividualFrame")
public abstract class NamedIndividualFrame implements EntityFrame<OWLNamedIndividualData>, HasPropertyValues, HasAnnotationPropertyValues, HasLogicalPropertyValues, HasPropertyValueList, Serializable {

    @JsonCreator
    @Nonnull
    public static NamedIndividualFrame get(@JsonProperty("subject") @Nonnull OWLNamedIndividualData subject,
                                @JsonProperty("classes") @Nonnull ImmutableSet<OWLClassData> namedTypes,
                                @JsonProperty("propertyValues") @Nonnull ImmutableSet<PropertyValue> propertyValueList,
                                @JsonProperty("sameIndividuals") @Nonnull ImmutableSet<OWLNamedIndividualData> sameIndividuals) {
        return new AutoValue_NamedIndividualFrame(subject,
                                                  namedTypes,
                                                  propertyValueList,
                                                  sameIndividuals);
    }

    @Nonnull
    public static NamedIndividualFrame empty(@Nonnull OWLNamedIndividualData subject) {
        return get(subject,
                   ImmutableSet.of(),
                   ImmutableSet.of(),
                   ImmutableSet.of());
    }

    @Nonnull
    public abstract OWLNamedIndividualData getSubject();

    @Nonnull
    public abstract ImmutableSet<OWLClassData> getClasses();

    @Nonnull
    @Override
    public abstract ImmutableSet<PropertyValue> getPropertyValues();

    @Nonnull
    public abstract ImmutableSet<OWLNamedIndividualData> getSameIndividuals();

    @Override
    public ImmutableSet<PropertyAnnotationValue> getAnnotationPropertyValues() {
        return getPropertyValueList().getAnnotationPropertyValues();
    }

    @Nonnull
    @Override
    public ImmutableList<PropertyValue> getLogicalPropertyValues() {
        return getPropertyValueList().getLogicalPropertyValues();
    }

    @Nonnull
    @Override
    public PropertyValueList getPropertyValueList() {
        return new PropertyValueList(getPropertyValues());
    }

    @Nonnull
    @Override
    public PlainNamedIndividualFrame toPlainFrame() {
        return PlainNamedIndividualFrame.get(
                getSubject().getEntity(),
                getClasses().stream().map(OWLClassData::getEntity).collect(toImmutableSet()),
                getSameIndividuals().stream().map(OWLNamedIndividualData::getEntity).collect(toImmutableSet()),
                getPropertyValues().stream().map(PropertyValue::toPlainPropertyValue).collect(toImmutableSet())
        );
    }
}
