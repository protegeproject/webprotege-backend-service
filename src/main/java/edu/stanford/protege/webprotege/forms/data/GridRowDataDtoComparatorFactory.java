package edu.stanford.protege.webprotege.forms.data;

import edu.stanford.protege.webprotege.forms.FormRegionOrderingIndex;
import edu.stanford.protege.webprotege.forms.field.FormRegionOrdering;
import edu.stanford.protege.webprotege.forms.field.FormRegionOrderingDirection;
import edu.stanford.protege.webprotege.forms.field.GridColumnId;
import edu.stanford.protege.webprotege.forms.field.GridControlDescriptor;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

public class GridRowDataDtoComparatorFactory {


    private static final int FIRST_COLUMN = 0;

    @Nonnull
    private final GridCellDataDtoComparator cellComparator;

    @Nonnull
    private final FormRegionOrderingIndex orderingIndex;

    @Inject
    public GridRowDataDtoComparatorFactory(@Nonnull GridCellDataDtoComparator cellComparator,
                                           @Nonnull FormRegionOrderingIndex orderingIndex) {
        this.cellComparator = checkNotNull(cellComparator);
        this.orderingIndex = checkNotNull(orderingIndex);
    }

    @Nonnull
    public Comparator<GridRowDataDto> get(@Nonnull GridControlDescriptor descriptor,
                                          Optional<FormRegionOrderingDirection> directionOverride) {
        return orderingIndex.getOrderings()
                            .stream()
                            .filter(this::isColumnOrdering)
                            .map(ordering -> {
                                var leafColumnToTopLevelColumnMap = descriptor.getLeafColumnToTopLevelColumnMap();
                                var leafColumnId = (GridColumnId) ordering.getRegionId();
                                var topLevelColumnId = leafColumnToTopLevelColumnMap.get(leafColumnId);
                                int columnIndex = descriptor.getColumnIndex(topLevelColumnId);
                                if (columnIndex == -1) {
                                    return null;
                                }
                                var direction = directionOverride.orElse(ordering.getDirection());
                                return new GridRowDtoByColumnIndexComparator(
                                        cellComparator,
                                        direction,
                                        columnIndex);
                            })
                            .filter(Objects::nonNull)
                            .map(c -> (Comparator<GridRowDataDto>) c)
                            .reduce(Comparator::thenComparing)
                            // Compare by the first column
                            .orElseGet(() -> new GridRowDtoByColumnIndexComparator(cellComparator,
                                                                                   FormRegionOrderingDirection.ASC,
                                                                                   FIRST_COLUMN));
    }

    private boolean isColumnOrdering(FormRegionOrdering ordering) {
        return ordering.getRegionId() instanceof GridColumnId;
    }
}
