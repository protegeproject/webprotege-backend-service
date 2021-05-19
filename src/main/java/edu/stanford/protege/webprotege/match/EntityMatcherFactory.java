package edu.stanford.protege.webprotege.match;

import edu.stanford.protege.webprotege.match.criteria.EntityMatchCriteria;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-15
 */
public interface EntityMatcherFactory {

    @Nonnull
    Matcher<OWLEntity> getEntityMatcher(@Nonnull EntityMatchCriteria criteria);
}
