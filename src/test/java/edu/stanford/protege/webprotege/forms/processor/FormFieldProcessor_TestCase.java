package edu.stanford.protege.webprotege.forms.processor;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.common.Page;
import edu.stanford.protege.webprotege.forms.FormFieldBindingMissingException;
import edu.stanford.protege.webprotege.forms.FormFrameBuilder;
import edu.stanford.protege.webprotege.forms.data.FormControlData;
import edu.stanford.protege.webprotege.forms.data.FormFieldData;
import edu.stanford.protege.webprotege.forms.field.FormFieldDescriptor;
import edu.stanford.protege.webprotege.forms.field.FormRegionId;
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
public class FormFieldProcessor_TestCase {

    private FormFieldProcessor formFieldProcessor;

    @Mock
    private FormControlDataProcessor formControlDataProcessor;

    @Mock
    private OwlBinding binding;

    @Mock
    private FormControlData formControlData;

    @Mock
    private FormFrameBuilder formFrameBuilder;

    @Mock
    private FormFieldData fieldData;

    @Mock
    private FormFieldDescriptor formFieldDescriptor;

    private FormRegionId fieldId;

    @BeforeEach
    public void setUp() {
        fieldId = FormRegionId.generate();
        formFieldProcessor = new FormFieldProcessor(formControlDataProcessor);
        when(fieldData.getFormFieldDescriptor())
                .thenReturn(formFieldDescriptor);
        when(formFieldDescriptor.getId())
                .thenReturn(fieldId);
        when(formFieldDescriptor.getOwlBinding())
                .thenReturn(Optional.of(binding));
        when(fieldData.getFormControlData())
                .thenReturn(Page.create(1, 1, ImmutableList.of(formControlData), 1));
    }

    @Test
    public void shouldProcessFormControlData() {
        formFieldProcessor.processFormFieldData(fieldData, formFrameBuilder);
        verify(formControlDataProcessor, times(1))
                .processFormControlData(binding, formControlData, formFrameBuilder);
    }

    @Test
public void shouldThrowMissingBindingExceptionIfBindingIsNotPresent() {
    assertThrows(FormFieldBindingMissingException.class, () -> { 
        when(formFieldDescriptor.getOwlBinding())
                .thenReturn(Optional.empty());
        formFieldProcessor.processFormFieldData(fieldData, formFrameBuilder);
     });
}
}
