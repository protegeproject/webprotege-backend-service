package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.index.*;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyProvider;

import java.util.Set;

public class AnnotationPropertyHierarchyProviderFactory {

    private final ProjectId projectId;

    private final OWLAnnotationPropertyProvider annotationPropertyProvider;

    private final ProjectSignatureByTypeIndex projectSignatureByTypeIndex;

    private final ProjectOntologiesIndex projectOntologyIndex;

    private final SubAnnotationPropertyAxiomsBySubPropertyIndex subAnnotationPropertyAxioms;

    private final SubAnnotationPropertyAxiomsBySuperPropertyIndex subAnnotationPropertyAxiomsBySuperPropertyIndex;

    private final EntitiesInProjectSignatureIndex entitiesInProjectSignatureIndex;

    private final AnnotationPropertyHierarchyProvider annotationPropertyHierarchyProvider;

    public AnnotationPropertyHierarchyProviderFactory(ProjectId projectId, OWLAnnotationPropertyProvider annotationPropertyProvider, ProjectSignatureByTypeIndex projectSignatureByTypeIndex, ProjectOntologiesIndex projectOntologyIndex, SubAnnotationPropertyAxiomsBySubPropertyIndex subAnnotationPropertyAxioms, SubAnnotationPropertyAxiomsBySuperPropertyIndex subAnnotationPropertyAxiomsBySuperPropertyIndex, EntitiesInProjectSignatureIndex entitiesInProjectSignatureIndex, AnnotationPropertyHierarchyProvider annotationPropertyHierarchyProvider) {
        this.projectId = projectId;
        this.annotationPropertyProvider = annotationPropertyProvider;
        this.projectSignatureByTypeIndex = projectSignatureByTypeIndex;
        this.projectOntologyIndex = projectOntologyIndex;
        this.subAnnotationPropertyAxioms = subAnnotationPropertyAxioms;
        this.subAnnotationPropertyAxiomsBySuperPropertyIndex = subAnnotationPropertyAxiomsBySuperPropertyIndex;
        this.entitiesInProjectSignatureIndex = entitiesInProjectSignatureIndex;
        this.annotationPropertyHierarchyProvider = annotationPropertyHierarchyProvider;
    }

    public AnnotationPropertyHierarchyProvider getAnnotationPropertyHierarchyProvider(Set<OWLAnnotationProperty> roots) {
        if(roots.isEmpty()) {
            return annotationPropertyHierarchyProvider;
        }
        return new AnnotationPropertyHierarchyProviderImpl(projectId,
                annotationPropertyProvider,
                projectSignatureByTypeIndex,
                projectOntologyIndex,
                subAnnotationPropertyAxioms,
                subAnnotationPropertyAxiomsBySuperPropertyIndex,
                entitiesInProjectSignatureIndex);
    }
}
