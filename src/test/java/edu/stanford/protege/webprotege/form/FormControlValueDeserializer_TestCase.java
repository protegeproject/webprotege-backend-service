package edu.stanford.protege.webprotege.form;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.protege.webprotege.MockingUtils;
import edu.stanford.protege.webprotege.jackson.ObjectMapperProvider;
import edu.stanford.protege.webprotege.form.data.EntityFormControlData;
import edu.stanford.protege.webprotege.form.data.PrimitiveFormControlData;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class FormControlValueDeserializer_TestCase {

    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        var objectMapperProvider = new ObjectMapperProvider();
        objectMapper = objectMapperProvider.get();

    }

    @Test
    public void shouldRoundTripOwlClass() throws JsonProcessingException {
        var value = EntityFormControlData.get(MockingUtils.mockOWLClass());
        var s = objectMapper.writeValueAsString(value);
        var read = objectMapper.readValue(s, PrimitiveFormControlData.class);
        assertThat(value, is(read));
    }

    @Test
    public void shouldRoundTripOwlObjectProperty() throws JsonProcessingException {
        var value = EntityFormControlData.get(MockingUtils.mockOWLObjectProperty());
        var s = objectMapper.writeValueAsString(value);
        var read = objectMapper.readValue(s, PrimitiveFormControlData.class);
        assertThat(value, is(read));
    }

    @Test
    public void shouldRoundTripOwlDataProperty() throws JsonProcessingException {
        var value = EntityFormControlData.get(MockingUtils.mockOWLDataProperty());
        var s = objectMapper.writeValueAsString(value);
        var read = objectMapper.readValue(s, PrimitiveFormControlData.class);
        assertThat(value, is(read));
    }

    @Test
    public void shouldRoundTripOwlAnnotationProperty() throws JsonProcessingException {
        var value = EntityFormControlData.get(MockingUtils.mockOWLAnnotationProperty());
        var s = objectMapper.writeValueAsString(value);
        var read = objectMapper.readValue(s, PrimitiveFormControlData.class);
        assertThat(value, is(read));
    }

    @Test
    public void shouldRoundTripOwlNamedIndividual() throws JsonProcessingException {
        var value = EntityFormControlData.get(MockingUtils.mockOWLNamedIndividual());
        var s = objectMapper.writeValueAsString(value);
        var read = objectMapper.readValue(s, PrimitiveFormControlData.class);
        assertThat(value, is(read));
    }

    @Test
    public void shouldRoundTripOwlDatatype() throws JsonProcessingException {
        var value = EntityFormControlData.get(MockingUtils.mockOWLDatatype());
        var s = objectMapper.writeValueAsString(value);
        var read = objectMapper.readValue(s, PrimitiveFormControlData.class);
        assertThat(value, is(read));
    }
}