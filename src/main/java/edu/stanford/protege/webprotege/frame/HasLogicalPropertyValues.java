package edu.stanford.protege.webprotege.frame;

import com.google.common.collect.ImmutableList;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/12/2012
 */
public interface HasLogicalPropertyValues {

    ImmutableList<PropertyValue> getLogicalPropertyValues();
}
