package edu.stanford.protege.webprotege.card;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.portlet.PortletId;

@JsonTypeName("CustomContent")
public record CustomContentEntityCardContentDescriptor(@JsonProperty("customContentId") CustomContentId customContentId) implements CardContentDescriptor {

    public static CustomContentEntityCardContentDescriptor create(CustomContentId customContentId) {
        return new CustomContentEntityCardContentDescriptor(customContentId);
    }
}
