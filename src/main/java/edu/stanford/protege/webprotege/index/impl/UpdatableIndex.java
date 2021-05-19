package edu.stanford.protege.webprotege.index.impl;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.change.OntologyChange;
import edu.stanford.protege.webprotege.index.Index;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-09
 *
 * An index that needs to be updated with ontology changes in order to
 * build or maintain it.
 */
public interface UpdatableIndex extends Index {

    /**
     * Apply the specified list of changes to update this index.
     */
    void applyChanges(@Nonnull ImmutableList<OntologyChange> changes);
}
