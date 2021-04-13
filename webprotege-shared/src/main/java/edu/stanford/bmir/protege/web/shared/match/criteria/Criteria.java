package edu.stanford.bmir.protege.web.shared.match.criteria;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Jun 2018
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "match")
@JsonSubTypes({
        @Type(AnnotationValueCriteria.class),
        @Type(AnnotationValueCriteria.class),
        @Type(AnnotationSetCriteria.class),
        @Type(RootCriteria.class)
})
public interface Criteria extends IsSerializable {

}
