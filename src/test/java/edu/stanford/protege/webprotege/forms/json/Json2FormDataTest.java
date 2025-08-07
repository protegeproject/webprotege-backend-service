package edu.stanford.protege.webprotege.forms.json;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.MockingUtils;
import edu.stanford.protege.webprotege.common.Page;
import edu.stanford.protege.webprotege.forms.ExpansionState;
import edu.stanford.protege.webprotege.forms.FormDescriptor;
import edu.stanford.protege.webprotege.forms.FormId;
import edu.stanford.protege.webprotege.forms.data.*;
import edu.stanford.protege.webprotege.forms.field.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class Json2FormDataTest {

    public static final int FIRST = 0;
    public static final int SECOND = 1;
    private OWLDataFactory dataFactory;

    private Json2FormData json2FormData;

    @BeforeEach
    void setUp() {
        dataFactory = new OWLDataFactoryImpl();
        json2FormData = new Json2FormData(
                new Json2FormControlData(
                        dataFactory,
                        new Json2TextControlData(dataFactory),
                        mock(Json2NumberControlData.class),
                        mock(Json2SingleChoiceControlData.class),
                        mock(Json2MultiChoiceControlData.class),
                        mock(Json2EntityNameControlData.class),
                        mock(Json2ImageControlData.class),
                        mock(Json2GridControlData.class),
                        mock(Json2SubFormControlData.class)
                )
        );

    }

    @Test
    public void shouldConvertEmptyFormData() {
        var subject = MockingUtils.mockOWLClass();
        var formData = JsonNodeFactory.instance.objectNode();
        var formId = FormId.generate();
        var data = json2FormData.convert(subject, formData, FormDescriptor.empty(formId));
        assertThat(data).flatMap(FormData::getSubject).map(FormEntitySubject::getEntity).contains(subject);
        assertThat(data).map(FormData::getFormId).contains(formId);
        assertThat(data).map(FormData::getFormFieldData).contains(ImmutableList.of());
    }

    @Test
    public void shouldConvertNonEmptyNonRepeatableFormData() {
        var subject = MockingUtils.mockOWLClass();
        var formData = JsonNodeFactory.instance.objectNode();

        var formId = FormId.generate();
        var textFieldId = FormRegionId.generate();

        formData.set("@id", JsonNodeFactory.instance.textNode(formId.getId()));
        formData.set(textFieldId.value(), JsonNodeFactory.instance.textNode("abc"));

        var descriptor = FormDescriptor.builder(formId)
                .addDescriptor(FormFieldDescriptor.get(
                        textFieldId,
                        null, null, null, null,
                        TextControlDescriptor.getDefault(),
                        Repeatability.NON_REPEATABLE,
                        10,
                        Optionality.OPTIONAL,
                        false,
                        null,
                        null
                )).build();
        var data = json2FormData.convert(subject, formData, descriptor);
        assertThat(data).flatMap(FormData::getSubject).map(FormEntitySubject::getEntity).contains(subject);
        assertThat(data).map(FormData::getFormId).contains(formId);
        assertThat(data).map(FormData::getFormFieldData)
                .map(l -> l.get(FIRST))
                .map(FormFieldData::getFormControlData)
                .map(Page::getPageElements)
                .map(l -> l.get(FIRST))
                .map(TextControlData.class::cast)
                .flatMap(TextControlData::getValue)
                .map(OWLLiteral::getLiteral)
                .contains("abc");
    }

    @Test
    public void shouldConvertNonEmptyRepeatableFormData() {
        var subject = MockingUtils.mockOWLClass();
        var formData = JsonNodeFactory.instance.objectNode();

        var formId = FormId.generate();
        var textFieldId = FormRegionId.generate();

        formData.set("@id", JsonNodeFactory.instance.textNode(formId.getId()));
        formData.set(textFieldId.value(), JsonNodeFactory.instance.arrayNode().add(JsonNodeFactory.instance.textNode("xyz")).add(JsonNodeFactory.instance.textNode("qpr")));

        var descriptor = FormDescriptor.builder(formId)
                .addDescriptor(FormFieldDescriptor.get(
                        textFieldId,
                        null, null, null, null,
                        TextControlDescriptor.getDefault(),
                        Repeatability.REPEATABLE_VERTICALLY,
                        10,
                        Optionality.OPTIONAL,
                        false,
                        null,
                        null
                )).build();
        var data = json2FormData.convert(subject, formData, descriptor);
        assertThat(data).flatMap(FormData::getSubject).map(FormEntitySubject::getEntity).contains(subject);
        assertThat(data).map(FormData::getFormId).contains(formId);
        assertThat(data).map(FormData::getFormFieldData)
                .map(l -> l.get(FIRST))
                .map(FormFieldData::getFormControlData)
                .map(Page::getPageElements)
                .map(l -> l.get(FIRST))
                .map(TextControlData.class::cast)
                .flatMap(TextControlData::getValue)
                .map(OWLLiteral::getLiteral)
                .contains("xyz");

        assertThat(data).map(FormData::getFormFieldData)
                .map(l -> l.get(FIRST))
                .map(FormFieldData::getFormControlData)
                .map(Page::getPageElements)
                .map(l -> l.get(SECOND))
                .map(TextControlData.class::cast)
                .flatMap(TextControlData::getValue)
                .map(OWLLiteral::getLiteral)
                .contains("qpr");
    }

}