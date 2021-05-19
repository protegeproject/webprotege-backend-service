package edu.stanford.protege.webprotege.match;

import edu.stanford.protege.webprotege.match.criteria.HierarchyPositionCriteria;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-10-07
 */
public interface HierarchyPositionMatchingEngine {

    @Nonnull
    Stream<OWLEntity> getMatchingEntities(@Nonnull HierarchyPositionCriteria criteria);
}
