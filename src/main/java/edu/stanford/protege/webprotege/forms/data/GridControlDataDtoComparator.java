package edu.stanford.protege.webprotege.forms.data;

import com.google.common.collect.Comparators;
import edu.stanford.protege.webprotege.forms.field.FormRegionOrderingDirection;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Comparator;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

public class GridControlDataDtoComparator implements Comparator<GridControlDataDto> {

    @Nonnull
    private final GridRowDataDtoComparatorFactory comparatorFactory;

    @Inject
    public GridControlDataDtoComparator(@Nonnull GridRowDataDtoComparatorFactory comparatorFactory) {
        this.comparatorFactory = checkNotNull(comparatorFactory);
    }

    @Override
    public int compare(GridControlDataDto o1, GridControlDataDto o2) {
        var gridControlDescriptor1 = o1.getDescriptor();
        var gridControlDescriptor2 = o2.getDescriptor();
        if(!gridControlDescriptor1.equals(gridControlDescriptor2)) {
            throw new RuntimeException("Incomparable grids");
        }
        // Always sort ascending otherwise reversed order gets cancelled out
        var directionOverride = Optional.of(FormRegionOrderingDirection.ASC);
        var comparator = comparatorFactory.get(gridControlDescriptor1, directionOverride);
        return Comparators.lexicographical(comparator).compare(o1.getRows().getPageElements(),
                                                               o2.getRows().getPageElements());
    }
}
