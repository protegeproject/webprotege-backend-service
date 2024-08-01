package edu.stanford.protege.webprotege.forms.field;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.criteria.EntityMatchCriteria;
import edu.stanford.protege.webprotege.forms.PropertyNames;
import org.semanticweb.owlapi.model.OWLProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-03-26
 */
@AutoValue

@JsonTypeName(OwlInstanceBinding.TYPE)
public abstract class OwlInstanceBinding implements OwlBinding {

    protected static final String TYPE = "INSTANCE";

    @JsonCreator
    @Nonnull
    public static OwlInstanceBinding get(@JsonProperty(PropertyNames.CRITERIA) @Nullable EntityMatchCriteria valuesFilter) {
        return new AutoValue_OwlInstanceBinding(valuesFilter);
    }

    @Nonnull
    public static OwlInstanceBinding get() {
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
