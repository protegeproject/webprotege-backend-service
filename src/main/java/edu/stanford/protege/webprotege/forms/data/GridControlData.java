package edu.stanford.protege.webprotege.forms.data;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.common.Page;
import edu.stanford.protege.webprotege.forms.PropertyNames;
import edu.stanford.protege.webprotege.forms.field.FormRegionOrdering;
import edu.stanford.protege.webprotege.forms.field.GridControlDescriptor;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-30
 */
@AutoValue

@JsonTypeName("GridControlData")
public abstract class GridControlData implements ComplexFormControlValue {

    @JsonCreator
    @Nonnull
    public static GridControlData get(@JsonProperty(PropertyNames.CONTROL) @JsonAlias(PropertyNames.DESCRIPTOR) @Nonnull GridControlDescriptor descriptor,
                                      @JsonProperty(PropertyNames.ROWS) @Nonnull Page<GridRowData> rows,
                                      @JsonProperty(PropertyNames.ORDERING) @Nonnull ImmutableSet<FormRegionOrdering> ordering) {
        return new AutoValue_GridControlData(descriptor, rows, ordering);
    }

    @Override
    public <R> R accept(@Nonnull FormControlDataVisitorEx<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public void accept(@Nonnull FormControlDataVisitor visitor) {
        visitor.visit(this);
    }

    @JsonProperty(PropertyNames.CONTROL)
    @Nonnull
    public abstract GridControlDescriptor getDescriptor();

    @JsonProperty(PropertyNames.ROWS)
    @Nonnull
    public abstract Page<GridRowData> getRows();

    @JsonProperty(PropertyNames.ORDERING)
    @Nonnull
    public abstract ImmutableSet<FormRegionOrdering> getOrdering();
}
