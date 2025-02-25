package edu.stanford.protege.webprotege.ui;

import edu.stanford.protege.webprotege.MockingUtils;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.forms.FormId;
import edu.stanford.protege.webprotege.forms.field.FormRegionId;
import edu.stanford.protege.webprotege.jackson.WebProtegeJacksonApplication;
import edu.stanford.protege.webprotege.perspective.PerspectiveId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi.model.OWLClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.semanticweb.owlapi.model.OWLEntity;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JsonTest(properties = "webprotege.rabbitmq.event-subscribe=false")
@Import(WebProtegeJacksonApplication.class)
@AutoConfigureJsonTesters
class DisplayContextTest {

    @Autowired
    private  JacksonTester<DisplayContext> jsonTester;

    private ProjectId projectId;

    private PerspectiveId perspectiveId;

    private ViewId viewId;

    private ViewNodeId viewNodeId;

    private Map<String, String> viewProperties;

    private List<FormId> formIds;

    private FormRegionId formFieldId;

    private List<List<OWLEntity>> selectedPaths;

    private OWLClass owlClass;

    @BeforeEach
    void setUp() {

        projectId = ProjectId.generate();
        perspectiveId = PerspectiveId.generate();
        viewId = ViewId.generate();
        viewNodeId = ViewNodeId.generate();
        viewProperties = Map.of("key", "value");
        formIds = List.of(FormId.generate());
        formFieldId = FormRegionId.generate();
        owlClass = MockingUtils.mockOWLClass();
        selectedPaths = List.of(List.of(owlClass));
    }

    @Test
    void shouldThrowNullPointerExceptionForNullArguments() {
        assertThatThrownBy(() -> new DisplayContext(null, perspectiveId, viewId, viewNodeId, viewProperties, formIds, formFieldId, selectedPaths))
                .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> new DisplayContext(projectId, null, viewId, viewNodeId, viewProperties, formIds, formFieldId, selectedPaths))
                .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> new DisplayContext(projectId, perspectiveId, null, viewNodeId, viewProperties, formIds, formFieldId, selectedPaths))
                .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> new DisplayContext(projectId, perspectiveId, viewId, null, viewProperties, formIds, formFieldId, selectedPaths))
                .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> new DisplayContext(projectId, perspectiveId, viewId, viewNodeId, null, formIds, formFieldId, selectedPaths))
                .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> new DisplayContext(projectId, perspectiveId, viewId, viewNodeId, viewProperties, formIds, formFieldId, null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldSerializeToJson() throws Exception {
        var displayContext = new DisplayContext(projectId, perspectiveId, viewId, viewNodeId, viewProperties, formIds, formFieldId, selectedPaths);

        var jsonContent = jsonTester.write(displayContext);

        assertThat(jsonContent).hasJsonPathStringValue("$.projectId", projectId.value());
        assertThat(jsonContent).hasJsonPathStringValue("$.perspectiveId", perspectiveId.getId());
        assertThat(jsonContent).hasJsonPathStringValue("$.viewId", viewId.value());
        assertThat(jsonContent).hasJsonPathStringValue("$.viewNodeId", viewNodeId.value());
        assertThat(jsonContent).hasJsonPathStringValue("$.viewProperties.key", "value");
        assertThat(jsonContent).hasJsonPathStringValue("$.formIds[0]", formIds.get(0).getId());
        assertThat(jsonContent).hasJsonPathStringValue("$.formFieldId", formFieldId.value());
        assertThat(jsonContent).hasJsonPathStringValue("$.selectedPaths[0][0].iri", selectedPaths.get(0).get(0).getIRI().toString());
    }

    @Test
    void shouldDeserializeFromJson() throws Exception {
        var json = """
                {
                    "projectId": "%s",
                    "perspectiveId": "%s",
                    "viewId": "%s",
                    "viewNodeId": "%s",
                    "viewProperties": {"key": "value"},
                    "formIds": ["%s"],
                    "formFieldId": "%s",
                    "selectedPaths": [[{"@type" : "Class", "iri":"http://example.org/Class1"}]]
                }
                """.formatted(
                projectId.value(),
                perspectiveId.getId(),
                viewId.value(),
                viewNodeId.value(),
                formIds.get(0).value(),
                formFieldId.value()
        );

        var deserialized = jsonTester.parseObject(json);

        assertThat(deserialized.projectId()).isEqualTo(projectId);
        assertThat(deserialized.perspectiveId()).isEqualTo(perspectiveId);
        assertThat(deserialized.viewId()).isEqualTo(viewId);
        assertThat(deserialized.viewNodeId()).isEqualTo(viewNodeId);
        assertThat(deserialized.viewProperties()).isEqualTo(viewProperties);
        assertThat(deserialized.formIds()).isEqualTo(formIds);
        assertThat(deserialized.formFieldId()).isEqualTo(formFieldId);
        assertThat(deserialized.selectedPaths().get(0).get(0).getIRI().toString()).isEqualTo("http://example.org/Class1");
    }
}
