package edu.stanford.protege.webprotege.forms;



import edu.stanford.protege.webprotege.forms.field.FormFieldId;
import edu.stanford.protege.webprotege.common.LanguageMap;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-26
 */
public class FormFieldBindingMissingException extends RuntimeException {

    private FormFieldId formFieldId;

    public FormFieldBindingMissingException(@Nonnull FormFieldId formFieldId,
                                            @Nonnull LanguageMap label) {
        super("Form field binding is missing for " + formFieldId + "(" + label + ").  Improperly configured form.");
        this.formFieldId = checkNotNull(formFieldId);
    }


    private FormFieldBindingMissingException() {
    }

    public FormFieldId getFormFieldId() {
        return formFieldId;
    }
}
