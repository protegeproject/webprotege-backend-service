package edu.stanford.protege.webprotege.icd;

import static edu.stanford.protege.webprotege.icd.IcdConstants.NS;

public enum IcdProperties {
    LABEL_PROP(NS + "label"),
    TITLE_PROP(NS + "title");
    private final String value;

    IcdProperties(String s) {
        this.value = s;
    }

    public String getValue(){
        return this.value;
    }
}
