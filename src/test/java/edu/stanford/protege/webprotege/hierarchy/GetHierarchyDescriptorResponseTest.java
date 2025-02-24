package edu.stanford.protege.webprotege.hierarchy;

import static org.assertj.core.api.Assertions.assertThat;

import edu.stanford.protege.webprotege.jackson.WebProtegeJacksonApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.context.annotation.Import;

import java.util.Optional;

@JsonTest
@Import(WebProtegeJacksonApplication.class)
class GetHierarchyDescriptorResponseTest {

    @Autowired
    private JacksonTester<GetHierarchyDescriptorResponse> tester;

    @Test
    void shouldSerializeResponseWithNonNullHierarchyDescriptor() throws Exception {
        var hierarchyDescriptor = ClassHierarchyDescriptor.create();
        var response = new GetHierarchyDescriptorResponse(hierarchyDescriptor);
        var jsonContent = tester.write(response);

        // Verify that the hierarchyDescriptor field is serialized with the expected type.
        assertThat(jsonContent).extractingJsonPathStringValue("$.hierarchyDescriptor.@type")
                .isEqualTo("ClassHierarchyDescriptor");
        // Verify that the 'roots' array is present and its first element has the expected IRI.
        assertThat(jsonContent).extractingJsonPathStringValue("$.hierarchyDescriptor.roots[0].iri")
                .isEqualTo("http://www.w3.org/2002/07/owl#Thing");
    }

    @Test
    void shouldSerializeResponseWithNullHierarchyDescriptor() throws Exception {
        var response = new GetHierarchyDescriptorResponse(null);
        var jsonContent = tester.write(response);

        // Verify that the hierarchyDescriptor property is present with a null value.
        assertThat(jsonContent).extractingJsonPathValue("$.hierarchyDescriptor")
                .isNull();
    }

    @Test
    void shouldDeserializeResponseWithNonNullHierarchyDescriptor() throws Exception {
        var json = """
            {
              "hierarchyDescriptor": {
                "@type": "ClassHierarchyDescriptor",
                "roots": [
                  {"@type": "Class", "iri": "http://www.w3.org/2002/07/owl#Thing"}
                ]
              }
            }
            """;
        var response = tester.parseObject(json);
        var hierarchyDescriptorOptional = response.getHierarchyDescriptor();
        assertThat(hierarchyDescriptorOptional).isPresent();
        var hierarchyDescriptor = hierarchyDescriptorOptional.get();
    }

    @Test
    void shouldDeserializeResponseWithNullHierarchyDescriptor() throws Exception {
        var json = """
            {
              "hierarchyDescriptor": null
            }
            """;
        var response = tester.parseObject(json);
        var hierarchyDescriptorOptional = response.getHierarchyDescriptor();
        assertThat(hierarchyDescriptorOptional).isEmpty();
    }
}
