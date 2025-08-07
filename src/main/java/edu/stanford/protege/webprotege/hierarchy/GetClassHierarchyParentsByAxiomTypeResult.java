package edu.stanford.protege.webprotege.hierarchy;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.entity.OWLEntityData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

import static edu.stanford.protege.webprotege.hierarchy.GetClassHierarchyParentsByAxiomTypeAction.CHANNEL;


@JsonTypeName(CHANNEL)
public record GetClassHierarchyParentsByAxiomTypeResult(@JsonProperty("owlClass") @Nullable OWLEntityData owlClass,
                                                        @JsonProperty("parentsBySubclassOf") @Nonnull Set<OWLEntityData> parentsBySubclassOf,
                                                        @JsonProperty("parentsByEquivalentClass") @Nonnull Set<OWLEntityData> parentsByEquivalentClass) implements Result {
}
