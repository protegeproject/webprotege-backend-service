package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.index.*;
import org.semanticweb.owlapi.model.OWLClass;

import javax.annotation.Nonnull;
import java.util.Set;

public class ClassHierarchyProviderFactory {

    private final ProjectId projectId;

    private final ProjectOntologiesIndex projectOntologiesIndex;
    @Nonnull
    private final SubClassOfAxiomsBySubClassIndex subClassOfAxiomsIndex;
    @Nonnull
    private final EquivalentClassesAxiomsIndex equivalentClassesAxiomsIndex;
    @Nonnull
    private final ProjectSignatureByTypeIndex projectSignatureByTypeIndex;
    @Nonnull
    private final EntitiesInProjectSignatureByIriIndex entitiesInProjectSignatureByIriIndex;

    private final @Nonnull ClassHierarchyChildrenAxiomsIndex classHierarchyChildrenAxiomsIndex;

    public ClassHierarchyProviderFactory(ProjectId projectId, ProjectOntologiesIndex projectOntologiesIndex, @Nonnull SubClassOfAxiomsBySubClassIndex subClassOfAxiomsIndex, @Nonnull EquivalentClassesAxiomsIndex equivalentClassesAxiomsIndex, @Nonnull ProjectSignatureByTypeIndex projectSignatureByTypeIndex, @Nonnull EntitiesInProjectSignatureByIriIndex entitiesInProjectSignatureByIriIndex, @Nonnull ClassHierarchyChildrenAxiomsIndex classHierarchyChildrenAxiomsIndex) {
        this.projectId = projectId;
        this.projectOntologiesIndex = projectOntologiesIndex;
        this.subClassOfAxiomsIndex = subClassOfAxiomsIndex;
        this.equivalentClassesAxiomsIndex = equivalentClassesAxiomsIndex;
        this.projectSignatureByTypeIndex = projectSignatureByTypeIndex;
        this.entitiesInProjectSignatureByIriIndex = entitiesInProjectSignatureByIriIndex;
        this.classHierarchyChildrenAxiomsIndex = classHierarchyChildrenAxiomsIndex;
    }

    public ClassHierarchyProvider getClassHierarchyProvider(Set<OWLClass> rootClases) {
        return new ClassHierarchyProviderImpl(projectId,
                rootClases,
                projectOntologiesIndex,
                subClassOfAxiomsIndex,
                equivalentClassesAxiomsIndex,
                projectSignatureByTypeIndex,
                entitiesInProjectSignatureByIriIndex,
                classHierarchyChildrenAxiomsIndex
                );
    }
}
