package edu.stanford.protege.webprotege.forms.field;

import com.fasterxml.jackson.annotation.*;
import edu.stanford.protege.webprotege.criteria.*;
import edu.stanford.protege.webprotege.forms.PropertyNames;
import org.semanticweb.owlapi.model.OWLProperty;

import javax.annotation.*;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-24
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({@JsonSubTypes.Type(value = OwlPropertyBinding.class, name = OwlPropertyBinding.TYPE), @JsonSubTypes.Type(value = OwlClassBinding.class, name = OwlClassBinding.TYPE), @JsonSubTypes.Type(value = OwlInstanceBinding.class, name = OwlInstanceBinding.TYPE), @JsonSubTypes.Type(value = OwlSubClassBinding.class, name = OwlSubClassBinding.TYPE)})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public interface OwlBinding {

    @JsonIgnore
    @Nonnull
    Optional<OWLProperty> getOwlProperty();
}
