package edu.stanford.protege.webprotege.match;

import edu.stanford.protege.webprotege.criteria.StringContainsCriteria;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-06-23
 */
public class StringContainsMatcher implements Matcher<String> {

    @Nonnull
    private final StringContainsCriteria criteria;

    public StringContainsMatcher(@Nonnull StringContainsCriteria criteria) {
        this.criteria = checkNotNull(criteria);
    }

    @Override
    public boolean matches(@Nonnull String value) {
        if(criteria.isIgnoreCase()) {
            return StringUtils.containsIgnoreCase(value, criteria.getValue());
        }
        else {
            return value.contains(criteria.getValue());
        }
    }
}
