package edu.stanford.protege.webprotege.forms.field;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Jun 2017
 */
public enum NodeType {

    @JsonProperty("Any") ANY,

    @JsonProperty("Leaf") LEAF
}
