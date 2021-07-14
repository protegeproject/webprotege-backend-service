package edu.stanford.protege.webprotege.form.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.form.field.EntityNameControlDescriptor;
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
    public static EntityNameControlData get(@JsonProperty("descriptor") @Nonnull EntityNameControlDescriptor descriptor,
                                            @Nullable OWLEntity entity) {
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

    @Nonnull
    public abstract EntityNameControlDescriptor getDescriptor();

    @Nullable
    @JsonProperty("entity")
    protected abstract OWLEntity getEntityInternal();

    @JsonIgnore
    @Nonnull
    public Optional<OWLEntity> getEntity() {
        return Optional.ofNullable(getEntityInternal());
    }
}
