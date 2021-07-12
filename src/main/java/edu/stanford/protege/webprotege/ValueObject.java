package edu.stanford.protege.webprotege;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-07-09
 */
public interface ValueObject {

    @JsonIgnore
    String value();
}
