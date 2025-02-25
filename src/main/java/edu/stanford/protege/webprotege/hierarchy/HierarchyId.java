package edu.stanford.protege.webprotege.hierarchy;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br> Stanford University<br> Bio-Medical Informatics Research Group<br> Date: 22/03/2013
 */
@AutoValue
public abstract class HierarchyId {

    public static final HierarchyId CLASS_HIERARCHY = get("Class");

    public static final HierarchyId OBJECT_PROPERTY_HIERARCHY = get("ObjectProperty");

    public static final HierarchyId DATA_PROPERTY_HIERARCHY = get("DataProperty");

    public static final HierarchyId ANNOTATION_PROPERTY_HIERARCHY = get("AnnotationProperty");

    @JsonValue
    @Nonnull
    public abstract String getId();

    @Nonnull
    @JsonCreator
    public static HierarchyId get(@Nonnull String id) {
        return new AutoValue_HierarchyId(checkNotNull(id));
    }

    @JsonIgnore
    public boolean isBuiltIn() {
        return this.equals(CLASS_HIERARCHY) || this.equals(OBJECT_PROPERTY_HIERARCHY) || this.equals(DATA_PROPERTY_HIERARCHY) || this.equals(ANNOTATION_PROPERTY_HIERARCHY);
    }
}
