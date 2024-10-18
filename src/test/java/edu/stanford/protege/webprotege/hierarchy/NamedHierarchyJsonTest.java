package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.common.LanguageMap;
import edu.stanford.protege.webprotege.jackson.WebProtegeJacksonApplication;
import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.context.annotation.Import;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@Import(WebProtegeJacksonApplication.class)
class NamedHierarchyJsonTest {

    @Autowired
    private JacksonTester<NamedHierarchy> jacksonTester;

    @Autowired
    private OWLDataFactory dataFactory;

    @Test
    void testSerializeNamedHierarchy() throws Exception {
        var hierarchyId = HierarchyId.get("test-hierarchy-id");
        var label = LanguageMap.of("en", "Test Label");
        var description = LanguageMap.of("en", "Test Description");
        var owlClass = dataFactory.getOWLThing();
        var hierarchyDescriptor = new ClassHierarchyDescriptor(Set.of(owlClass));

        var namedHierarchy = new NamedHierarchy(hierarchyId, label, description, hierarchyDescriptor);

        var json = jacksonTester.write(namedHierarchy);

        assertThat(json).hasJsonPath("$.hierarchyId");
        assertThat(json).hasJsonPath("$.label");
        assertThat(json).hasJsonPath("$.description");
        assertThat(json).hasJsonPath("$.hierarchyDescriptor");
    }

    @Test
    void testDeserializeNamedHierarchy() throws Exception {
        var json = """
        {
            "hierarchyId": "test-hierarchy-id",
            "label": {
                "en": "Test Label"
            },
            "description": {
                "en": "Test Description"
            },
            "hierarchyDescriptor": {
                "@type": "ClassHierarchyDescriptor",
                "roots": [
                    {
                        "iri": "http://www.w3.org/2002/07/owl#Thing"
                    }
                ]
            }
        }
        """;

        var objectContent = jacksonTester.parse(json);
        var namedHierarchy = objectContent.getObject();

        assertThat(namedHierarchy.hierarchyId()).isNotNull();
        assertThat(namedHierarchy.label()).isNotNull();
        assertThat(namedHierarchy.description()).isNotNull();
        assertThat(namedHierarchy.hierarchyDescriptor()).isNotNull();
    }

}