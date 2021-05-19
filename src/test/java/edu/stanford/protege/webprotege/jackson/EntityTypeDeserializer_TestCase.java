package edu.stanford.protege.webprotege.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.EntityType;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class EntityTypeDeserializer_TestCase {

    private ObjectMapper objectMapper;



    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapperProvider().get();
    }

    @Test
    public void shouldDeserializeOwlClassAsClass() throws JsonProcessingException {
        var read = objectMapper.readValue("\"owl:Class\"", EntityType.class);
        assertThat(read, is(EntityType.CLASS));
    }

    @Test
    public void shouldDeserializeClassAsClass() throws JsonProcessingException {
        var read = objectMapper.readValue("\"Class\"", EntityType.class);
        assertThat(read, is(EntityType.CLASS));
    }

    @Test
    public void shouldDeserializeOwlObjectPropertyAsObjectProperty() throws JsonProcessingException {
        var read = objectMapper.readValue("\"owl:ObjectProperty\"", EntityType.class);
        assertThat(read, is(EntityType.OBJECT_PROPERTY));
    }

    @Test
    public void shouldDeserializeObjectPropertyAsObjectProperty() throws JsonProcessingException {
        var read = objectMapper.readValue("\"ObjectProperty\"", EntityType.class);
        assertThat(read, is(EntityType.OBJECT_PROPERTY));
    }

    @Test
    public void shouldDeserializeOwlAnnotationPropertyAsAnnotationProperty() throws JsonProcessingException {
        var read = objectMapper.readValue("\"owl:AnnotationProperty\"", EntityType.class);
        assertThat(read, is(EntityType.ANNOTATION_PROPERTY));
    }

    @Test
    public void shouldDeserializeAnnotationPropertyAsAnnotationProperty() throws JsonProcessingException {
        var read = objectMapper.readValue("\"AnnotationProperty\"", EntityType.class);
        assertThat(read, is(EntityType.ANNOTATION_PROPERTY));
    }

    @Test
    public void shouldDeserializeOwlDataPropertyAsDataProperty() throws JsonProcessingException {
        var read = objectMapper.readValue("\"owl:DatatypeProperty\"", EntityType.class);
        assertThat(read, is(EntityType.DATA_PROPERTY));
    }

    @Test
    public void shouldDeserializeDataPropertyAsDataProperty() throws JsonProcessingException {
        var read = objectMapper.readValue("\"DataProperty\"", EntityType.class);
        assertThat(read, is(EntityType.DATA_PROPERTY));
    }

    @Test
    public void shouldDeserializeOwlNamedIndividualAsNamedIndividual() throws JsonProcessingException {
        var read = objectMapper.readValue("\"owl:NamedIndividual\"", EntityType.class);
        assertThat(read, is(EntityType.NAMED_INDIVIDUAL));
    }

    @Test
    public void shouldDeserializeNamedIndividualAsNamedIndividual() throws JsonProcessingException {
        var read = objectMapper.readValue("\"NamedIndividual\"", EntityType.class);
        assertThat(read, is(EntityType.NAMED_INDIVIDUAL));
    }

    @Test
    public void shouldDeserializeRdfsDatatypeAsDatatype() throws JsonProcessingException {
        var read = objectMapper.readValue("\"rdfs:Datatype\"", EntityType.class);
        assertThat(read, is(EntityType.DATATYPE));
    }

    @Test
    public void shouldDeserializeDatatypeAsDatatype() throws JsonProcessingException {
        var read = objectMapper.readValue("\"Datatype\"", EntityType.class);
        assertThat(read, is(EntityType.DATATYPE));
    }
}