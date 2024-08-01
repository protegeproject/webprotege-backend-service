package edu.stanford.protege.webprotege.forms.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import edu.stanford.protege.webprotege.forms.*;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-24
 */
@JsonTypeName(GridControlDescriptor.TYPE)
@AutoValue

public abstract class GridControlDescriptor implements FormControlDescriptor {

    protected static final String TYPE = "GRID";

    public static String getType() {
        return TYPE;
    }

    @JsonCreator
    @Nonnull
    public static GridControlDescriptor get(@Nonnull @JsonProperty(PropertyNames.COLUMNS) ImmutableList<GridColumnDescriptor> columnDescriptors,
                                            @Nullable @JsonProperty(PropertyNames.SUBJECT_FACTORY) FormSubjectFactoryDescriptor subjectFactoryDescriptor) {
        return new AutoValue_GridControlDescriptor(columnDescriptors == null ? ImmutableList.of() : columnDescriptors,
                                                   subjectFactoryDescriptor);
    }

    @Nonnull
    @Override
    public String getAssociatedType() {
        return getType();
    }

    @JsonProperty(PropertyNames.COLUMNS)
    @Nonnull
    public abstract ImmutableList<GridColumnDescriptor> getColumns();

    @Override
    public <R> R accept(@Nonnull FormControlDescriptorVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @JsonIgnore
    public int getNestedColumnCount() {
        int count = 0;
        for (GridColumnDescriptor columnDescriptor : getColumns()) {
            count += columnDescriptor.getNestedColumnCount();
        }
        return count;
    }


    @JsonProperty(PropertyNames.SUBJECT_FACTORY)
    @Nullable
    protected abstract FormSubjectFactoryDescriptor getSubjectFactoryDescriptorInternal();

    @JsonIgnore
    public Optional<FormSubjectFactoryDescriptor> getSubjectFactoryDescriptor() {
        return Optional.ofNullable(getSubjectFactoryDescriptorInternal());
    }

    @JsonIgnore
    @Nonnull
    public Stream<GridColumnDescriptor> getLeafColumns() {
        return getColumns().stream().flatMap(GridColumnDescriptor::getLeafColumnDescriptors);
    }

    /**
     * Returns a map of leaf column Ids to top level columns in this grid.  If a column does not
     * have nested columns then the column will map to itself.
     */
    @JsonIgnore
    public ImmutableMap<FormRegionId, FormRegionId> getLeafColumnToTopLevelColumnMap() {
        ImmutableMap.Builder<FormRegionId, FormRegionId> builder = ImmutableMap.builder();
        getColumns().forEach(topLevelColumn -> {
            topLevelColumn.getLeafColumnDescriptors().map(GridColumnDescriptor::getId).forEach(leafColumnId -> {
                builder.put(leafColumnId, topLevelColumn.getId());
            });
        });
        return builder.build();
    }

    /**
     * Gets the index of the specified columnId.
     *
     * @param columnId The {@link FormRegionId}
     * @return The column index of the specified column Id.  A value of -1 is returned if the
     * {@link FormRegionId} does not identify a column in this grid.
     */
    @JsonIgnore
    public int getColumnIndex(FormRegionId columnId) {
        ImmutableList<GridColumnDescriptor> columns = getColumns();
        for (int i = 0; i < columns.size(); i++) {
            if (columns.get(i).getId().equals(columnId)) {
                return i;
            }
        }
        return -1;
    }

}
