package edu.stanford.protege.webprotege.forms.processor;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.forms.FormFrameBuilder;
import edu.stanford.protege.webprotege.forms.FormSubjectFactoryDescriptor;
import edu.stanford.protege.webprotege.forms.GridColumnBindingMissingException;
import edu.stanford.protege.webprotege.forms.data.FormEntitySubject;
import edu.stanford.protege.webprotege.forms.data.GridCellData;
import edu.stanford.protege.webprotege.forms.data.GridRowData;
import edu.stanford.protege.webprotege.forms.field.FormRegionId;
import edu.stanford.protege.webprotege.forms.field.GridColumnDescriptor;
import edu.stanford.protege.webprotege.forms.field.OwlBinding;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-26
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GridRowDataProcessor_TestCase {

    private GridRowDataProcessor processor;

    @Mock
    private FormFrameBuilder formFrameBuilder, rowformFrameBuilder;

    @Mock
    private GridCellDataProcessor gridCellDataProcessor;

    @Mock
    private OwlBinding binding;

    @Mock
    private FormSubjectFactoryDescriptor rowSubjectFactoryDescriptor;

    @Mock
    private GridColumnDescriptor columnDescriptor;

    @Mock
    private GridRowData gridRowData;

    @Mock
    private GridCellData gridCellData;

    @Mock
    private OwlBinding columnBinding;

    @BeforeEach
    public void setUp() {
        processor = new GridRowDataProcessor(() -> rowformFrameBuilder,
                                             gridCellDataProcessor);

        when(columnDescriptor.getId())
                .thenReturn(FormRegionId.generate());

        when(gridRowData.getCells())
                .thenReturn(ImmutableList.of(gridCellData));
        when(columnDescriptor.getOwlBinding())
                .thenReturn(Optional.of(columnBinding));
    }

    @Test
    public void shouldAddBindingForRow() {
        processor.processGridRowData(binding,
                                     rowSubjectFactoryDescriptor,
                                     ImmutableList.of(columnDescriptor),
                                     formFrameBuilder,
                                     gridRowData);
        verify(formFrameBuilder, times(1))
                .add(binding, rowformFrameBuilder);
    }

    @Test
    public void shouldNotSetSubjectIfRowDoesNotHaveSubject() {
        processor.processGridRowData(binding,
                                     rowSubjectFactoryDescriptor,
                                     ImmutableList.of(columnDescriptor),
                                     formFrameBuilder,
                                     gridRowData);
        verify(formFrameBuilder, never()).setSubject(any());
    }

    @Test
    public void shouldSetSubjectIfRowHasSubject() {
        var formSubject = mock(FormEntitySubject.class);
        when(gridRowData.getSubject())
                .thenReturn(Optional.of(formSubject));
        processor.processGridRowData(binding,
                                     rowSubjectFactoryDescriptor,
                                     ImmutableList.of(columnDescriptor),
                                     formFrameBuilder,
                                     gridRowData);
        var rowFrameCapture = ArgumentCaptor.forClass(FormFrameBuilder.class);
        verify(formFrameBuilder, times(1)).add(eq(binding), rowFrameCapture.capture());
        verify(rowFrameCapture.getValue(), times(1)).setSubject(formSubject);
    }

    @Test
    public void shouldAddBindingForCells() {
        processor.processGridRowData(binding,
                                     rowSubjectFactoryDescriptor,
                                     ImmutableList.of(columnDescriptor),
                                     formFrameBuilder,
                                     gridRowData);
        verify(gridCellDataProcessor, times(1))
                .processGridCellData(rowformFrameBuilder, columnBinding, gridCellData);
    }

    @Test
public void shouldThrowMissingBindingException() {
    assertThrows(GridColumnBindingMissingException.class, () -> { 
        when(columnDescriptor.getOwlBinding())
                .thenReturn(Optional.empty());
        processor.processGridRowData(binding,
                                     rowSubjectFactoryDescriptor,
                                     ImmutableList.of(columnDescriptor),
                                     formFrameBuilder,
                                     gridRowData);
     });
}
}
