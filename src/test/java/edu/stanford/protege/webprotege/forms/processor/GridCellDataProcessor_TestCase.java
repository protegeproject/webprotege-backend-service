package edu.stanford.protege.webprotege.forms.processor;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.forms.FormFrameBuilder;
import edu.stanford.protege.webprotege.forms.data.FormControlData;
import edu.stanford.protege.webprotege.forms.data.GridCellData;
import edu.stanford.protege.webprotege.forms.field.OwlBinding;
import edu.stanford.protege.webprotege.common.Page;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-26
 */
@RunWith(MockitoJUnitRunner.class)
public class GridCellDataProcessor_TestCase {

    private GridCellDataProcessor processor;

    @Mock
    private FormControlDataProcessor formControlDataProcessor;

    @Mock
    private FormFrameBuilder formFrameBuilder;

    @Mock
    private OwlBinding binding;

    @Mock
    private GridCellData gridCellData;

    @Mock
    private FormControlData formControlData;

    private ImmutableList<FormControlData> values;

    @Before
    public void setUp() {
        values = ImmutableList.of(formControlData);
        processor = new GridCellDataProcessor(() -> formControlDataProcessor);
        when(gridCellData.getValues())
                .thenReturn(Page.create(1, 1, values, values.size()));
    }

    @Test
    public void shouldProcessCellData() {
        processor.processGridCellData(formFrameBuilder,
                                      binding,
                                      gridCellData);
        verify(formControlDataProcessor, times(1))
                .processFormControlData(binding, formControlData, formFrameBuilder);
    }
}
