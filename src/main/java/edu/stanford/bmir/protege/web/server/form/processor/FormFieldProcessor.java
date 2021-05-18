package edu.stanford.bmir.protege.web.server.form.processor;

import edu.stanford.bmir.protege.web.server.form.FormFrameBuilder;
import edu.stanford.bmir.protege.web.server.form.data.FormFieldData;
import edu.stanford.bmir.protege.web.server.form.FormFieldBindingMissingException;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-26
 */
public class FormFieldProcessor {

    @Nonnull
    private final FormControlDataProcessor formControlDataProcessor;

    @Inject
    public FormFieldProcessor(@Nonnull FormControlDataProcessor formControlDataProcessor) {
        this.formControlDataProcessor = formControlDataProcessor;
    }

    public void processFormFieldData(@Nonnull FormFieldData formFieldData,
                              @Nonnull FormFrameBuilder formFrameBuilder) {
        var formFieldDescriptor = formFieldData.getFormFieldDescriptor();
        var owlBinding = formFieldDescriptor.getOwlBinding();
        var binding = owlBinding.orElseThrow(() -> new FormFieldBindingMissingException(formFieldDescriptor.getId(),
                                                                                        formFieldDescriptor.getLabel()));
        var formControlData = formFieldData.getFormControlData();
        formControlData.forEach(fcd -> formControlDataProcessor.processFormControlData(binding, fcd, formFrameBuilder));
    }


}
