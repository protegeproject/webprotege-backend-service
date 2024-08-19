package edu.stanford.protege.webprotege.forms.data;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.forms.PropertyNames;
import edu.stanford.protege.webprotege.forms.field.ImageControlDescriptor;
import org.semanticweb.owlapi.model.IRI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-08
 */
@AutoValue

@JsonTypeName("ImageControlData")
public abstract class ImageControlData implements FormControlData {

    @JsonCreator
    public static ImageControlData get(@JsonProperty(PropertyNames.CONTROL) @Nonnull ImageControlDescriptor descriptor,
                                       @JsonProperty(PropertyNames.IRI) @Nullable IRI iri) {
        return new AutoValue_ImageControlData(descriptor, iri);
    }

    @Override
    public <R> R accept(@Nonnull FormControlDataVisitorEx<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public void accept(@Nonnull FormControlDataVisitor visitor) {
        visitor.visit(this);
    }

    @JsonProperty(PropertyNames.CONTROL)
    @Nonnull
    public abstract ImageControlDescriptor getDescriptor();

    @JsonProperty(PropertyNames.IRI)
    @Nullable
    protected abstract IRI getIriInternal();

    @JsonIgnore
    @Nonnull
    public Optional<IRI> getIri() {
        return Optional.ofNullable(getIriInternal());
    }
}
