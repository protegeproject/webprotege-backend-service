package edu.stanford.protege.webprotege;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 02/12/2012
 * <p>
 *     A marker interface for objects that have a signature
 * </p>
 */
public interface HasSignature {

    /**
     * Gets the signature of the object that implements this interface.
     * @return A set of entities that represent the signature of this object
     */
    @JsonIgnore
    Set<OWLEntity> getSignature();
}
