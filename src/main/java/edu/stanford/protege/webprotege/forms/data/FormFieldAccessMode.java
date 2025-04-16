package edu.stanford.protege.webprotege.forms.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum FormFieldAccessMode {

    @JsonProperty("ReadOnly")
    READ_ONLY,

    @JsonProperty("ReadWrite")
    READ_WRITE;
}
