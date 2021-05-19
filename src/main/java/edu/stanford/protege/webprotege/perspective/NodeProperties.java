package edu.stanford.protege.webprotege.perspective;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.base.MoreObjects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-20
 */
public class NodeProperties {

    private static final NodeProperties EMPTY_NODE_PROPERTIES = new NodeProperties(Collections.emptyMap());

    private final Map<String, NodePropertyValue> properties = new HashMap<>();

    @JsonCreator
    private NodeProperties(@Nullable Map<String, NodePropertyValue> properties) {
        if (properties != null) {
            this.properties.putAll(properties);
        }
    }


    public static Builder builder() {
        return new Builder();
    }

    public static NodeProperties emptyNodeProperties() {
        return EMPTY_NODE_PROPERTIES;
    }


    public String getPropertyValue(String propertyName, String defaultValue) {
        NodePropertyValue value = properties.get(propertyName);
        if(value instanceof StringNodePropertyValue) {
            return ((StringNodePropertyValue) value).getValue();
        }
        else {
            return defaultValue;
        }
    }

    public NodePropertyValue getPropertyValue(String propertyName, NodePropertyValue defaultValue) {
        NodePropertyValue value = properties.get(propertyName);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    @JsonValue
    public NodePropertyValueMap getPropertiesAsMap() {
        return new NodePropertyValueMap(properties);
    }

    @JsonIgnore
    @Nonnull
    public List<String> getProperties() {
        return new ArrayList<>(properties.keySet());
    }

    @Nonnull
    public Builder toBuilder() {
        return new Builder(properties);
    }

    public static class Builder {

        @Nonnull
        private final Map<String, NodePropertyValue> propertiesMap;

        public Builder() {
            this(new LinkedHashMap<>());
        }

        public Builder(@Nonnull Map<String, NodePropertyValue> propertiesMap) {
            this.propertiesMap = propertiesMap;
        }

        @Nonnull
        public Builder setValue(String property, NodePropertyValue value) {
            propertiesMap.put(property, value);
            return this;
        }

        @Nonnull
        public Builder setValue(String property, String value) {
            setValue(property, StringNodePropertyValue.get(value));
            return this;
        }

        public NodeProperties build() {
            if(propertiesMap.isEmpty()) {
                return EMPTY_NODE_PROPERTIES;
            }
            else {
                return new NodeProperties(new LinkedHashMap<>(propertiesMap));
            }
        }
    }

    @Override
    public String toString() {
        MoreObjects.ToStringHelper helper = MoreObjects.toStringHelper("NodeProperties");
        for(String property : properties.keySet()) {
            helper.add(property, properties.get(property));
        }
        return helper.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof NodeProperties)) {
            return false;
        }
        NodeProperties other = (NodeProperties) obj;
        return this.properties.equals(other.properties);
    }
}
