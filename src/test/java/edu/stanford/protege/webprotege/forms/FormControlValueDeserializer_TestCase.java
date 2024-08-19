package edu.stanford.protege.webprotege.forms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.hamcrest.Matchers.is;

@SpringBootTest
@DirtiesContext
public class FormControlValueDeserializer_TestCase {

//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    public void shouldRoundTripOwlClass() throws JsonProcessingException {
//        var value = EntityFormControlData.get(MockingUtils.mockOWLClass());
//        var s = objectMapper.writeValueAsString(value);
//        var read = objectMapper.readValue(s, PrimitiveFormControlData.class);
//        assertThat(value, is(read));
//    }
//
//    @Test
//    public void shouldRoundTripOwlObjectProperty() throws JsonProcessingException {
//        var value = EntityFormControlData.get(MockingUtils.mockOWLObjectProperty());
//        var s = objectMapper.writeValueAsString(value);
//        var read = objectMapper.readValue(s, PrimitiveFormControlData.class);
//        assertThat(value, is(read));
//    }
//
//    @Test
//    public void shouldRoundTripOwlDataProperty() throws JsonProcessingException {
//        var value = EntityFormControlData.get(MockingUtils.mockOWLDataProperty());
//        var s = objectMapper.writeValueAsString(value);
//        var read = objectMapper.readValue(s, PrimitiveFormControlData.class);
//        assertThat(value, is(read));
//    }
//
//    @Test
//    public void shouldRoundTripOwlAnnotationProperty() throws JsonProcessingException {
//        var value = EntityFormControlData.get(MockingUtils.mockOWLAnnotationProperty());
//        var s = objectMapper.writeValueAsString(value);
//        var read = objectMapper.readValue(s, PrimitiveFormControlData.class);
//        assertThat(value, is(read));
//    }
//
//    @Test
//    public void shouldRoundTripOwlNamedIndividual() throws JsonProcessingException {
//        var value = EntityFormControlData.get(MockingUtils.mockOWLNamedIndividual());
//        var s = objectMapper.writeValueAsString(value);
//        var read = objectMapper.readValue(s, PrimitiveFormControlData.class);
//        assertThat(value, is(read));
//    }
//
//    @Test
//    public void shouldRoundTripOwlDatatype() throws JsonProcessingException {
//        var value = EntityFormControlData.get(MockingUtils.mockOWLDatatype());
//        var s = objectMapper.writeValueAsString(value);
//        var read = objectMapper.readValue(s, PrimitiveFormControlData.class);
//        assertThat(value, is(read));
//    }
}