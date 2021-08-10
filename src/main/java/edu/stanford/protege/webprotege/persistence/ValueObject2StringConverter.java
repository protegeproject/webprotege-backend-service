package edu.stanford.protege.webprotege.persistence;

import edu.stanford.protege.webprotege.common.ValueObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-07-09
 */
public class ValueObject2StringConverter<V> implements Converter<V, String> {

    public static final String VALUE_METHOD_NAME = "value";

    private final Logger logger = LoggerFactory.getLogger(ValueObject2StringConverter.class);

    private final Class<V> cls;

    private Method method;

    public ValueObject2StringConverter(Class<V> cls) {
        this.cls = cls;
    }

    public static <V extends ValueObject> Converter<V, String> get(Class<V> forClass) {
        return new ValueObject2StringConverter<>(forClass);
    }

    @Override
    public String convert(V source) {
        if(method == null) {
            try {
                method = cls.getMethod(VALUE_METHOD_NAME);
            } catch (NoSuchMethodException e) {
                logger.error("Missing value method", e);
            }
        }
        try {
            return (String) method.invoke(source);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
