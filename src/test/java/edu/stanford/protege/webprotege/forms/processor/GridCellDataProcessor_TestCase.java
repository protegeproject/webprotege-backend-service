package edu.stanford.protege.webprotege.forms.processor;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.common.Page;
import edu.stanford.protege.webprotege.forms.FormFrameBuilder;
import edu.stanford.protege.webprotege.forms.data.FormControlData;
import edu.stanford.protege.webprotege.forms.data.GridCellData;
import edu.stanford.protege.webprotege.forms.field.OwlBinding;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.mockito.Mockito.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-26
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
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

    @BeforeEach
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
