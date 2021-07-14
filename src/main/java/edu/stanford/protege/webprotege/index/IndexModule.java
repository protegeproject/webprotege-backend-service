package edu.stanford.protege.webprotege.index;

import edu.stanford.protege.webprotege.index.impl.*;
import edu.stanford.protege.webprotege.inject.ProjectSingleton;
import edu.stanford.protege.webprotege.owlapi.ProjectAnnotationAssertionAxiomsBySubjectIndexImpl;
import edu.stanford.protege.webprotege.project.DefaultOntologyIdManager;
import edu.stanford.protege.webprotege.project.DefaultOntologyIdManagerImpl;
import edu.stanford.protege.webprotege.revision.RevisionManager;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-09-10
 */
public class IndexModule {

    
    ClassFrameAxiomsIndex provideClassFrameAxiomsIndex(ClassFrameAxiomsIndexImpl impl) {
        return impl;
    }

    
    NamedIndividualFrameAxiomIndex provideNamedIndividualFrameAxiomIndex(NamedIndividualFrameAxiomsIndexImpl impl) {
        return impl;
    }

    
    AnnotationAssertionAxiomsBySubjectIndex provideAnnotationAssertionAxiomsBySubjectIndex(
            AnnotationAssertionAxiomsBySubjectIndexImpl impl) {
        return impl;
    }

    
    AnnotationAssertionAxiomsIndex provideAnnotationAssertionAxiomsIndex(AnnotationAssertionAxiomsIndexWrapperImpl impl) {
        return impl;
    }

    
    AnnotationPropertyDomainAxiomsIndex provideAnnotationPropertyDomainAxiomsIndex(
            AnnotationPropertyDomainAxiomsIndexImpl impl) {
        return impl;
    }

    
    AnnotationPropertyRangeAxiomsIndex provideAnnotationPropertyRangeAxiomsIndex(AnnotationPropertyRangeAxiomsIndexImpl impl) {
        return impl;
    }

    
    AxiomsByEntityReferenceIndex provideAxiomsByEntityReferenceIndex(AxiomsByEntityReferenceIndexImpl impl) {
        return impl;
    }

    
    AnnotationAxiomsByIriReferenceIndex provideAxiomsByIriReferenceIndex(AnnotationAxiomsByIriReferenceIndexImpl impl) {
        return impl;
    }

    
    AxiomsByReferenceIndex provideAxiomsByReferenceIndex(AxiomsByReferenceIndexImpl impl) {
        return impl;
    }

    
    AxiomsByTypeIndex provideAxiomsByTypeIndex(AxiomsByTypeIndexImpl impl) {
        return impl;
    }

    
    @ProjectSingleton
    public ClassAssertionAxiomsByClassIndex provideClassAssertionAxiomsByClassIndex(ClassAssertionAxiomsByClassIndexImpl impl) {
        return impl;
    }

    
    ClassAssertionAxiomsByIndividualIndex provideClassAssertionAxiomsByIndividualIndex(
            ClassAssertionAxiomsByIndividualIndexImpl impl) {
        return impl;
    }

    
    DataPropertyAssertionAxiomsBySubjectIndex provideDataPropertyAssertionAxiomsBySubjectIndex(
            DataPropertyAssertionAxiomsBySubjectIndexImpl impl) {
        return impl;
    }

    
    DataPropertyCharacteristicsIndex provideDataPropertyCharacteristicsIndex(DataPropertyCharacteristicsIndexImpl impl) {
        return impl;
    }

    
    DataPropertyDomainAxiomsIndex provideDataPropertyDomainAxiomsIndex(DataPropertyDomainAxiomsIndexImpl impl) {
        return impl;
    }

    
    DataPropertyRangeAxiomsIndex provideDataPropertyRangeAxiomsIndex(DataPropertyRangeAxiomsIndexImpl impl) {
        return impl;
    }

    
    DefaultOntologyIdManager provideDefaultOntologyIdManager(DefaultOntologyIdManagerImpl impl) {
        return impl;
    }

    @ProjectSingleton
    
    DeprecatedEntitiesByEntityIndex provideDeprecatedEntitiesByEntityIndex(DeprecatedEntitiesByEntityIndexImpl impl) {
        return impl;
    }

    @ProjectSingleton
    
    ClassHierarchyChildrenAxiomsIndex provideClassHierarchyChildrenAxiomsIndex(ClassHierarchyChildrenAxiomsIndexImpl impl) {
        return impl;
    }

    @ProjectSingleton
    
    UpdatableIndex provideClassHierarchyChildrenAxiomsIndexIntoSet(ClassHierarchyChildrenAxiomsIndexImpl impl) {
        return impl;
    }



    @ProjectSingleton
    UpdatableIndex provideDeprecatedEntitiesByEntityIndexIntoSet(DeprecatedEntitiesByEntityIndexImpl impl) {
        return impl;
    }

    
    DifferentIndividualsAxiomsIndex provideDifferentIndividualsAxiomsIndex(DifferentIndividualsAxiomsIndexImpl impl) {
        return impl;
    }

    
    DisjointClassesAxiomsIndex provideDisjointClassesAxiomsIndex(DisjointClassesAxiomsIndexImpl impl) {
        return impl;
    }

    
    DisjointDataPropertiesAxiomsIndex provideDisjointDataPropertiesAxiomsIndex(DisjointDataPropertiesAxiomsIndexImpl impl) {
        return impl;
    }

    
    DisjointObjectPropertiesAxiomsIndex provideDisjointObjectPropertiesAxiomsIndex(
            DisjointObjectPropertiesAxiomsIndexImpl impl) {
        return impl;
    }

    
    EntitiesInOntologySignatureByIriIndex provideEntitiesInOntologySignatureByIriIndex(
            EntitiesInOntologySignatureByIriIndexImpl impl) {
        return impl;
    }

    
    EntitiesInOntologySignatureIndex provideEntitiesInOntologySignatureIndex(EntitiesInOntologySignatureIndexImpl impl) {
        return impl;
    }

    
    EntitiesInProjectSignatureByIriIndex provideEntitiesInProjectSignatureByIriIndex(
            EntitiesInProjectSignatureByIriIndexImpl impl) {
        return impl;
    }

    
    EntitiesInProjectSignatureIndex provideEntitiesInProjectSignatureIndexImpl(EntitiesInProjectSignatureIndexImpl impl) {
        return impl;
    }

    
    EquivalentClassesAxiomsIndex provideEquivalentClassesAxiomsIndex(EquivalentClassesAxiomsIndexImpl impl) {
        return impl;
    }

    
    EquivalentDataPropertiesAxiomsIndex provideEquivalentDataPropertiesAxiomsIndex(
            EquivalentDataPropertiesAxiomsIndexImpl impl) {
        return impl;
    }

    
    EquivalentObjectPropertiesAxiomsIndex provideEquivalentObjectPropertiesAxiomsIndex(
            EquivalentObjectPropertiesAxiomsIndexImpl impl) {
        return impl;
    }

    
    IndividualsByTypeIndex provideIndividualsByTypeIndex(IndividualsByTypeIndexImpl impl) {
        return impl;
    }

    
    @ProjectSingleton
    IndividualsIndex provideIndividualsIndex(IndividualsIndexImpl impl) {
        return impl;
    }

    
    InverseObjectPropertyAxiomsIndex provideInverseObjectPropertyAxiomsIndex(InverseObjectPropertyAxiomsIndexImpl impl) {
        return impl;
    }

    
    ObjectPropertyAssertionAxiomsBySubjectIndex provideObjectPropertyAssertionAxiomsBySubjectIndex(
            ObjectPropertyAssertionAxiomsBySubjectIndexImpl impl) {
        return impl;
    }

    
    ObjectPropertyCharacteristicsIndex provideObjectPropertyCharacteristicsIndex(ObjectPropertyCharacteristicsIndexImpl impl) {
        return impl;
    }

    
    ObjectPropertyDomainAxiomsIndex provideObjectPropertyDomainAxiomsIndex(ObjectPropertyDomainAxiomsIndexImpl impl) {
        return impl;
    }

    
    ObjectPropertyRangeAxiomsIndex provideObjectPropertyRangeAxiomsIndex(ObjectPropertyRangeAxiomsIndexImpl impl) {
        return impl;
    }

    @ProjectSingleton
    
    OntologyAnnotationsIndex provideOntologyAnnotationsIndex(OntologyAnnotationsIndexImpl impl) {
        return impl;
    }

    
    OntologyAnnotationsSignatureIndex provideOntologyAnnotationsSignatureIndex(@Nonnull OntologyAnnotationsIndexImpl impl) {
        return impl;
    }

    
    OntologyAxiomsIndex provideOntologyAxiomsIndex(OntologyAxiomsIndexImpl impl) {
        return impl;
    }

    
    OntologyAxiomsSignatureIndex provideOntologyAxiomsSignatureIndex(AxiomsByEntityReferenceIndexImpl impl) {
        return impl;
    }

    
    OntologySignatureByTypeIndex provideOntologySignatureByTypeIndex(OntologySignatureByTypeIndexImpl impl) {
        return impl;
    }

    
    OntologySignatureIndex provideOntologySignatureIndex(OntologySignatureIndexImpl impl) {
        return impl;
    }

    
    ProjectClassAssertionAxiomsByIndividualIndex provideProjectClassAssertionAxiomsByIndividualIndex(
            ProjectClassAssertionAxiomsByIndividualIndexImpl impl) {
        return impl;
    }

    
    ProjectOntologiesIndex provideProjectOntologiesIndex(ProjectOntologiesIndexImpl impl,
                                                         RevisionManager revisionManager) {
        impl.init(revisionManager);
        return impl;
    }

    
    ProjectSignatureByTypeIndex provideProjectSignatureByTypeIndex(ProjectSignatureByTypeIndexImpl impl) {
        return impl;
    }

    
    ProjectSignatureIndex provideProjectSignatureIndex(ProjectSignatureIndexImpl impl) {
        return impl;
    }

    
    PropertyAssertionAxiomsBySubjectIndex providePropertyAssertionAxiomsBySubjectIndex(
            PropertyAssertionAxiomsBySubjectIndexImpl impl) {
        return impl;
    }

    
    SameIndividualAxiomsIndex provideSameIndividualAxiomsIndex(SameIndividualAxiomsIndexImpl impl) {
        return impl;
    }

    
    SubAnnotationPropertyAxiomsBySubPropertyIndex provideSubAnnotationPropertyAxiomsBySubPropertyIndex(
            SubAnnotationPropertyAxiomsBySubPropertyIndexImpl impl) {
        return impl;
    }

    
    SubAnnotationPropertyAxiomsBySuperPropertyIndex provideSubAnnotationPropertyAxiomsBySuperPropertyIndex(
            SubAnnotationPropertyAxiomsBySuperPropertyIndexImpl impl) {
        return impl;
    }

    
    SubClassOfAxiomsBySubClassIndex provideSubClassOfAxiomsBySubClassIndex(SubClassOfAxiomsBySubClassIndexImpl impl) {
        return impl;
    }

    
    SubDataPropertyAxiomsBySubPropertyIndex provideSubDataPropertyAxiomsBySubPropertyIndex(
            SubDataPropertyAxiomsBySubPropertyIndexImpl impl) {
        return impl;
    }

    
    SubObjectPropertyAxiomsBySubPropertyIndex provideSubObjectPropertyAxiomsBySubPropertyIndex(
            SubObjectPropertyAxiomsBySubPropertyIndexImpl impl) {
        return impl;
    }

    
    ProjectAnnotationAssertionAxiomsBySubjectIndex providesHasAnnotationAssertionAxioms(
            ProjectAnnotationAssertionAxiomsBySubjectIndexImpl impl) {
        return impl;
    }

    
    @ProjectSingleton
    public AnnotationAssertionAxiomsByValueIndex provideAnnotationAssertionAxiomsByValueIndex(AnnotationAssertionAxiomsByValueIndexImpl impl) {
        return impl;
    }


    
    
    public UpdatableIndex provideAnnotationAssertionAxiomsBySubjectIndexImplIntoSet(AnnotationAssertionAxiomsBySubjectIndexImpl impl) {
        return impl;
    }

    
    
    public UpdatableIndex provideAnnotationAxiomsByIriReferenceIndexImplIntoSet(AnnotationAxiomsByIriReferenceIndexImpl impl) {
        return impl;
    }

    
    
    public UpdatableIndex provideAxiomsByEntityReferenceIndexImplIntoSet(AxiomsByEntityReferenceIndexImpl impl) {
        return impl;
    }



    
    
    public UpdatableIndex provideAnnotationAssertionAxiomsByValueIndexIntoSet(AnnotationAssertionAxiomsByValueIndexImpl impl) {
        return impl;
    }

    
    
    public UpdatableIndex provideAxiomsByTypeIndexImplIntoSet(AxiomsByTypeIndexImpl impl) {
        return impl;
    }

    
    
    public UpdatableIndex provideClassAssertionAxiomsByClassIndexImplIntoSet(ClassAssertionAxiomsByClassIndexImpl impl) {
        return impl;
    }

    
    
    public UpdatableIndex provideClassAssertionAxiomsByIndividualIndexImplIntoSet(ClassAssertionAxiomsByIndividualIndexImpl impl) {
        return impl;
    }

    
    
    public UpdatableIndex provideDataPropertyAssertionAxiomsBySubjectIndexImplIntoSet(DataPropertyAssertionAxiomsBySubjectIndexImpl impl) {
        return impl;
    }

    
    
    public UpdatableIndex provideDifferentIndividualsAxiomsIndexImplIntoSet(DifferentIndividualsAxiomsIndexImpl impl) {
        return impl;
    }

    
    
    public UpdatableIndex provideDisjointClassesAxiomsIndexImplIntoSet(DisjointClassesAxiomsIndexImpl impl) {
        return impl;
    }

    
    
    public UpdatableIndex provideObjectPropertyAssertionAxiomsBySubjectIndexImplIntoSet(ObjectPropertyAssertionAxiomsBySubjectIndexImpl impl) {
        return impl;
    }

    
    
    public UpdatableIndex provideOntologyAnnotationsIndexImplIntoSet(OntologyAnnotationsIndexImpl impl) {
        return impl;
    }

    
    
    public UpdatableIndex provideSameIndividualAxiomsIndexImplIntoSet(SameIndividualAxiomsIndexImpl impl) {
        return impl;
    }

    
    
    public UpdatableIndex provideSubAnnotationPropertyAxiomsBySuperPropertyIndexImplIntoSet(SubAnnotationPropertyAxiomsBySuperPropertyIndexImpl impl) {
        return impl;
    }

    
    
    public UpdatableIndex provideSubClassOfAxiomsBySubClassIndexImplIntoSet(SubClassOfAxiomsBySubClassIndexImpl impl) {
        return impl;
    }

    
    
    public UpdatableIndex provideEquivalentClassesAxiomsIndexIntoSet(EquivalentClassesAxiomsIndexImpl impl) {
        return impl;
    }

    @ProjectSingleton
    
    IndexUpdater provideIndexUpdater(IndexUpdaterFactory factory) {
        var updater = factory.create();
        updater.buildIndexes();
        return updater;
    }

    @ProjectSingleton
    
    RootIndex provideRootIndex(RootIndexImpl impl) {
        return impl;
    }

    
    
    UpdatableIndex provideProjectOntologiesIndexIntoSet(ProjectOntologiesIndexImpl impl) {
        return impl;
    }

    
    @ProjectSingleton
    public BuiltInOwlEntitiesIndex provideBuiltInOwlEntitiesIndex(@Nonnull BuiltInOwlEntitiesIndexImpl impl) {
        return impl;
    }

    
    @ProjectSingleton
    public BuiltInSkosEntitiesIndex provideBuiltInSkosEntitiesIndex(@Nonnull BuiltInSkosEntitiesIndexImpl impl) {
        return impl;
    }

    
    @ProjectSingleton
    public DeprecatedEntitiesIndex provideDeprecatedEntitiesIndex(@Nonnull DeprecatedEntitiesIndexLuceneImpl impl) {
        return impl;
    }
}
