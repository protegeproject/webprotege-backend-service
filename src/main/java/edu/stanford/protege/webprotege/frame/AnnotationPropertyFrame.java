package edu.stanford.protege.webprotege.frame;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.entity.OWLAnnotationPropertyData;
import edu.stanford.protege.webprotege.entity.OWLEntityData;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/11/2012
 */
@AutoValue

@JsonTypeName("AnnotationPropertyFrame")
public abstract class AnnotationPropertyFrame implements EntityFrame<OWLAnnotationPropertyData>, HasPropertyValueList {

    @Nonnull
    public static AnnotationPropertyFrame empty(@Nonnull OWLAnnotationPropertyData subject) {
        return get(subject,
                   ImmutableSet.of(),
                   ImmutableSet.of(),
                   ImmutableSet.of());
    }

    @Nonnull
    public abstract OWLAnnotationPropertyData getSubject();

    @Nonnull
    public abstract ImmutableSet<PropertyAnnotationValue> getPropertyValues();

    @Nonnull
    public abstract ImmutableSet<OWLEntityData> getDomains();

    @Nonnull
    public abstract ImmutableSet<OWLEntityData> getRanges();


    @JsonCreator
    @Nonnull
    public static AnnotationPropertyFrame get(@JsonProperty("subject") @Nonnull OWLAnnotationPropertyData subject,
                                              @JsonProperty("propertyValues") @Nonnull ImmutableSet<PropertyAnnotationValue> propertyValues,
                                              @JsonProperty("domains") @Nonnull ImmutableSet<OWLEntityData> domains,
                                              @JsonProperty("ranges") @Nonnull ImmutableSet<OWLEntityData> ranges) {
        return new AutoValue_AnnotationPropertyFrame(subject,
                                                     propertyValues,
                                                     domains,
                                                     ranges);
    }

    @JsonIgnore
    @Override
    public PropertyValueList getPropertyValueList() {
        return new PropertyValueList(getPropertyValues());
    }

    @Nonnull
    @Override
    public PlainAnnotationPropertyFrame toPlainFrame() {
        return PlainAnnotationPropertyFrame.get(
                getSubject().getEntity(),
                getPropertyValues().stream().map(PropertyAnnotationValue::toPlainPropertyValue).collect(toImmutableSet()),
                getDomains().stream().map(OWLEntityData::getEntity).map(OWLEntity::getIRI).collect(toImmutableSet()),
                getRanges().stream().map(OWLEntityData::getEntity).map(OWLEntity::getIRI).collect(toImmutableSet()));
    }
}
