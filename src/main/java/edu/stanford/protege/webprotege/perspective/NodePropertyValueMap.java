package edu.stanford.protege.webprotege.perspective;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-21
 */
public class NodePropertyValueMap implements Map<String, NodePropertyValue> {

    private final Map<String, NodePropertyValue> delegate = new LinkedHashMap<>();

    public NodePropertyValueMap() {
    }

    public NodePropertyValueMap(Map<? extends String, ? extends NodePropertyValue> m) {
        delegate.putAll(m);
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @JsonIgnore
    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return delegate.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return delegate.containsValue(value);
    }

    @Override
    public NodePropertyValue get(Object key) {
        return delegate.get(key);
    }

    @Override
    public NodePropertyValue put(String key, NodePropertyValue value) {
        return delegate.put(key, value);
    }

    @Override
    public NodePropertyValue remove(Object key) {
        return delegate.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends NodePropertyValue> m) {
        delegate.putAll(m);
    }

    @Override
    public void clear() {
        delegate.clear();
    }

    @Override
    public Set<String> keySet() {
        return delegate.keySet();
    }

    @Override
    public Collection<NodePropertyValue> values() {
        return delegate.values();
    }

    @Override
    public Set<Entry<String, NodePropertyValue>> entrySet() {
        return delegate.entrySet();
    }
}
