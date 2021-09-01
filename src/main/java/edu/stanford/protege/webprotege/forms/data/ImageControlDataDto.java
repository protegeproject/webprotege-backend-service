package edu.stanford.protege.webprotege.forms.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.forms.field.ImageControlDescriptor;
import org.semanticweb.owlapi.model.IRI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

@AutoValue

@JsonTypeName("ImageControlDataDto")
public abstract class ImageControlDataDto implements FormControlDataDto {

    @JsonCreator
    @Nonnull
    public static ImageControlDataDto get(@JsonProperty("descriptor") @Nonnull ImageControlDescriptor descriptor,
                                          @JsonProperty("iri") @Nonnull IRI iri,
                                          @JsonProperty("depth") int depth) {
        return new AutoValue_ImageControlDataDto(depth, descriptor, iri);
    }

    @JsonProperty("descriptor")
    @Nonnull
    public abstract ImageControlDescriptor getDescriptor();

    @JsonProperty("iri")
    @Nullable
    protected abstract IRI getIriInternal();

    @Nonnull
    public Optional<IRI> getIri() {
        return Optional.ofNullable(getIriInternal());
    }

    @Override
    public <R> R accept(FormControlDataDtoVisitorEx<R> visitor) {
        return visitor.visit(this);
    }

    @Nonnull
    @Override
    public FormControlData toFormControlData() {
        return ImageControlData.get(getDescriptor(),
                getIriInternal());
    }
}
