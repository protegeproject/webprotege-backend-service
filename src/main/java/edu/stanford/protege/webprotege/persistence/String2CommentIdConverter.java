package edu.stanford.protege.webprotege.persistence;

import edu.stanford.protege.webprotege.issues.CommentId;
import org.springframework.core.convert.converter.Converter;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-11-03
 */
public class String2CommentIdConverter implements Converter<String, CommentId> {

    @Override
    public CommentId convert(String source) {
        return CommentId.valueOf(source);
    }
}
