package edu.stanford.protege.webprotege.owlapi;

import edu.stanford.protege.webprotege.change.OntologyChange;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/04/2012
 */
public class OWLEntityCreator<E extends OWLEntity> {

    private final E entity;

    private final List<OntologyChange> changes = new ArrayList<>();

    public OWLEntityCreator(E entity, List<OntologyChange> changes) {
        this.entity = entity;
        this.changes.addAll(changes);
    }


    public E getEntity() {
        return entity;
    }

    public List<OntologyChange> getChanges() {
        return new ArrayList<>(changes);
    }
}
