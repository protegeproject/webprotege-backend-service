package edu.stanford.protege.webprotege.forms.field;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.criteria.*;
import edu.stanford.protege.webprotege.forms.PropertyNames;
import org.semanticweb.owlapi.model.OWLProperty;

import javax.annotation.*;
import java.util.Optional;

@AutoValue

@JsonTypeName(OwlSubClassBinding.TYPE)
public abstract class OwlSubClassBinding implements OwlBinding {

    public static final String TYPE = "SUBCLASS";

    @JsonCreator
    @Nonnull
    public static OwlSubClassBinding get(@JsonProperty(PropertyNames.CRITERIA) @Nullable CompositeRootCriteria criteria) {
        return new AutoValue_OwlSubClassBinding(criteria);
    }

    @Nonnull
    @Override
    public Optional<OWLProperty> getOwlProperty() {
        return Optional.empty();
    }


    @JsonProperty(PropertyNames.CRITERIA)
    @Nullable
    public abstract CompositeRootCriteria getCriteriaInternal();

    @JsonIgnore
    public Optional<CompositeRootCriteria> getCriteria() {
        return Optional.ofNullable(getCriteriaInternal());
    }
}
