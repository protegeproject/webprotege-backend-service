package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.index.*;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import java.util.Set;

public class ObjectPropertyHierarchyProviderFactory {

    private final ProjectId projectId;

    private final EntitiesInProjectSignatureIndex entitiesInProjectSignatureIndex;

    private final ProjectOntologiesIndex projectOntologiesIndex;

    private final OntologySignatureByTypeIndex ontologySignatureByTypeIndex;

    private final SubObjectPropertyAxiomsBySubPropertyIndex subObjectPropertyAxiomsBySubPropertyIndex;

    private final AxiomsByTypeIndex axiomsByTypeIndex;

    public ObjectPropertyHierarchyProviderFactory(ProjectId projectId, EntitiesInProjectSignatureIndex entitiesInProjectSignatureIndex, ProjectOntologiesIndex projectOntologiesIndex, OntologySignatureByTypeIndex ontologySignatureByTypeIndex, SubObjectPropertyAxiomsBySubPropertyIndex subObjectPropertyAxiomsBySubPropertyIndex, AxiomsByTypeIndex axiomsByTypeIndex) {
        this.projectId = projectId;
        this.entitiesInProjectSignatureIndex = entitiesInProjectSignatureIndex;
        this.projectOntologiesIndex = projectOntologiesIndex;
        this.ontologySignatureByTypeIndex = ontologySignatureByTypeIndex;
        this.subObjectPropertyAxiomsBySubPropertyIndex = subObjectPropertyAxiomsBySubPropertyIndex;
        this.axiomsByTypeIndex = axiomsByTypeIndex;
    }

    public ObjectPropertyHierarchyProvider getObjectPropertyHierarchyProvider(Set<OWLObjectProperty> roots) {
        return new ObjectPropertyHierarchyProviderImpl(projectId,
                roots.iterator().next(),
                entitiesInProjectSignatureIndex,
                projectOntologiesIndex,
                ontologySignatureByTypeIndex,
                subObjectPropertyAxiomsBySubPropertyIndex,
                axiomsByTypeIndex);
    }
}
