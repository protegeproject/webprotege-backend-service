package edu.stanford.protege.webprotege.forms.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.stanford.protege.webprotege.forms.data.FormControlData;
import edu.stanford.protege.webprotege.forms.data.FormData;
import edu.stanford.protege.webprotege.forms.data.FormEntitySubject;
import org.semanticweb.owlapi.model.IRI;

import jakarta.annotation.Nonnull;
import jakarta.inject.Provider;

public class FormDataJsonConverter {


    public static final String IRI_KEY = "@id";

    public static final String JSON_LANG = "json";

    private final JsonNodeFactory nodeFactory = JsonNodeFactory.instance;

    private final Provider<FormControlDataConverter> formControlDataConverterProvider;

    public FormDataJsonConverter(Provider<FormControlDataConverter> formControlDataConverterProvider) {
        this.formControlDataConverterProvider = formControlDataConverterProvider;
    }

    @Nonnull
    public ObjectNode convert(@Nonnull FormData formData) {
        var fields = formData.getFormFieldData();
        var obj = nodeFactory.objectNode();
        var subjNode = formData.getSubject().map(FormEntitySubject::getIri).map(IRI::toString).map(i -> (JsonNode) nodeFactory.textNode(i)).orElse(nodeFactory.nullNode());
        obj.set(IRI_KEY, subjNode);
        fields.forEach(field -> {
            var formFieldDescriptor = field.getFormFieldDescriptor();
            var key = formFieldDescriptor.getLabel().asMap().getOrDefault(JSON_LANG, formFieldDescriptor.getId().value());
            if (formFieldDescriptor.getRepeatability().isRepeatable()) {
                var val = nodeFactory.arrayNode();
                field.getFormControlData()
                        .getPageElements()
                        .stream()
                        .map(this::convertFormControlData)
                        .forEach(val::add);
                obj.set(key, val);
            } else {
                field.getFormControlData()
                        .getPageElements()
                        .stream()
                        .map(this::convertFormControlData)
                        .findFirst()
                        .ifPresent(v -> obj.set(key, v));
            }

        });
        return obj;
    }

    private JsonNode convertFormControlData(FormControlData formControlData) {
        var converter = formControlDataConverterProvider.get();
        return converter.convert(formControlData);
    }


}
