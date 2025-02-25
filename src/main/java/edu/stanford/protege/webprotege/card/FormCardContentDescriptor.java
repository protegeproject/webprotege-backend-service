package edu.stanford.protege.webprotege.card;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.forms.FormId;

@JsonTypeName("FormContent")
public record FormCardContentDescriptor(@JsonProperty("formId") FormId formId) implements CardContentDescriptor {

    public static FormCardContentDescriptor create(FormId formId) {
        return new FormCardContentDescriptor(formId);
    }
}
