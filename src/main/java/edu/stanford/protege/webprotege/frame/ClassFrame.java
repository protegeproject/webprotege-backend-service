package edu.stanford.protege.webprotege.frame;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.entity.OWLClassData;

import javax.annotation.Nonnull;
import java.io.Serializable;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/11/2012
 * <p>
 * A class frame describes some class in terms of other classEntries and property values.
 * </p>
 */
@AutoValue

@JsonTypeName("ClassFrame")
public abstract class ClassFrame implements EntityFrame<OWLClassData>, Serializable, HasPropertyValueList, HasPropertyValues, HasAnnotationPropertyValues, HasLogicalPropertyValues {


    @JsonCreator
    @Nonnull
    public static ClassFrame get(@JsonProperty("subject") @Nonnull OWLClassData subject,
                                 @JsonProperty("classes") @Nonnull ImmutableSet<OWLClassData> classEntries,
                                 @JsonProperty("propertyValues") @Nonnull ImmutableSet<PropertyValue> propertyValues) {

        return new AutoValue_ClassFrame(subject,
                                        classEntries,
                                        propertyValues);
    }

    public static ClassFrame empty(@Nonnull OWLClassData subject) {
        return get(subject, ImmutableSet.of(), ImmutableSet.of());
    }

    /**
     * Gets the subject of this class frame.
     *
     * @return The subject.  Not {@code null}.
     */
    @JsonProperty("subject")
    @Nonnull
    public abstract OWLClassData getSubject();

    @JsonProperty("classes")
    @Nonnull
    public abstract ImmutableSet<OWLClassData> getClassEntries();

    /**
     * Gets the {@link PropertyValue}s in this frame.
     *
     * @return The (possibly empty) set of property values in this frame. Not {@code null}.  The returned set is unmodifiable.
     */
    @JsonProperty("propertyValues")
    @Nonnull
    public abstract ImmutableSet<PropertyValue> getPropertyValues();


    @JsonIgnore
    @Nonnull
    @Override
    public PropertyValueList getPropertyValueList() {
        return new PropertyValueList(getPropertyValues());
    }

    @JsonIgnore
    @Nonnull
    public ImmutableSet<PropertyAnnotationValue> getAnnotationPropertyValues() {
        return getPropertyValueList().getAnnotationPropertyValues();
    }

    @JsonIgnore
    @Nonnull
    public ImmutableList<PropertyValue> getLogicalPropertyValues() {
        return getPropertyValueList().getLogicalPropertyValues();
    }

    @Override
    @Nonnull
    public PlainClassFrame toPlainFrame() {
        return PlainClassFrame.get(
                getSubject().getEntity(),
                getClassEntries().stream().map(OWLClassData::getEntity).collect(toImmutableSet()),
                getPropertyValues().stream().map(PropertyValue::toPlainPropertyValue).collect(toImmutableSet())
        );
    }
}
