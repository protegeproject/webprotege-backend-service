package edu.stanford.protege.webprotege.persistence;

import org.springframework.core.convert.converter.Converter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-07-09
 */
public class String2ValueObjectConverter<V> implements Converter<String, V> {

    public static final String VALUE_OF = "valueOf";

    private final Class<V> cls;

    private Method valueOfMethod;

    protected String2ValueObjectConverter(Class<V> cls) {
        this.cls = cls;
    }


    @Override
    public V convert(String s) {
        try {
            if(valueOfMethod == null) {
                try {
                    valueOfMethod = cls.getMethod(VALUE_OF, String.class);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
            return (V) valueOfMethod.invoke(cls, s);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

    }
}
