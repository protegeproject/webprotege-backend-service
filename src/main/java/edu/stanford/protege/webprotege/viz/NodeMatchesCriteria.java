package edu.stanford.protege.webprotege.viz;

import edu.stanford.protege.webprotege.match.criteria.EntityMatchCriteria;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-06
 *
 * A node that matches some entity matching criteria
 */
public interface NodeMatchesCriteria extends EdgeNodeCriteria  {

    @Nonnull
    EntityMatchCriteria getNodeCriteria();
}
