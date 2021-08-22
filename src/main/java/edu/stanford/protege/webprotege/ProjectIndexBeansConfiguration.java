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
    AnnotationAssertionAxiomsIndexWrapperImpl AnnotationAssertionAxiomsIndex(ProjectOntologiesIndex projectOntologiesIndex,
                                                                             AxiomsByTypeIndex axiomsByTypeIndex,
                                                                             ProjectAnnotationAssertionAxiomsBySubjectIndex projectAnnotationAssertionAxiomsBySubjectIndex) {
        return new AnnotationAssertionAxiomsIndexWrapperImpl(projectOntologiesIndex,
                                                             axiomsByTypeIndex,
                                                             projectAnnotationAssertionAxiomsBySubjectIndex);
    }

    @Bean
    ProjectAnnotationAssertionAxiomsBySubjectIndexImpl ProjectAnnotationAssertionAxiomsBySubjectIndexImpl(
            ProjectOntologiesIndex projectOntologiesIndex,
            AnnotationAssertionAxiomsBySubjectIndex annotationAssertionAxiomsBySubjectIndex) {
        return new ProjectAnnotationAssertionAxiomsBySubjectIndexImpl(projectOntologiesIndex,
                                                                      annotationAssertionAxiomsBySubjectIndex);
    }

    @Bean
    AnnotationAxiomsByIriReferenceIndexImpl AnnotationAxiomsByIriReferenceIndex() {
        return new AnnotationAxiomsByIriReferenceIndexImpl();
    }

    @Bean
    AnnotationPropertyDomainAxiomsIndexImpl AnnotationPropertyDomainAxiomsIndex(AxiomsByTypeIndex axiomsByTypeIndex) {
        return new AnnotationPropertyDomainAxiomsIndexImpl(axiomsByTypeIndex);
    }

    @Bean
    AnnotationPropertyRangeAxiomsIndexImpl AnnotationPropertyRangeAxiomsIndex(AxiomsByTypeIndex axiomsByTypeIndex) {
        return new AnnotationPropertyRangeAxiomsIndexImpl(axiomsByTypeIndex);
    }

    @Bean
    AxiomsByEntityReferenceIndexImpl AxiomsByEntityReferenceIndex(OWLEntityProvider entityProvider) {
        return new AxiomsByEntityReferenceIndexImpl(entityProvider);
    }

    @Bean
    AxiomsByReferenceIndexImpl AxiomsByReferenceIndex(AxiomsByEntityReferenceIndex axiomsByEntityReferenceIndex,
                                                      AnnotationAxiomsByIriReferenceIndex annotationAxiomsByIriReferenceIndex) {
        return new AxiomsByReferenceIndexImpl(axiomsByEntityReferenceIndex,
                                              annotationAxiomsByIriReferenceIndex);
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
    DataPropertyCharacteristicsIndexImpl DataPropertyCharacteristicsIndex(AxiomsByTypeIndex axiomsByTypeIndex) {
        return new DataPropertyCharacteristicsIndexImpl(axiomsByTypeIndex);
    }

    @Bean
    DataPropertyDomainAxiomsIndexImpl DataPropertyDomainAxiomsIndex(AxiomsByTypeIndex axiomsByTypeIndex) {
        return new DataPropertyDomainAxiomsIndexImpl(axiomsByTypeIndex);
    }

    @Bean
    DataPropertyRangeAxiomsIndexImpl DataPropertyRangeAxiomsIndex(AxiomsByTypeIndex axiomsByTypeIndex) {
        return new DataPropertyRangeAxiomsIndexImpl(axiomsByTypeIndex);
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
    DisjointDataPropertiesAxiomsIndexImpl DisjointDataPropertiesAxiomsIndex(AxiomsByTypeIndex axiomsByTypeIndex) {
        return new DisjointDataPropertiesAxiomsIndexImpl(axiomsByTypeIndex);
    }

    @Bean
    DisjointObjectPropertiesAxiomsIndexImpl DisjointObjectPropertiesAxiomsIndex(AxiomsByTypeIndex axiomsByTypeIndex) {
        return new DisjointObjectPropertiesAxiomsIndexImpl(axiomsByTypeIndex);
    }

    @Bean
    EntitiesInOntologySignatureByIriIndexImpl EntitiesInOntologySignatureByIriIndex(AxiomsByEntityReferenceIndexImpl axiomsByEntityReferenceIndex,
                                                                                    OntologyAnnotationsSignatureIndex ontologyAnnotationsIndex) {
        return new EntitiesInOntologySignatureByIriIndexImpl(axiomsByEntityReferenceIndex,
                                                             ontologyAnnotationsIndex);
    }

    @Bean
    EntitiesInOntologySignatureIndexImpl EntitiesInOntologySignatureIndex(OntologyAnnotationsSignatureIndex ontologyAnnotationsIndex,
                                                                          OntologyAxiomsSignatureIndex axiomsByEntityReferenceIndex) {
        return new EntitiesInOntologySignatureIndexImpl(axiomsByEntityReferenceIndex,
                                                        ontologyAnnotationsIndex);
    }

    @Bean
    EntitiesInProjectSignatureByIriIndexImpl EntitiesInProjectSignatureByIriIndex(ProjectOntologiesIndex projectOntologiesIndex,
                                                                                  EntitiesInOntologySignatureByIriIndex entitiesInOntologySignatureByIriIndex) {
        return new EntitiesInProjectSignatureByIriIndexImpl(projectOntologiesIndex,
                                                            entitiesInOntologySignatureByIriIndex);
    }

    @Bean
    EntitiesInProjectSignatureIndexImpl EntitiesInProjectSignatureIndex(ProjectOntologiesIndex projectOntologiesIndex,
                                                                        EntitiesInOntologySignatureIndex entitiesInOntologySignatureIndex) {
        return new EntitiesInProjectSignatureIndexImpl(projectOntologiesIndex,
                                                       entitiesInOntologySignatureIndex);
    }

    @Bean
    EquivalentClassesAxiomsIndexImpl EquivalentClassesAxiomsIndex() {
        return new EquivalentClassesAxiomsIndexImpl();
    }

    @Bean
    EquivalentDataPropertiesAxiomsIndexImpl EquivalentDataPropertiesAxiomsIndex(AxiomsByTypeIndex axiomsByTypeIndex) {
        return new EquivalentDataPropertiesAxiomsIndexImpl(axiomsByTypeIndex);
    }

    @Bean
    EquivalentObjectPropertiesAxiomsIndexImpl EquivalentObjectPropertiesAxiomsIndex(AxiomsByTypeIndex axiomsByTypeIndex) {
        return new EquivalentObjectPropertiesAxiomsIndexImpl(axiomsByTypeIndex);
    }

    @Bean
    IndividualsByTypeIndexImpl IndividualsByTypeIndex(ClassHierarchyProvider classHierarchyProvider,
                                                      DictionaryManager dictionaryManager,
                                                      OWLDataFactory dataFactory,
                                                      ProjectOntologiesIndex projectOntologiesIndex,
                                                      ProjectSignatureByTypeIndex projectSignatureByTypeIndex,
                                                      ClassAssertionAxiomsByIndividualIndex classAssertionAxiomsByIndividualIndex,
                                                      ClassAssertionAxiomsByClassIndex classAssertionAxiomsByClassIndex) {
        return new IndividualsByTypeIndexImpl(projectOntologiesIndex,
                                              projectSignatureByTypeIndex,
                                              classAssertionAxiomsByIndividualIndex,
                                              classAssertionAxiomsByClassIndex,
                                              classHierarchyProvider,
                                              dictionaryManager,
                                              dataFactory);
    }

    @Bean
    IndividualsIndexImpl IndividualsIndex(DictionaryManager dictionaryManager,
                                          ClassHierarchyProvider classHierarchyProvider,
                                          OWLDataFactory dataFactory,
                                          ProjectOntologiesIndex projectOntologiesIndex,
                                          ClassAssertionAxiomsByIndividualIndex classAssertionAxiomsByIndividualIndex,
                                          IndividualsByTypeIndex individualsByTypeIndex) {
        return new IndividualsIndexImpl(projectOntologiesIndex,
                                        classAssertionAxiomsByIndividualIndex,
                                        dictionaryManager,
                                        classHierarchyProvider,
                                        dataFactory,
                                        individualsByTypeIndex);
    }

    @Bean
    InverseObjectPropertyAxiomsIndexImpl InverseObjectPropertyAxiomsIndex(AxiomsByTypeIndex axiomsByTypeIndex) {
        return new InverseObjectPropertyAxiomsIndexImpl(axiomsByTypeIndex);
    }

    @Bean
    ObjectPropertyAssertionAxiomsBySubjectIndexImpl ObjectPropertyAssertionAxiomsBySubjectIndex() {
        return new ObjectPropertyAssertionAxiomsBySubjectIndexImpl();
    }

    @Bean
    ObjectPropertyCharacteristicsIndexImpl ObjectPropertyCharacteristicsIndex(AxiomsByTypeIndex axiomsByTypeIndex) {
        return new ObjectPropertyCharacteristicsIndexImpl(axiomsByTypeIndex);
    }

    @Bean
    ObjectPropertyDomainAxiomsIndexImpl ObjectPropertyDomainAxiomsIndex(AxiomsByTypeIndex axiomsByTypeIndex) {
        return new ObjectPropertyDomainAxiomsIndexImpl(axiomsByTypeIndex);
    }

    @Bean
    ObjectPropertyRangeAxiomsIndexImpl ObjectPropertyRangeAxiomsIndex(AxiomsByTypeIndex axiomsByTypeIndex) {
        return new ObjectPropertyRangeAxiomsIndexImpl(axiomsByTypeIndex);
    }

    @Bean
    OntologyAnnotationsIndexImpl OntologyAnnotationsIndex() {
        return new OntologyAnnotationsIndexImpl();
    }

    @Bean
    OntologyAxiomsIndexImpl OntologyAxiomsIndex(AxiomsByTypeIndexImpl axiomsByTypeIndex) {
        return new OntologyAxiomsIndexImpl(axiomsByTypeIndex);
    }

    @Bean
    OntologySignatureByTypeIndexImpl OntologySignatureByTypeIndex(OntologyAxiomsSignatureIndex axiomsByEntityReferenceIndex,
                                                                  OntologyAnnotationsSignatureIndex ontologyAnnotationsIndex) {
        return new OntologySignatureByTypeIndexImpl(axiomsByEntityReferenceIndex,
                                                    ontologyAnnotationsIndex);
    }

    @Bean
    OntologySignatureIndexImpl OntologySignatureIndex(AxiomsByEntityReferenceIndexImpl axiomsByEntityReferenceIndex) {
        return new OntologySignatureIndexImpl(axiomsByEntityReferenceIndex);
    }

    @Bean
    ProjectClassAssertionAxiomsByIndividualIndexImpl ProjectClassAssertionAxiomsByIndividualIndex(ProjectOntologiesIndex projectOntologiesIndex,
                                                                                                  ClassAssertionAxiomsByIndividualIndex classAssertionAxiomsByIndividualIndex) {
        return new ProjectClassAssertionAxiomsByIndividualIndexImpl(projectOntologiesIndex,
                                                                    classAssertionAxiomsByIndividualIndex);
    }

    @Bean
    ProjectOntologiesIndexImpl ProjectOntologiesIndex(RevisionManager revisionManager) {
        var index = new ProjectOntologiesIndexImpl();
        index.init(revisionManager);
        return index;
    }

    @Bean
    ProjectSignatureByTypeIndexImpl ProjectSignatureByTypeIndex(AxiomsByEntityReferenceIndexImpl axiomsByEntityReferenceIndex) {
        return new ProjectSignatureByTypeIndexImpl(axiomsByEntityReferenceIndex);
    }

    @Bean
    ProjectSignatureIndexImpl ProjectSignatureIndex(ProjectOntologiesIndex projectOntologiesIndex,
                                                    OntologySignatureIndex ontologySignatureIndex) {
        return new ProjectSignatureIndexImpl(projectOntologiesIndex,
                                             ontologySignatureIndex);
    }

    @Bean
    PropertyAssertionAxiomsBySubjectIndexImpl PropertyAssertionAxiomsBySubjectIndex(
            AnnotationAssertionAxiomsBySubjectIndex annotationAssertionAxiomsBySubjectIndex,
            ObjectPropertyAssertionAxiomsBySubjectIndex objectPropertyAssertionAxiomsBySubjectIndex,
            DataPropertyAssertionAxiomsBySubjectIndex dataPropertyAssertionAxiomsBySubjectIndex) {
        return new PropertyAssertionAxiomsBySubjectIndexImpl(annotationAssertionAxiomsBySubjectIndex,
                                                             objectPropertyAssertionAxiomsBySubjectIndex,
                                                             dataPropertyAssertionAxiomsBySubjectIndex);
    }

    @Bean
    SameIndividualAxiomsIndexImpl SameIndividualAxiomsIndex() {
        return new SameIndividualAxiomsIndexImpl();
    }

    @Bean
    SubAnnotationPropertyAxiomsBySubPropertyIndexImpl SubAnnotationPropertyAxiomsBySubPropertyIndex(AxiomsByTypeIndex axiomsByTypeIndex) {
        return new SubAnnotationPropertyAxiomsBySubPropertyIndexImpl(axiomsByTypeIndex);
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
    SubDataPropertyAxiomsBySubPropertyIndexImpl SubDataPropertyAxiomsBySubPropertyIndex(AxiomsByTypeIndex axiomsByTypeIndex) {
        return new SubDataPropertyAxiomsBySubPropertyIndexImpl(axiomsByTypeIndex);
    }

    @Bean
    SubObjectPropertyAxiomsBySubPropertyIndexImpl SubObjectPropertyAxiomsBySubPropertyIndex(AxiomsByTypeIndex axiomsByTypeIndex) {
        return new SubObjectPropertyAxiomsBySubPropertyIndexImpl(axiomsByTypeIndex);
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
