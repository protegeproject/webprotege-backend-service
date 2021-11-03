package edu.stanford.protege.webprotege.persistence;

import edu.stanford.protege.webprotege.issues.CommentId;
import org.springframework.core.convert.converter.Converter;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-11-03
 */
public class CommentId2StringConverter implements Converter<CommentId, String> {

    @Override
    public String convert(CommentId source) {
        return source.value();
    }
}
