package edu.stanford.protege.webprotege.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.semanticweb.owlapi.model.OWLProperty;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 03/12/2012
 */
public abstract class OWLPropertyData extends OWLEntityData {

    @JsonIgnore
    public abstract boolean isOWLAnnotationProperty();

    @Override
    public OWLProperty getEntity() {
        return (OWLProperty) super.getEntity();
    }
}
