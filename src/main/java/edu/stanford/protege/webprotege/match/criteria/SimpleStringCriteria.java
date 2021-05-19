package edu.stanford.protege.webprotege.match.criteria;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Jun 2018
 */
public interface SimpleStringCriteria extends LexicalValueCriteria {

    String IGNORE_CASE = "ignoreCase";

    String VALUE = "value";

    String getValue();

    boolean isIgnoreCase();
}
