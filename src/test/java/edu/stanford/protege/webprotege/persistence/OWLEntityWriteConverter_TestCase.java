package edu.stanford.protege.webprotege.persistence;

import com.mongodb.DBObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Jul 16
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class OWLEntityWriteConverter_TestCase {

    private OWLEntityWriteConverter converter;

    @Mock
    private OWLEntity entity;

    private IRI iri = IRI.create("http://the.entity.iri");

    @BeforeEach
    public void setUp() throws Exception {
        converter = new OWLEntityWriteConverter();
        when(entity.getIRI()).thenReturn(iri);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldWriteOWLClass() {
        when(entity.getEntityType()).thenReturn((EntityType) EntityType.CLASS);
        DBObject dbObject = converter.convert(entity);
        assertThat(dbObject.get("type"), is("Class"));
        assertThat(dbObject.get("iri"), is(iri.toString()));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldWriteOWLObjectProperty() {
        when(entity.getEntityType()).thenReturn((EntityType) EntityType.OBJECT_PROPERTY);
        DBObject dbObject = converter.convert(entity);
        assertThat(dbObject.get("type"), is("ObjectProperty"));
        assertThat(dbObject.get("iri"), is(iri.toString()));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldWriteOWLDataProperty() {
        when(entity.getEntityType()).thenReturn((EntityType) EntityType.DATA_PROPERTY);
        DBObject dbObject = converter.convert(entity);
        assertThat(dbObject.get("type"), is("DataProperty"));
        assertThat(dbObject.get("iri"), is(iri.toString()));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldWriteOWLAnnotationProperty() {
        when(entity.getEntityType()).thenReturn((EntityType) EntityType.ANNOTATION_PROPERTY);
        DBObject dbObject = converter.convert(entity);
        assertThat(dbObject.get("type"), is("AnnotationProperty"));
        assertThat(dbObject.get("iri"), is(iri.toString()));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldWriteOWLNamedIndividual() {
        when(entity.getEntityType()).thenReturn((EntityType) EntityType.NAMED_INDIVIDUAL);
        DBObject dbObject = converter.convert(entity);
        assertThat(dbObject.get("type"), is("NamedIndividual"));
        assertThat(dbObject.get("iri"), is(iri.toString()));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldWriteOWLDatatype() {
        when(entity.getEntityType()).thenReturn((EntityType) EntityType.DATATYPE);
        DBObject dbObject = converter.convert(entity);
        assertThat(dbObject.get("type"), is("Datatype"));
        assertThat(dbObject.get("iri"), is(iri.toString()));
    }
}
