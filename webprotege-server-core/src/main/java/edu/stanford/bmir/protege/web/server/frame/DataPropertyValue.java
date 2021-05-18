package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.server.entity.OWLDataPropertyData;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/11/2012
 */
public abstract class DataPropertyValue extends PropertyValue {

    @Override
    public abstract OWLDataPropertyData getProperty();
}
