package edu.stanford.protege.webprotege.forms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.PropertyName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.protege.webprotege.forms.PropertyNames;
import edu.stanford.protege.webprotege.forms.field.EntityNameControlDescriptor;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-08
 */
@AutoValue
@JsonTypeName("EntityNameControlData")
public abstract class EntityNameControlData implements FormControlData {

    @JsonCreator
    public static EntityNameControlData get(@JsonProperty(PropertyNames.CONTROL) @JsonAlias(PropertyNames.DESCRIPTOR) @Nonnull EntityNameControlDescriptor descriptor,
                                            @JsonProperty(PropertyNames.ENTITY) @Nullable OWLEntity entity) {
        return new AutoValue_EntityNameControlData(descriptor, entity);
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
    public abstract EntityNameControlDescriptor getDescriptor();

    @Nullable
    @JsonProperty(PropertyNames.ENTITY)
    protected abstract OWLEntity getEntityInternal();

    @JsonIgnore
    @Nonnull
    public Optional<OWLEntity> getEntity() {
        return Optional.ofNullable(getEntityInternal());
    }
}
