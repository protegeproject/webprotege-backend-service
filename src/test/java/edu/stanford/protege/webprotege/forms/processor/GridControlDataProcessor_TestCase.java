package edu.stanford.protege.webprotege.forms.processor;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.forms.FormFrameBuilder;
import edu.stanford.protege.webprotege.forms.FormSubjectFactoryDescriptor;
import edu.stanford.protege.webprotege.forms.FormSubjectFactoryDescriptorMissingException;
import edu.stanford.protege.webprotege.forms.data.GridControlData;
import edu.stanford.protege.webprotege.forms.data.GridRowData;
import edu.stanford.protege.webprotege.forms.field.FormRegionOrdering;
import edu.stanford.protege.webprotege.forms.field.GridColumnDescriptor;
import edu.stanford.protege.webprotege.forms.field.GridControlDescriptor;
import edu.stanford.protege.webprotege.forms.field.OwlBinding;
import edu.stanford.protege.webprotege.common.Page;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-26
 */
@RunWith(MockitoJUnitRunner.class)
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

    @Before
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

    @Test(expected = FormSubjectFactoryDescriptorMissingException.class)
    public void shouldThrowExceptionForMissingRowSubjectFactoryDescriptor() {
        when(gridControlDescriptor.getSubjectFactoryDescriptor())
                .thenReturn(Optional.empty());
        processor.processGridControlData(binding, gridControlData, formFrameBuilder);

    }
}
