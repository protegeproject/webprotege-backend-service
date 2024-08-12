package edu.stanford.protege.webprotege.forms.data;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLPrimitive;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-30
 */
@AutoValue
public abstract class EntityFormControlData implements PrimitiveFormControlData {

    @JsonCreator
    public static EntityFormControlData get(@Nonnull OWLEntity entity) {
        return new AutoValue_EntityFormControlData(entity);
    }

    @JsonValue
    @Nonnull
    public abstract OWLEntity getEntity();

    @Nonnull
    @Override
    public Optional<OWLEntity> asEntity() {
        return Optional.of(getEntity());
    }

    @Nonnull
    @Override
    public Optional<IRI> asIri() {
        return Optional.empty();
    }

    @Nonnull
    @Override
    public Optional<OWLLiteral> asLiteral() {
        return Optional.empty();
    }

    @JsonIgnore
    @Nonnull
    @Override
    public OWLPrimitive getPrimitive() {
        return getEntity();
    }

    @Override
    public PrimitiveFormControlDataProxy toPrimitiveFormControlDataProxy() {
        return new PrimitiveFormControlDataProxy(getEntity().getIRI().toString(),
                                                 getEntity().getEntityType().getName(),
                                                 null,
                                                 null,
                                                 null);
    }
}
