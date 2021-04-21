package edu.stanford.bmir.protege.web.shared.perspective;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-20
 */
@JsonSubTypes(
        @JsonSubTypes.Type(value = StringNodePropertyValue.class)
)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
public interface NodePropertyValue {

}
