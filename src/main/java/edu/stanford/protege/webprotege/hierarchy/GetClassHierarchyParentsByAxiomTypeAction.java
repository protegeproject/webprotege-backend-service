package edu.stanford.protege.webprotege.hierarchy;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import org.semanticweb.owlapi.model.OWLClass;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.protege.webprotege.hierarchy.GetClassHierarchyParentsByAxiomTypeAction.CHANNEL;


@JsonTypeName(CHANNEL)
public record GetClassHierarchyParentsByAxiomTypeAction(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                                        @JsonProperty("owlClass") @Nonnull OWLClass owlClass,
                                                        @JsonProperty("classHierarchyDescriptor") @Nonnull ClassHierarchyDescriptor classHierarchyDescriptor) implements ProjectAction<GetClassHierarchyParentsByAxiomTypeResult> {

    public static final String CHANNEL = "webprotege.hierarchies.GetClassHierarchyParentsByAxiomType";

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    public GetClassHierarchyParentsByAxiomTypeAction(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                                     @JsonProperty("owlClass") @Nonnull OWLClass owlClass,
                                                     @JsonProperty("classHierarchyDescriptor") @Nonnull ClassHierarchyDescriptor classHierarchyDescriptor) {
        this.projectId = checkNotNull(projectId);
        this.owlClass = checkNotNull(owlClass);
        this.classHierarchyDescriptor = checkNotNull(classHierarchyDescriptor);
    }
}
