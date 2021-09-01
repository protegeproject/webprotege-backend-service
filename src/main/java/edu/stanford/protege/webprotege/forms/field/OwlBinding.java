package edu.stanford.protege.webprotege.forms.field;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.semanticweb.owlapi.model.OWLProperty;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-24
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
                      @JsonSubTypes.Type(value = OwlPropertyBinding.class, name = OwlPropertyBinding.TYPE),
                      @JsonSubTypes.Type(value = OwlClassBinding.class, name = OwlClassBinding.TYPE),
                      @JsonSubTypes.Type(value = OwlInstanceBinding.class, name = OwlInstanceBinding.TYPE),
                      @JsonSubTypes.Type(value = OwlSubClassBinding.class, name = OwlSubClassBinding.TYPE)
              })
public interface OwlBinding {

    String VALUES_CRITERIA = "valuesCriteria";

    @JsonIgnore
    @Nonnull
    Optional<OWLProperty> getOwlProperty();
}
