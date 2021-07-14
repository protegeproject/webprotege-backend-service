package edu.stanford.protege.webprotege.viz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.entity.OWLEntityData;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Oct 2018
 */
@AutoValue

@JsonTypeName("IsA")
public abstract class IsAEdge implements Edge {

    private static final String DESCRIPTOR = "ISA";

    @JsonCreator
    public static IsAEdge get(@JsonProperty("tail") @Nonnull OWLEntityData tail,
                              @JsonProperty("head") @Nonnull OWLEntityData head) {
        return new AutoValue_IsAEdge(head, tail);
    }

    @Override
    public String getRelationshipDescriptor() {
        return DESCRIPTOR;
    }

    @Nonnull
    @Override
    public String getLabel() {
        return "";
    }

    @Override
    public boolean isIsA() {
        return true;
    }

    @Override
    public boolean isRelationship() {
        return false;
    }

    @Override
    public Optional<OWLEntityData> getLabellingEntity() {
        return Optional.empty();
    }
}
