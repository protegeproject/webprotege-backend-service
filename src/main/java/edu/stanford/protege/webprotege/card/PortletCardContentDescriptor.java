package edu.stanford.protege.webprotege.card;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.portlet.PortletId;

@JsonTypeName("PortletCardContentDescriptor")
public record PortletCardContentDescriptor(@JsonProperty("portletId") PortletId portletId) implements CardContentDescriptor {

    public static PortletCardContentDescriptor create(PortletId portletId) {
        return new PortletCardContentDescriptor(portletId);
    }
}
