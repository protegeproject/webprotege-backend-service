package edu.stanford.protege.webprotege.forms.processor;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.common.Page;
import edu.stanford.protege.webprotege.forms.FormFrameBuilder;
import edu.stanford.protege.webprotege.forms.FormSubjectFactoryDescriptor;
import edu.stanford.protege.webprotege.forms.FormSubjectFactoryDescriptorMissingException;
import edu.stanford.protege.webprotege.forms.data.GridControlData;
import edu.stanford.protege.webprotege.forms.data.GridRowData;
import edu.stanford.protege.webprotege.forms.field.FormRegionOrdering;
import edu.stanford.protege.webprotege.forms.field.GridColumnDescriptor;
import edu.stanford.protege.webprotege.forms.field.GridControlDescriptor;
import edu.stanford.protege.webprotege.forms.field.OwlBinding;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-26
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GridControlDataProcessor_TestCase {

    GridControlDataProcessor processor;

    @Mock
    private GridRowDataProcessor gridRowDataProcessor;

    @Mock
    private OwlBinding binding;

    @Mock
    private GridControlDescriptor gridControlDescriptor;

    private ImmutableList<GridColumnDescriptor> columns = ImmutableList.of(mock(GridColumnDescriptor.class));

    @Mock
    private FormFrameBuilder formFrameBuilder;

    @Mock
    private FormSubjectFactoryDescriptor rowSubjectFactoryDescriptor;

    @Mock
    private GridRowData gridRowData;

    private Page<GridRowData> page;

    private GridControlData gridControlData;

    private ImmutableSet<FormRegionOrdering> ordering = ImmutableSet.of();

    @BeforeEach
    public void setUp() {
        processor = new GridControlDataProcessor(gridRowDataProcessor);
        when(gridControlDescriptor.getColumns())
                .thenReturn(columns);
        page = Page.create(1, 1, ImmutableList.of(gridRowData), 1);
        gridControlData = GridControlData.get(gridControlDescriptor, page, ordering);
    }

    @Test
    public void shouldProcessGridRows() {
        when(gridControlDescriptor.getSubjectFactoryDescriptor())
                .thenReturn(Optional.of(rowSubjectFactoryDescriptor));
        processor.processGridControlData(binding, gridControlData, formFrameBuilder);
        verify(gridRowDataProcessor, times(1))
                .processGridRowData(binding, rowSubjectFactoryDescriptor, columns, formFrameBuilder, gridRowData);
    }

    @Test
public void shouldThrowExceptionForMissingRowSubjectFactoryDescriptor() {
    assertThrows(FormSubjectFactoryDescriptorMissingException.class, () -> { 
        when(gridControlDescriptor.getSubjectFactoryDescriptor())
                .thenReturn(Optional.empty());
        processor.processGridControlData(binding, gridControlData, formFrameBuilder);

     });
}
}
