package edu.stanford.protege.webprotege.persistence;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2 Oct 2016
 */
public interface Converter<S, T> {

    T convert(S source);
}
