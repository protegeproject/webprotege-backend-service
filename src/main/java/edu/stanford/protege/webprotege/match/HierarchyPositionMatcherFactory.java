package edu.stanford.protege.webprotege.match;

import edu.stanford.protege.webprotege.criteria.CompositeHierarchyPositionCriteria;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-08
 */
public interface HierarchyPositionMatcherFactory {

    @Nonnull
    Matcher<OWLEntity> getHierarchyPositionMatcher(@Nonnull CompositeHierarchyPositionCriteria criteria);
}
