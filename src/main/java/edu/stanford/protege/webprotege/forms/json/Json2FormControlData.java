package edu.stanford.protege.webprotege.forms.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import edu.stanford.protege.webprotege.forms.data.FormControlData;
import edu.stanford.protege.webprotege.forms.field.*;
import org.semanticweb.owlapi.model.OWLDataFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Json2FormControlData {

    private final OWLDataFactory dataFactory;

    private final Json2TextControlData json2TextControlData;

    private final Json2NumberControlData json2NumberControlData;

    private final Json2SingleChoiceControlData json2SingleChoiceControlData;

    private final Json2MultiChoiceControlData json2MultiChoiceControlData;

    private final Json2EntityNameControlData json2EntityNameControlData;

    private final Json2ImageControlData json2ImageControlData;

    private final Json2GridControlData json2GridControlData;

    private final Json2SubFormControlData json2SubFormControlData;

    public Json2FormControlData(OWLDataFactory dataFactory, Json2TextControlData json2TextControlData, Json2NumberControlData json2NumberControlData, Json2SingleChoiceControlData json2SingleChoiceControlData, Json2MultiChoiceControlData json2MultiChoiceControlData, Json2EntityNameControlData json2EntityNameControlData, Json2ImageControlData json2ImageControlData, Json2GridControlData json2GridControlData, Json2SubFormControlData json2SubFormControlData) {
        this.dataFactory = dataFactory;
        this.json2TextControlData = json2TextControlData;
        this.json2NumberControlData = json2NumberControlData;
        this.json2SingleChoiceControlData = json2SingleChoiceControlData;
        this.json2MultiChoiceControlData = json2MultiChoiceControlData;
        this.json2EntityNameControlData = json2EntityNameControlData;
        this.json2ImageControlData = json2ImageControlData;
        this.json2GridControlData = json2GridControlData;
        this.json2SubFormControlData = json2SubFormControlData;
    }


    public List<? extends FormControlData> convertFromArray(ArrayNode jsonFieldData, FormControlDescriptor formControlDescriptor) {
        return formControlDescriptor.accept(new FormControlDescriptorVisitor<List<? extends FormControlData>>() {
            @Override
            public List<? extends FormControlData> visit(TextControlDescriptor textControlDescriptor) {
                return new ArrayList<>();
            }

            @Override
            public List<? extends FormControlData> visit(NumberControlDescriptor numberControlDescriptor) {
                return new ArrayList<>();
            }

            @Override
            public List<? extends FormControlData> visit(SingleChoiceControlDescriptor singleChoiceControlDescriptor) {
                return new ArrayList<>();
            }

            @Override
            public List<? extends FormControlData> visit(MultiChoiceControlDescriptor multiChoiceControlDescriptor) {
                return json2MultiChoiceControlData.convert(jsonFieldData, multiChoiceControlDescriptor);
            }

            @Override
            public List<? extends FormControlData> visit(EntityNameControlDescriptor entityNameControlDescriptor) {
                return json2EntityNameControlData.convertAsList(jsonFieldData, entityNameControlDescriptor);
            }

            @Override
            public List<? extends FormControlData> visit(ImageControlDescriptor imageControlDescriptor) {
                return new ArrayList<>();
            }

            @Override
            public List<? extends FormControlData> visit(GridControlDescriptor gridControlDescriptor) {
                return json2GridControlData.convert(jsonFieldData, gridControlDescriptor);
            }

            @Override
            public List<? extends FormControlData> visit(SubFormControlDescriptor subFormControlDescriptor) {
                return new ArrayList<>();
            }
        });
    }

    public Optional<? extends FormControlData> convert(JsonNode jsonFieldData,
                                                                   FormControlDescriptor formControlDescriptor) {
        return formControlDescriptor.accept(new FormControlDescriptorVisitor<>() {
            @Override
            public Optional<? extends FormControlData> visit(TextControlDescriptor textControlDescriptor) {
                return json2TextControlData.convert(jsonFieldData, textControlDescriptor);
            }

            @Override
            public Optional<? extends FormControlData> visit(NumberControlDescriptor numberControlDescriptor) {
                return json2NumberControlData.convert(jsonFieldData, numberControlDescriptor);
            }

            @Override
            public Optional<? extends FormControlData> visit(SingleChoiceControlDescriptor singleChoiceControlDescriptor) {
                return json2SingleChoiceControlData.convert(jsonFieldData, singleChoiceControlDescriptor);
            }

            @Override
            public Optional<? extends FormControlData> visit(MultiChoiceControlDescriptor multiChoiceControlDescriptor) {
                return Optional.empty();
            }

            @Override
            public Optional<? extends FormControlData> visit(EntityNameControlDescriptor entityNameControlDescriptor) {
                return json2EntityNameControlData.convert(jsonFieldData, entityNameControlDescriptor);
            }

            @Override
            public Optional<? extends FormControlData> visit(ImageControlDescriptor imageControlDescriptor) {
                return json2ImageControlData.convert(jsonFieldData, imageControlDescriptor);
            }

            @Override
            public Optional<? extends FormControlData> visit(GridControlDescriptor gridControlDescriptor) {
                return Optional.empty();
            }

            @Override
            public Optional<? extends FormControlData> visit(SubFormControlDescriptor subFormControlDescriptor) {
                return json2SubFormControlData.convert(jsonFieldData, subFormControlDescriptor);
            }
        });
    }
}
