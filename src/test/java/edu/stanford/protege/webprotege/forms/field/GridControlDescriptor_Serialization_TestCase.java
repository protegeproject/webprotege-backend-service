package edu.stanford.protege.webprotege.forms.field;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.common.LanguageMap;
import edu.stanford.protege.webprotege.forms.FormSubjectFactoryDescriptor;
import edu.stanford.protege.webprotege.jackson.WebProtegeJacksonApplication;
import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi.model.EntityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.*;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.context.annotation.Import;


import java.io.*;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-05-05
 */
@JsonTest
@AutoConfigureJsonTesters
@Import(WebProtegeJacksonApplication.class)
public class GridControlDescriptor_Serialization_TestCase {

    @Autowired
    private JacksonTester<FormControlDescriptor> tester;

    @Test
    void shouldSerialize() throws IOException {
        var written = tester.write(GridControlDescriptor.get(ImmutableList.of(
                GridColumnDescriptor.get(
                        FormRegionId.generate(),
                        Optionality.OPTIONAL,
                        Repeatability.NON_REPEATABLE,
                        null,
                        LanguageMap.empty(),
                        EntityNameControlDescriptor.getDefault()
                )
        ), FormSubjectFactoryDescriptor.get(EntityType.CLASS,
                                            null, Optional.empty())));
        System.out.println(written.getJson());
        assertThat(written).hasJsonPathArrayValue("columns");
        assertThat(written).hasJsonPathMapValue("subjectFactory");
    }

    @Test
    void shouldSerializeWithNullSubjectFactory() throws IOException {
        var written = tester.write(GridControlDescriptor.get(ImmutableList.of(
                GridColumnDescriptor.get(
                        FormRegionId.generate(),
                        Optionality.OPTIONAL,
                        Repeatability.NON_REPEATABLE,
                        null,
                        LanguageMap.empty(),
                        EntityNameControlDescriptor.getDefault()
                )
        ), null));
        System.out.println(written.getJson());
        assertThat(written).hasJsonPathArrayValue("columns");
        assertThat(written).hasJsonPath("subjectFactory");
    }

    @Test
    void shouldDeserializeWithNonNullSubjectFactory() throws IOException {
        var json = """
                {"@type":"GRID","columns":[{"optionality":"OPTIONAL","repeatability":"NON_REPEATABLE","owlBinding":null,"label":{},"control":{"@type":"ENTITY_NAME","criteria":{"match":"CompositeCriteria","criteria":[{"match":"EntityTypeIsOneOf","types":["Class"]}],"matchType":"ALL"}},"id":"6bdcc85d-a514-4f4e-874b-f41a94ee8d2b"}],"subjectFactory":{"entityType":"Class","parent":null,"targetOntologyIri":null}}
                """;
        var read = tester.read(new StringReader(json));
        assertThat(read.getObject()).isInstanceOf(GridControlDescriptor.class);
        var obj = (GridControlDescriptor) read.getObject();
        assertThat(obj.getSubjectFactoryDescriptor()).isPresent();
        assertThat(obj.getColumns()).hasSize(1);
    }

    @Test
    void shouldDeserializeWithNullSubjectFactory() throws IOException {
        var json = """
                {"@type":"GRID","columns":[],"subjectFactory":null}
                """;
        var read = tester.read(new StringReader(json));
        assertThat(read.getObject()).isInstanceOf(GridControlDescriptor.class);
        var obj = (GridControlDescriptor) read.getObject();
        assertThat(obj.getSubjectFactoryDescriptor()).isEmpty();

    }

    @Test
    void shouldDeserializeWithMissingSubjectFactory() throws IOException {
        var json = """
                {"@type":"GRID","columns":[]}
                """;
        var read = tester.read(new StringReader(json));
        assertThat(read.getObject()).isInstanceOf(GridControlDescriptor.class);
        var obj = (GridControlDescriptor) read.getObject();
        assertThat(obj.getSubjectFactoryDescriptor()).isEmpty();

    }
}
