package edu.stanford.protege.webprotege.viz;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import edu.stanford.protege.webprotege.entity.OWLEntityData;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Oct 2018
 */

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @Type(IsAEdge.class),
        @Type(RelationshipEdge.class)
})
public interface Edge {

    @Nonnull
    OWLEntityData getHead();

    OWLEntityData getTail();

    String getRelationshipDescriptor();

    @Nonnull
    String getLabel();

    boolean isIsA();

    boolean isRelationship();

    Optional<OWLEntityData> getLabellingEntity();
}
