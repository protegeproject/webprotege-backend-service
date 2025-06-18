package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.DataFactory;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.index.*;
import org.semanticweb.owlapi.model.OWLDataProperty;

import java.util.Collections;
import java.util.Set;

public class DataPropertyHierarchyProviderFactory {

    private final ProjectId projectId;

    private final EntitiesInProjectSignatureIndex entitiesInProjectSignatureIndex;

    private final ProjectOntologiesIndex projectOntologiesIndex;

    private final OntologySignatureByTypeIndex ontologySignatureByTypeIndex;

    private final SubDataPropertyAxiomsBySubPropertyIndex subDataPropertyAxiomsBySubPropertyIndex;

    private final AxiomsByTypeIndex axiomsByTypeIndex;

    private final DataPropertyHierarchyProvider dataPropertyHierarchyProvider;

    public DataPropertyHierarchyProviderFactory(ProjectId projectId, EntitiesInProjectSignatureIndex entitiesInProjectSignatureIndex, ProjectOntologiesIndex projectOntologiesIndex, OntologySignatureByTypeIndex ontologySignatureByTypeIndex, SubDataPropertyAxiomsBySubPropertyIndex subDataPropertyAxiomsBySubPropertyIndex, AxiomsByTypeIndex axiomsByTypeIndex, DataPropertyHierarchyProvider dataPropertyHierarchyProvider) {
        this.projectId = projectId;
        this.entitiesInProjectSignatureIndex = entitiesInProjectSignatureIndex;
        this.projectOntologiesIndex = projectOntologiesIndex;
        this.ontologySignatureByTypeIndex = ontologySignatureByTypeIndex;
        this.subDataPropertyAxiomsBySubPropertyIndex = subDataPropertyAxiomsBySubPropertyIndex;
        this.axiomsByTypeIndex = axiomsByTypeIndex;
        this.dataPropertyHierarchyProvider = dataPropertyHierarchyProvider;
    }

    public DataPropertyHierarchyProvider getDataPropertyHierarchyProvider(Set<OWLDataProperty> roots) {
        if(roots.equals(Collections.singleton(DataFactory.getOWLTopDataProperty()))) {
            return dataPropertyHierarchyProvider;
        }
        return new DataPropertyHierarchyProviderImpl(projectId,
                roots.iterator().next(),
                projectOntologiesIndex,
                axiomsByTypeIndex,
                ontologySignatureByTypeIndex,
                subDataPropertyAxiomsBySubPropertyIndex,
                entitiesInProjectSignatureIndex);
    }
}
