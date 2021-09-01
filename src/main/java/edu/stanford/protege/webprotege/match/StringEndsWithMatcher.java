package edu.stanford.protege.webprotege.match;

import edu.stanford.protege.webprotege.criteria.StringEndsWithCriteria;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-06-23
 */
public class StringEndsWithMatcher implements Matcher<String> {

    @Nonnull
    private final StringEndsWithCriteria criteria;

    public StringEndsWithMatcher(@Nonnull StringEndsWithCriteria criteria) {
        this.criteria = checkNotNull(criteria);
    }

    @Override
    public boolean matches(@Nonnull String value) {
        if(criteria.isIgnoreCase()) {
            return StringUtils.endsWithIgnoreCase(value, criteria.getValue());
        }
        else {
            return value.endsWith(criteria.getValue());
        }
    }
}



