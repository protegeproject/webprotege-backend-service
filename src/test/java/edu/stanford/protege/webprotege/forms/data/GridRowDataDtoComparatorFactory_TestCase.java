package edu.stanford.protege.webprotege.forms.data;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import edu.stanford.protege.webprotege.common.LanguageMap;
import edu.stanford.protege.webprotege.forms.FormRegionOrderingIndex;
import edu.stanford.protege.webprotege.forms.FormSubjectFactoryDescriptor;
import edu.stanford.protege.webprotege.forms.field.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GridRowDataDtoComparatorFactory_TestCase {

    public static final Optionality REQUIRED = Optionality.REQUIRED;

    public static final Repeatability NON_REPEATABLE = Repeatability.NON_REPEATABLE;


    @Mock
    public LanguageMap columnLabel = LanguageMap.empty();

    @Mock
    public OwlBinding owlBinding;

    @Mock
    public FormSubjectFactoryDescriptor subjectFactoryDescriptor;


    private GridRowDataDtoComparatorFactory comparatorFactory;

    @Mock
    private GridCellDataDtoComparator gridCellDataDtoComparator;

    @Mock
    private FormRegionOrderingIndex orderingIndex;

    @BeforeEach
    public void setUp() throws Exception {
        comparatorFactory = new GridRowDataDtoComparatorFactory(gridCellDataDtoComparator,
                                                                orderingIndex);
    }

    @Test
    public void shouldCreateComparatorForFirstColumn() {
        var columnId = generateColumnId();
        var textControlDescriptor = TextControlDescriptor.getDefault();
        var desc = createGridControlDescriptorWithColumns(ImmutableMap.of(columnId, textControlDescriptor));
        var comparator = (GridRowDtoByColumnIndexComparator) createComparatorOrderByColumnIds(desc, columnId);
        var columnIndex = comparator.getColumnIndex();
        assertThat(columnIndex, equalTo(0));
    }

    @Test
    public void shouldCreateComparatorForSecondColumn() {
        var column0Id = generateColumnId();
        var column1Id = generateColumnId();
        var textControlDescriptor = TextControlDescriptor.getDefault();
        var gridControlDescriptor = createGridControlDescriptorWithColumns(ImmutableMap.of(column0Id, textControlDescriptor,
                                                              column1Id, textControlDescriptor));
        var comparator = (GridRowDtoByColumnIndexComparator) createComparatorOrderByColumnIds(gridControlDescriptor, column1Id);
        var columnIndex = comparator.getColumnIndex();
        assertThat(columnIndex, equalTo(1));
    }

    @Test
    public void shouldCreateComparatorForFirstColumnThenSecondColumn() {
        var column0Id = generateColumnId();
        var nestedColumn0Id = generateColumnId();
        var nestedColumn1Id = generateColumnId();
        var controlDescriptor = createGridControlDescriptorWithColumns(Map.of(nestedColumn0Id, TextControlDescriptor.getDefault(),
                                                                              nestedColumn1Id, TextControlDescriptor.getDefault()));
        var gridControlDescriptor = createGridControlDescriptorWithColumns(ImmutableMap.of(column0Id, controlDescriptor));
        var comparator = (GridRowDtoByColumnIndexComparator) createComparatorOrderByColumnIds(gridControlDescriptor, nestedColumn1Id);
        var columnIndex = comparator.getColumnIndex();
        assertThat(columnIndex, equalTo(0));
    }

    public Comparator<GridRowDataDto> createComparatorOrderByColumnIds(GridControlDescriptor gridControlDescriptor,
                                                                       FormRegionId... columnIds) {
        var orderBys = Stream.of(columnIds)
              .map(columnId -> FormRegionOrdering.get(columnId, FormRegionOrderingDirection.ASC))
              .collect(toImmutableSet());
        when(orderingIndex.getOrderings())
                .thenReturn(orderBys);
        return comparatorFactory.get(gridControlDescriptor, Optional.empty());
    }


    private GridControlDescriptor createGridControlDescriptorWithColumns(Map<FormRegionId, FormControlDescriptor> colId2ControlDescriptor) {
        var columnDescriptors = colId2ControlDescriptor
                .entrySet()
                .stream()
              .map(k -> createGridColumnDescriptor(k.getValue(), k.getKey()))
              .collect(toImmutableList());
        var descriptors = ImmutableList.copyOf(columnDescriptors);
        return createGridControlDescriptor(descriptors);
    }



    public GridControlDescriptor createGridControlDescriptor(ImmutableList<GridColumnDescriptor> columnDescriptors) {
        return GridControlDescriptor.get(
                columnDescriptors,
                subjectFactoryDescriptor
        );
    }

    private GridColumnDescriptor createGridColumnDescriptor(FormControlDescriptor formControlDescriptor,
                                                            FormRegionId columnId) {
        return GridColumnDescriptor.get(
                columnId,
                REQUIRED,
                NON_REPEATABLE,
                owlBinding,
                columnLabel,
                formControlDescriptor
        );
    }

    public static FormRegionId generateColumnId() {
        return FormRegionId.get(UUID.randomUUID().toString());
    }
}