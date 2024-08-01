package edu.stanford.protege.webprotege.forms.field;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-11
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({@JsonSubTypes.Type(value = FixedChoiceListSourceDescriptor.class, name = FixedChoiceListSourceDescriptor.TYPE), @JsonSubTypes.Type(value = DynamicChoiceListSourceDescriptor.class, name = DynamicChoiceListSourceDescriptor.TYPE)})
public interface ChoiceListSourceDescriptor {

}
