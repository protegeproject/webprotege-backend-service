package edu.stanford.protege.webprotege.forms;

import edu.stanford.protege.webprotege.forms.data.FormData;
import edu.stanford.protege.webprotege.forms.processor.FormDataConverter;
import edu.stanford.protege.webprotege.forms.processor.FormDataProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-26
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class FormDataConverter_TestCase {

    private FormDataConverter converter;

    @Mock
    private FormFrameBuilder formFrameBuilder;

    @Mock
    private FormSubjectResolver formSubjectResolver;

    @Mock
    private FormDataProcessor formDataProcessor;

    @Mock
    private FormData formData;

    @Mock
    private FormFrame formFrame;

    @BeforeEach
    public void setUp() {
        converter = new FormDataConverter(
                formSubjectResolver,
                formDataProcessor
        );
        when(formDataProcessor.processFormData(formData, false))
                .thenReturn(formFrameBuilder);
        when(formFrameBuilder.build(formSubjectResolver))
                .thenReturn(formFrame);
    }

    @Test
    public void shouldCreateFormFrame() {
        // Check that the converter uses the supplied form data processor and resolver correctly
        var converted = converter.convert(formData);
        assertThat(converted, is(formFrame));
    }
}
