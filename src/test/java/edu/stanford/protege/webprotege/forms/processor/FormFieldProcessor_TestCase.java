package edu.stanford.protege.webprotege.forms.processor;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.forms.FormFrameBuilder;
import edu.stanford.protege.webprotege.forms.FormFieldBindingMissingException;
import edu.stanford.protege.webprotege.forms.data.FormControlData;
import edu.stanford.protege.webprotege.forms.data.FormFieldData;
import edu.stanford.protege.webprotege.forms.field.*;
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

    @Before
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

    @Test(expected = FormFieldBindingMissingException.class)
    public void shouldThrowMissingBindingExceptionIfBindingIsNotPresent() {
        when(formFieldDescriptor.getOwlBinding())
                .thenReturn(Optional.empty());
        formFieldProcessor.processFormFieldData(fieldData, formFrameBuilder);
    }
}
