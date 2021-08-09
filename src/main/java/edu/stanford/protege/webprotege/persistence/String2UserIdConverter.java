package edu.stanford.protege.webprotege.persistence;

import edu.stanford.protege.webprotege.common.UserId;
import org.springframework.core.convert.converter.Converter;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-07-09
 */
public class String2UserIdConverter implements Converter<String, UserId> {

    @Override
    public UserId convert(String source) {
        return UserId.valueOf(source);
    }
}
