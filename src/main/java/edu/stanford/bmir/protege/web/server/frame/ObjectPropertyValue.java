package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.server.entity.OWLObjectPropertyData;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/11/2012
 */
public abstract class ObjectPropertyValue extends PropertyValue {

    @Override
    public abstract OWLObjectPropertyData getProperty();
}
