package edu.stanford.protege.webprotege.forms.processor;

import edu.stanford.protege.webprotege.forms.FormFieldBindingMissingException;
import edu.stanford.protege.webprotege.forms.FormFrameBuilder;
import edu.stanford.protege.webprotege.forms.data.FormFieldData;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;

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
