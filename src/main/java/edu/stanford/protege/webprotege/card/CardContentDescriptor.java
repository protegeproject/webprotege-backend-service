package edu.stanford.protege.webprotege.card;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonSubTypes({
        @JsonSubTypes.Type(FormCardContentDescriptor.class),
        @JsonSubTypes.Type(PortletCardContentDescriptor.class)
})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
public interface CardContentDescriptor {
}
