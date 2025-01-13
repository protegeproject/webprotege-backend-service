package edu.stanford.protege.webprotege.forms.processor;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.forms.FormDescriptor;
import edu.stanford.protege.webprotege.forms.FormFrameBuilder;
import edu.stanford.protege.webprotege.forms.FormSubjectFactoryDescriptor;
import edu.stanford.protege.webprotege.forms.FormSubjectFactoryDescriptorMissingException;
import edu.stanford.protege.webprotege.forms.data.FormData;
import edu.stanford.protege.webprotege.forms.data.FormEntitySubject;
import edu.stanford.protege.webprotege.forms.data.FormFieldData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-26
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class FormDataProcessor_TestCase {

    private FormDataProcessor processor;

    @Mock
    private FormFrameBuilder formFrameBuilder;

    @Mock
    private FormFieldProcessor formFieldProcessor;

    @Mock
    private FormData formData;

    @Mock
    private FormFieldData formFieldData;

    @Mock
    private FormEntitySubject subject;

    @Mock
    private FormDescriptor formDescriptor;

    @Mock
    private FormSubjectFactoryDescriptor subjectFactoryDescriptor;

    @BeforeEach
    public void setUp() {
        processor = new FormDataProcessor(() -> formFrameBuilder,
                                          formFieldProcessor);
        when(formData.getFormFieldData())
                .thenReturn(ImmutableList.of(formFieldData));
        when(formData.getSubject())
                .thenReturn(Optional.of(subject));
        when(formData.getFormDescriptor())
                .thenReturn(formDescriptor);
        when(formDescriptor.getSubjectFactoryDescriptor())
                .thenReturn(Optional.of(subjectFactoryDescriptor));
    }

    @Test
    public void shouldUseFormFrameBuilder() {
        var ffb = processor.processFormData(formData, false);
        assertThat(ffb, is(formFrameBuilder));
    }

    @Test
    public void shouldProcessSubject() {
        processor.processFormData(formData, false);
        verify(formFrameBuilder, times(1))
                .setSubject(subject);
    }

    @Test
    public void shouldProcessFormFieldData() {
        processor.processFormData(formData, false);
        verify(formFieldProcessor, times(1))
                .processFormFieldData(formFieldData, formFrameBuilder);
    }

    @Test
    public void shouldSetSubjectFactory() {
        processor.processFormData(formData, false);
        verify(formFrameBuilder, times(1))
                .setSubjectFactoryDescriptor(subjectFactoryDescriptor);
    }

    @Test
public void shouldThrowExceptionIfSubjectFactoryDescriptorIsMissing() {
    assertThrows(FormSubjectFactoryDescriptorMissingException.class, () -> { 
        when(formDescriptor.getSubjectFactoryDescriptor())
                .thenReturn(Optional.empty());
        processor.processFormData(formData, true);
     });
}
}
