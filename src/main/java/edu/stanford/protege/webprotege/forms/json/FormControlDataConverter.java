package edu.stanford.protege.webprotege.forms.json;

import com.fasterxml.jackson.databind.JsonNode;
import edu.stanford.protege.webprotege.forms.data.*;
import org.jetbrains.annotations.NotNull;

public class FormControlDataConverter {

    private final EntityNameControlDataConverter entityNameControlDataConverter;

    private final GridControlDataConverter gridControlDataConverter;

    private final ImageControlDataConverter imageControlDataConverter;

    private final NumberControlDataConverter numberControlDataConverter;

    private final TextControlDataConverter textControlDataConverter;

    private final MultiChoiceControlDataConverter multiChoiceControlDataConverter;

    private final SingleChoiceControlDataConverter singleChoiceControlDataConverter;

    private final FormDataJsonConverter formDataJsonConverter;

    public FormControlDataConverter(EntityNameControlDataConverter entityNameControlDataConverter, GridControlDataConverter gridControlDataConverter, ImageControlDataConverter imageControlDataConverter, NumberControlDataConverter numberControlDataConverter, TextControlDataConverter textControlDataConverter, MultiChoiceControlDataConverter multiChoiceControlDataConverter, SingleChoiceControlDataConverter singleChoiceControlDataConverter, FormDataJsonConverter formDataJsonConverter) {
        this.entityNameControlDataConverter = entityNameControlDataConverter;
        this.gridControlDataConverter = gridControlDataConverter;
        this.imageControlDataConverter = imageControlDataConverter;
        this.numberControlDataConverter = numberControlDataConverter;
        this.textControlDataConverter = textControlDataConverter;
        this.multiChoiceControlDataConverter = multiChoiceControlDataConverter;
        this.singleChoiceControlDataConverter = singleChoiceControlDataConverter;
        this.formDataJsonConverter = formDataJsonConverter;
    }


    /**
     * Extracts a JSON representation of the data held in a form.
     *
     * @param data The form data to convert to JSON
     * @return The JSON representation of the form data
     */
    public JsonNode convert(FormControlData data) {
        return data.accept(new FormControlDataVisitorEx<JsonNode>() {
            @Override
            public JsonNode visit(@NotNull EntityNameControlData entityNameControlData) {
                return entityNameControlDataConverter.convert(entityNameControlData);
            }

            @Override
            public JsonNode visit(@NotNull FormData formData) {
                return formDataJsonConverter.convert(formData);
            }

            @Override
            public JsonNode visit(@NotNull GridControlData gridControlData) {
                return gridControlDataConverter.convert(gridControlData);
            }

            @Override
            public JsonNode visit(@NotNull ImageControlData imageControlData) {
                return imageControlDataConverter.convert(imageControlData);
            }

            @Override
            public JsonNode visit(@NotNull MultiChoiceControlData multiChoiceControlData) {
                return multiChoiceControlDataConverter.convert(multiChoiceControlData);
            }

            @Override
            public JsonNode visit(@NotNull SingleChoiceControlData singleChoiceControlData) {
                return singleChoiceControlDataConverter.convert(singleChoiceControlData);
            }

            @Override
            public JsonNode visit(@NotNull NumberControlData numberControlData) {
                return numberControlDataConverter.convert(numberControlData);
            }

            @Override
            public JsonNode visit(@NotNull TextControlData textControlData) {
                return textControlDataConverter.convert(textControlData);
            }
        });
    }
}
