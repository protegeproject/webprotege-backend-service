package edu.stanford.protege.webprotege.persistence;

import edu.stanford.protege.webprotege.issues.ThreadId;
import org.springframework.core.convert.converter.Converter;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-11-03
 */
public class ThreadId2StringConverter implements Converter<ThreadId, String> {

    @Override
    public String convert(ThreadId source) {
        return source.value();
    }
}
