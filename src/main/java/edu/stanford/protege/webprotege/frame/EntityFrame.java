package edu.stanford.protege.webprotege.frame;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import edu.stanford.protege.webprotege.entity.OWLEntityData;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/01/2013
 * <p>
 *     A marker interface for entity frames.
 * </p>
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property="type")
@JsonSubTypes({
        @Type(ClassFrame.class),
        @Type(ObjectPropertyFrame.class),
        @Type(DataPropertyFrame.class),
        @Type(AnnotationPropertyFrame.class),
        @Type(NamedIndividualFrame.class),
})
public interface EntityFrame<E extends OWLEntityData> extends Frame<E> {

}
