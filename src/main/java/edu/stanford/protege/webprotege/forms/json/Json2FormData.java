package edu.stanford.protege.webprotege.forms.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.common.Page;
import edu.stanford.protege.webprotege.forms.FormDescriptor;
import edu.stanford.protege.webprotege.forms.data.FormControlData;
import edu.stanford.protege.webprotege.forms.data.FormData;
import edu.stanford.protege.webprotege.forms.data.FormEntitySubject;
import edu.stanford.protege.webprotege.forms.data.FormFieldData;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.ArrayList;
import java.util.Optional;

public class Json2FormData {

    private static final String JSON_LANG = "json";

    private final Json2FormControlData json2FormControlData;

    public Json2FormData(Json2FormControlData json2FormControlData) {
        this.json2FormControlData = json2FormControlData;
    }

    public Optional<FormData> convert(OWLEntity subject,
                            JsonNode jsonFormData,
                            FormDescriptor formDescriptor) {
        // Fields
        if(!jsonFormData.isObject()) {
            FormData.empty(subject, formDescriptor.getFormId());
        }
        var jsonObj = (ObjectNode) jsonFormData;
        var fields = formDescriptor.getFields();
        var formFieldDataList = new ImmutableList.Builder<FormFieldData>();
        fields.forEach(fieldDescriptor -> {
            var key = fieldDescriptor.getLabel().asMap().getOrDefault(JSON_LANG, fieldDescriptor.getId().value());
            var fieldValue = jsonObj.get(key);
            // To Form Control Data
            var controlDescriptor = fieldDescriptor.getFormControlDescriptor();
            if(fieldValue.isArray()) {
                // Multiple values in resulting data array
                var fieldArray = (ArrayNode) fieldValue;
                var dataList = new ArrayList<FormControlData>();
                fieldArray.forEach(element -> {
                    var elementData = json2FormControlData.convert(element, controlDescriptor);
                    elementData.ifPresent(dataList::add);
                });
                var formControlDataPage = Page.of(ImmutableList.copyOf(dataList));
                var formFieldData = FormFieldData.get(fieldDescriptor, formControlDataPage);
                formFieldDataList.add(formFieldData);
            }
            else {
                // Single value in resulting data array
                var fieldData = json2FormControlData.convert(fieldValue, controlDescriptor);
                fieldData.map(d -> FormFieldData.get(fieldDescriptor, Page.of(ImmutableList.of(d)))).ifPresent(formFieldDataList::add);
            }
        });
        return Optional.of(FormData.get(Optional.of(FormEntitySubject.get(subject)),
                formDescriptor,
                formFieldDataList.build()));
    }


}
