package edu.stanford.protege.webprotege.forms.json;

import com.fasterxml.jackson.databind.JsonNode;
import edu.stanford.protege.webprotege.forms.data.FormData;
import edu.stanford.protege.webprotege.forms.field.SubFormControlDescriptor;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;

import javax.inject.Provider;
import java.util.Optional;

public class Json2SubFormControlData {

    private final Provider<Json2FormData> json2FormDataProvider;

    private final OWLDataFactory dataFactory;

    public Json2SubFormControlData(Provider<Json2FormData> json2FormDataProvider, OWLDataFactory dataFactory) {
        this.json2FormDataProvider = json2FormDataProvider;
        this.dataFactory = dataFactory;
    }

    public Optional<FormData> convert(JsonNode jsonFieldData, SubFormControlDescriptor subFormControlDescriptor) {
        var json2FormData = json2FormDataProvider.get();
        var iriString = jsonFieldData.get("@id").textValue();
        var subject = subFormControlDescriptor.getFormDescriptor().getSubjectFactoryDescriptor()
                .map(sf -> dataFactory.getOWLEntity(sf.getEntityType(), IRI.create(iriString)))
                .orElse(null);
        return json2FormData.convert(subject, jsonFieldData, subFormControlDescriptor.getFormDescriptor());
    }
}
