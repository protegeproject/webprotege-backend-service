package edu.stanford.protege.webprotege.app;

import javax.json.JsonObject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23/12/14
 */
public interface ClientObjectEncoder<T> {

    JsonObject encode(T object);
}
