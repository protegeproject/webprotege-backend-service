package edu.stanford.protege.webprotege.entity;

import com.google.common.collect.ImmutableSet;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/03/2013
 */
public interface CreateEntitiesInHierarchyAction<R extends CreateEntitiesInHierarchyResult<E>, E extends OWLEntity> extends AbstractCreateEntitiesAction<R, E> {

    @Nonnull
    ImmutableSet<E> getParents();
}
