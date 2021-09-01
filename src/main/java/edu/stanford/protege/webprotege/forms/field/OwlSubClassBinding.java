package edu.stanford.protege.webprotege.forms.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import org.semanticweb.owlapi.model.OWLProperty;

import javax.annotation.Nonnull;
import java.util.Optional;

@AutoValue

@JsonTypeName(OwlSubClassBinding.TYPE)
public abstract class OwlSubClassBinding implements OwlBinding {

    public static final String TYPE = "SUBCLASS";

    @JsonCreator
    @Nonnull
    public static OwlSubClassBinding get() {
        return new AutoValue_OwlSubClassBinding();
    }

    @Nonnull
    @Override
    public Optional<OWLProperty> getOwlProperty() {
        return Optional.empty();
    }
}
