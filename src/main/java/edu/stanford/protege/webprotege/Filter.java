package edu.stanford.protege.webprotege;



/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/01/2012
 */
public interface Filter<T> {

    boolean isIncluded(T object);
}
