package edu.stanford.protege.webprotege;

import edu.stanford.protege.webprotege.hierarchy.ClassHierarchyProvider;
import edu.stanford.protege.webprotege.index.*;
import edu.stanford.protege.webprotege.index.impl.*;
import edu.stanford.protege.webprotege.owlapi.ProjectAnnotationAssertionAxiomsBySubjectIndexImpl;
import edu.stanford.protege.webprotege.revision.RevisionManager;
import edu.stanford.protege.webprotege.shortform.DictionaryManager;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntityProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-07-12
 */
public class ProjectIndexBeansConfiguration {

    @Bean
    AnnotationAssertionAxiomsBySubjectIndexImpl AnnotationAssertionAxiomsBySubjectIndex() {
        return new AnnotationAssertionAxiomsBySubjectIndexImpl();
    }

    @Bean
    AnnotationAssertionAxiomsByValueIndexImpl AnnotationAssertionAxiomsByValueIndex() {
        return new AnnotationAssertionAxiomsByValueIndexImpl();
    }

    @Bean
    AnnotationAssertionAxiomsIndexWrapperImpl AnnotationAssertionAxiomsIndex(ProjectOntologiesIndex projectOntologiesIndex) {
        return new AnnotationAssertionAxiomsIndexWrapperImpl(projectOntologiesIndex,
                                                             AxiomsByTypeIndex(),
                                                             ProjectAnnotationAssertionAxiomsBySubjectIndexImpl(projectOntologiesIndex));
    }

    @Bean
    ProjectAnnotationAssertionAxiomsBySubjectIndexImpl ProjectAnnotationAssertionAxiomsBySubjectIndexImpl(ProjectOntologiesIndex projectOntologiesIndex) {
        return new ProjectAnnotationAssertionAxiomsBySubjectIndexImpl(projectOntologiesIndex,
                                                                      AnnotationAssertionAxiomsBySubjectIndex());
    }

    @Bean
    AnnotationAxiomsByIriReferenceIndexImpl AnnotationAxiomsByIriReferenceIndex() {
        return new AnnotationAxiomsByIriReferenceIndexImpl();
    }

    @Bean
    AnnotationPropertyDomainAxiomsIndexImpl AnnotationPropertyDomainAxiomsIndex() {
        return new AnnotationPropertyDomainAxiomsIndexImpl(AxiomsByTypeIndex());
    }

    @Bean
    AnnotationPropertyRangeAxiomsIndexImpl AnnotationPropertyRangeAxiomsIndex() {
        return new AnnotationPropertyRangeAxiomsIndexImpl(AxiomsByTypeIndex());
    }

    @Bean
    AxiomsByEntityReferenceIndexImpl AxiomsByEntityReferenceIndex(OWLEntityProvider entityProvider) {
        return new AxiomsByEntityReferenceIndexImpl(entityProvider);
    }

    @Bean
    AxiomsByReferenceIndexImpl AxiomsByReferenceIndex(OWLEntityProvider entityProvider) {
        return new AxiomsByReferenceIndexImpl(AxiomsByEntityReferenceIndex(entityProvider),
                                              AnnotationAxiomsByIriReferenceIndex());
    }

    @Bean
    AxiomsByTypeIndexImpl AxiomsByTypeIndex() {
        return new AxiomsByTypeIndexImpl();
    }

    @Bean
    ClassAssertionAxiomsByClassIndexImpl ClassAssertionAxiomsByClassIndex() {
        return new ClassAssertionAxiomsByClassIndexImpl();
    }

    @Bean
    ClassAssertionAxiomsByIndividualIndexImpl ClassAssertionAxiomsByIndividualIndex() {
        return new ClassAssertionAxiomsByIndividualIndexImpl();
    }

    @Bean
    ClassHierarchyChildrenAxiomsIndexImpl ClassHierarchyChildrenAxiomsIndex(ProjectOntologiesIndex projectOntologiesIndex) {
        return new ClassHierarchyChildrenAxiomsIndexImpl(projectOntologiesIndex);
    }

    @Bean
    DataPropertyAssertionAxiomsBySubjectIndexImpl DataPropertyAssertionAxiomsBySubjectIndex() {
        return new DataPropertyAssertionAxiomsBySubjectIndexImpl();
    }

    @Bean
    DataPropertyCharacteristicsIndexImpl DataPropertyCharacteristicsIndex() {
        return new DataPropertyCharacteristicsIndexImpl(AxiomsByTypeIndex());
    }

    @Bean
    DataPropertyDomainAxiomsIndexImpl DataPropertyDomainAxiomsIndex() {
        return new DataPropertyDomainAxiomsIndexImpl(AxiomsByTypeIndex());
    }

    @Bean
    DataPropertyRangeAxiomsIndexImpl DataPropertyRangeAxiomsIndex() {
        return new DataPropertyRangeAxiomsIndexImpl(AxiomsByTypeIndex());
    }

    @Bean
    DeprecatedEntitiesByEntityIndexImpl DeprecatedEntitiesByEntityIndex(ProjectOntologiesIndex projectOntologiesIndex) {
        return new DeprecatedEntitiesByEntityIndexImpl(projectOntologiesIndex);
    }

    @Bean
    DifferentIndividualsAxiomsIndexImpl DifferentIndividualsAxiomsIndex() {
        return new DifferentIndividualsAxiomsIndexImpl();
    }

    @Bean
    DisjointClassesAxiomsIndexImpl DisjointClassesAxiomsIndex() {
        return new DisjointClassesAxiomsIndexImpl();
    }

    @Bean
    DisjointDataPropertiesAxiomsIndexImpl DisjointDataPropertiesAxiomsIndex() {
        return new DisjointDataPropertiesAxiomsIndexImpl(AxiomsByTypeIndex());
    }

    @Bean
    DisjointObjectPropertiesAxiomsIndexImpl DisjointObjectPropertiesAxiomsIndex() {
        return new DisjointObjectPropertiesAxiomsIndexImpl(AxiomsByTypeIndex());
    }

    @Bean
    EntitiesInOntologySignatureByIriIndexImpl EntitiesInOntologySignatureByIriIndex(OWLEntityProvider entityProvider) {
        return new EntitiesInOntologySignatureByIriIndexImpl(AxiomsByEntityReferenceIndex(entityProvider),
                                                             OntologyAnnotationsIndex());
    }

    @Bean
    EntitiesInOntologySignatureIndexImpl EntitiesInOntologySignatureIndex(OWLEntityProvider entityProvider) {
        return new EntitiesInOntologySignatureIndexImpl(AxiomsByEntityReferenceIndex(entityProvider),
                                                        OntologyAnnotationsIndex());
    }

    @Bean
    EntitiesInProjectSignatureByIriIndexImpl EntitiesInProjectSignatureByIriIndex(OWLEntityProvider entityProvider,
                                                                                  ProjectOntologiesIndex projectOntologiesIndex) {
        return new EntitiesInProjectSignatureByIriIndexImpl(projectOntologiesIndex,
                                                            EntitiesInOntologySignatureByIriIndex(entityProvider));
    }

    @Bean
    EntitiesInProjectSignatureIndexImpl EntitiesInProjectSignatureIndex(OWLEntityProvider entityProvider, ProjectOntologiesIndex projectOntologiesIndex) {
        return new EntitiesInProjectSignatureIndexImpl(projectOntologiesIndex,
                                                       EntitiesInOntologySignatureIndex(entityProvider));
    }

    @Bean
    EquivalentClassesAxiomsIndexImpl EquivalentClassesAxiomsIndex() {
        return new EquivalentClassesAxiomsIndexImpl();
    }

    @Bean
    EquivalentDataPropertiesAxiomsIndexImpl EquivalentDataPropertiesAxiomsIndex() {
        return new EquivalentDataPropertiesAxiomsIndexImpl(AxiomsByTypeIndex());
    }

    @Bean
    EquivalentObjectPropertiesAxiomsIndexImpl EquivalentObjectPropertiesAxiomsIndex() {
        return new EquivalentObjectPropertiesAxiomsIndexImpl(AxiomsByTypeIndex());
    }

    @Bean
    IndividualsByTypeIndexImpl IndividualsByTypeIndex(ClassHierarchyProvider classHierarchyProvider,
                                                      DictionaryManager dictionaryManager,
                                                      OWLDataFactory dataFactory,
                                                      OWLEntityProvider entityProvider,
                                                      ProjectOntologiesIndex projectOntologiesIndex) {
        return new IndividualsByTypeIndexImpl(projectOntologiesIndex,
                                              ProjectSignatureByTypeIndex(entityProvider),
                                              ClassAssertionAxiomsByIndividualIndex(),
                                              ClassAssertionAxiomsByClassIndex(),
                                              classHierarchyProvider,
                                              dictionaryManager,
                                              dataFactory);
    }

    @Bean
    IndividualsIndexImpl IndividualsIndex(DictionaryManager dictionaryManager,
                                          ClassHierarchyProvider classHierarchyProvider,
                                          OWLDataFactory dataFactory,
                                          OWLEntityProvider entityProvider,
                                          ProjectOntologiesIndex projectOntologiesIndex) {
        return new IndividualsIndexImpl(projectOntologiesIndex,
                                        ClassAssertionAxiomsByIndividualIndex(),
                                        dictionaryManager,
                                        classHierarchyProvider,
                                        dataFactory,
                                        IndividualsByTypeIndex(classHierarchyProvider,
                                                               dictionaryManager,
                                                               dataFactory,
                                                               entityProvider,
                                                               projectOntologiesIndex));
    }

    @Bean
    InverseObjectPropertyAxiomsIndexImpl InverseObjectPropertyAxiomsIndex() {
        return new InverseObjectPropertyAxiomsIndexImpl(AxiomsByTypeIndex());
    }

    @Bean
    ObjectPropertyAssertionAxiomsBySubjectIndexImpl ObjectPropertyAssertionAxiomsBySubjectIndex() {
        return new ObjectPropertyAssertionAxiomsBySubjectIndexImpl();
    }

    @Bean
    ObjectPropertyCharacteristicsIndexImpl ObjectPropertyCharacteristicsIndex() {
        return new ObjectPropertyCharacteristicsIndexImpl(AxiomsByTypeIndex());
    }

    @Bean
    ObjectPropertyDomainAxiomsIndexImpl ObjectPropertyDomainAxiomsIndex() {
        return new ObjectPropertyDomainAxiomsIndexImpl(AxiomsByTypeIndex());
    }

    @Bean
    ObjectPropertyRangeAxiomsIndexImpl ObjectPropertyRangeAxiomsIndex() {
        return new ObjectPropertyRangeAxiomsIndexImpl(AxiomsByTypeIndex());
    }

    @Bean
    OntologyAnnotationsIndexImpl OntologyAnnotationsIndex() {
        return new OntologyAnnotationsIndexImpl();
    }

    @Bean
    OntologyAxiomsIndexImpl OntologyAxiomsIndex() {
        return new OntologyAxiomsIndexImpl(AxiomsByTypeIndex());
    }

    @Bean
    OntologySignatureByTypeIndexImpl OntologySignatureByTypeIndex(OWLEntityProvider entityProvider) {
        return new OntologySignatureByTypeIndexImpl(AxiomsByEntityReferenceIndex(entityProvider),
                                                    OntologyAnnotationsIndex());
    }

    @Bean
    OntologySignatureIndexImpl OntologySignatureIndex(OWLEntityProvider entityProvider) {
        return new OntologySignatureIndexImpl(AxiomsByEntityReferenceIndex(entityProvider));
    }

    @Bean
    ProjectClassAssertionAxiomsByIndividualIndexImpl ProjectClassAssertionAxiomsByIndividualIndex(ProjectOntologiesIndex projectOntologiesIndex) {
        return new ProjectClassAssertionAxiomsByIndividualIndexImpl(projectOntologiesIndex,
                                                                    ClassAssertionAxiomsByIndividualIndex());
    }

    @Bean
    ProjectOntologiesIndexImpl ProjectOntologiesIndex(RevisionManager revisionManager) {
        var index = new ProjectOntologiesIndexImpl();
        index.init(revisionManager);
        return index;
    }

    @Bean
    ProjectSignatureByTypeIndexImpl ProjectSignatureByTypeIndex(OWLEntityProvider entityProvider) {
        return new ProjectSignatureByTypeIndexImpl(AxiomsByEntityReferenceIndex(entityProvider));
    }

    @Bean
    ProjectSignatureIndexImpl ProjectSignatureIndex(OWLEntityProvider entityProvider,
                                                    ProjectOntologiesIndex projectOntologiesIndex) {
        return new ProjectSignatureIndexImpl(projectOntologiesIndex,
                                             OntologySignatureIndex(entityProvider));
    }

    @Bean
    PropertyAssertionAxiomsBySubjectIndexImpl PropertyAssertionAxiomsBySubjectIndex() {
        return new PropertyAssertionAxiomsBySubjectIndexImpl(AnnotationAssertionAxiomsBySubjectIndex(),
                                                             ObjectPropertyAssertionAxiomsBySubjectIndex(),
                                                             DataPropertyAssertionAxiomsBySubjectIndex());
    }

    @Bean
    SameIndividualAxiomsIndexImpl SameIndividualAxiomsIndex() {
        return new SameIndividualAxiomsIndexImpl();
    }

    @Bean
    SubAnnotationPropertyAxiomsBySubPropertyIndexImpl SubAnnotationPropertyAxiomsBySubPropertyIndex() {
        return new SubAnnotationPropertyAxiomsBySubPropertyIndexImpl(AxiomsByTypeIndex());
    }

    @Bean
    SubAnnotationPropertyAxiomsBySuperPropertyIndexImpl SubAnnotationPropertyAxiomsBySuperPropertyIndex() {
        return new SubAnnotationPropertyAxiomsBySuperPropertyIndexImpl();
    }

    @Bean
    SubClassOfAxiomsBySubClassIndexImpl SubClassOfAxiomsBySubClassIndex() {
        return new SubClassOfAxiomsBySubClassIndexImpl();
    }

    @Bean
    SubDataPropertyAxiomsBySubPropertyIndexImpl SubDataPropertyAxiomsBySubPropertyIndex() {
        return new SubDataPropertyAxiomsBySubPropertyIndexImpl(AxiomsByTypeIndex());
    }

    @Bean
    SubObjectPropertyAxiomsBySubPropertyIndexImpl SubObjectPropertyAxiomsBySubPropertyIndex() {
        return new SubObjectPropertyAxiomsBySubPropertyIndexImpl(AxiomsByTypeIndex());
    }

    @Bean
    ClassFrameAxiomsIndex classFrameAxiomsIndex(ProjectOntologiesIndex p1,
                                                SubClassOfAxiomsBySubClassIndex p2,
                                                EquivalentClassesAxiomsIndex p3,
                                                AnnotationAssertionAxiomsBySubjectIndex p4) {
        return new ClassFrameAxiomsIndexImpl(p1, p2, p3, p4);
    }

    @Bean
    NamedIndividualFrameAxiomIndex namedIndividualFrameAxiomIndex(ProjectOntologiesIndex p1,
                                                                  ClassAssertionAxiomsByIndividualIndex p2,
                                                                  PropertyAssertionAxiomsBySubjectIndex p3,
                                                                  SameIndividualAxiomsIndex p4) {
        return new NamedIndividualFrameAxiomsIndexImpl(p1, p2, p3, p4);
    }
}
