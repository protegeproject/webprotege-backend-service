package edu.stanford.bmir.protege.web.shared.viz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Oct 2018
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("Relationship")
public abstract class RelationshipEdge implements Edge {

    @JsonCreator
    @Nonnull
    public static RelationshipEdge get(@JsonProperty("tail") @Nonnull OWLEntityData tail,
                                       @JsonProperty("head") @Nonnull OWLEntityData head,
                                       @JsonProperty("relationship") @Nonnull OWLEntityData relationship) {
        return new AutoValue_RelationshipEdge(head, tail, relationship);
    }

    @Nonnull
    public abstract OWLEntityData getRelationship();

    @Override
    public String getRelationshipDescriptor() {
        return getRelationship().getEntity().getIRI().toString();
    }

    @Nonnull
    @Override
    public String getLabel() {
        return getRelationship().getBrowserText();
    }

    @Override
    public boolean isIsA() {
        return false;
    }

    @Override
    public boolean isRelationship() {
        return true;
    }

    @Override
    public Optional<OWLEntityData> getLabellingEntity() {
        return Optional.of(getRelationship());
    }
}
