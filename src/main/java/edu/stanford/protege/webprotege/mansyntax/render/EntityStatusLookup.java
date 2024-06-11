package edu.stanford.protege.webprotege.mansyntax.render;

import org.semanticweb.owlapi.model.OWLEntity;

import java.util.Set;

public interface EntityStatusLookup {

    Set<EntityStatus> getEntityStatus(OWLEntity entity);
}
