package edu.stanford.protege.webprotege.forms.field;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.criteria.EntityMatchCriteria;
import edu.stanford.protege.webprotege.forms.PropertyNames;
import org.checkerframework.checker.nullness.Opt;
import org.semanticweb.owlapi.model.OWLProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-25
 */
@AutoValue

@JsonTypeName(OwlClassBinding.TYPE)
public abstract class OwlClassBinding implements OwlBinding {

    public static final String TYPE = "CLASS";

    @JsonCreator
    @Nonnull
    public static OwlClassBinding get(@JsonProperty(PropertyNames.CRITERIA) @Nullable EntityMatchCriteria valuesFilter) {
        return new AutoValue_OwlClassBinding(valuesFilter);
    }

    @Nonnull
    public static OwlClassBinding get() {
        return get(null);
    }

    @Nonnull
    @Override
    public Optional<OWLProperty> getOwlProperty() {
        return Optional.empty();
    }

    @JsonProperty(PropertyNames.CRITERIA)
    @Nullable
    public abstract EntityMatchCriteria getCriteriaInternal();

    @JsonIgnore
    public Optional<EntityMatchCriteria> getCriteria() {
        return Optional.ofNullable(getCriteriaInternal());
    }
}
