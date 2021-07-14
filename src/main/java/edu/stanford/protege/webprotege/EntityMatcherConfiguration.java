package edu.stanford.protege.webprotege;

import edu.stanford.protege.webprotege.frame.translator.AxiomPropertyValueTranslator;
import edu.stanford.protege.webprotege.hierarchy.ClassHierarchyProvider;
import edu.stanford.protege.webprotege.index.*;
import edu.stanford.protege.webprotege.match.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Provider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-07-13
 */
public class EntityMatcherConfiguration {

    @Bean
    SubClassOfMatcherFactory subClassOfMatcherFactory(Provider<ClassHierarchyProvider> p1) {
        return new SubClassOfMatcherFactory(p1);
    }

    @Bean
    LeafClassMatcherFactory leafClassMatcherFactory(Provider<ClassHierarchyProvider> p1) {
        return new LeafClassMatcherFactory(p1);
    }

    @Bean
    NotSubClassOfMatcherFactory notSubClassOfMatcherFactory(Provider<ClassHierarchyProvider> p1) {
        return new NotSubClassOfMatcherFactory(p1);
    }

    @Bean
    InstanceOfMatcherFactory instanceOfMatcherFactory(Provider<ClassHierarchyProvider> p1,
                                                      Provider<ProjectOntologiesIndex> p2,
                                                      Provider<ClassAssertionAxiomsByClassIndex> p3,
                                                      Provider<ProjectSignatureByTypeIndex> p4) {
        return new InstanceOfMatcherFactory(p1, p2, p3, p4);
    }

    @Bean
    ConflictingBooleanValuesMatcherFactory conflictingBooleanValuesMatcherFactory(Provider<AnnotationAssertionAxiomsIndex> p1) {
        return new ConflictingBooleanValuesMatcherFactory(p1);
    }

    @Bean
    EntityIsDeprecatedMatcherFactory entityIsDeprecatedMatcherFactory(Provider<AnnotationAssertionAxiomsIndex> p1) {
        return new EntityIsDeprecatedMatcherFactory(p1);
    }

    @Bean
    AnnotationValuesAreNotDisjointMatcherFactory annotationValuesAreNotDisjointMatcherFactory(Provider<AnnotationAssertionAxiomsIndex> p1) {
        return new AnnotationValuesAreNotDisjointMatcherFactory(p1);
    }

    @Bean
    NonUniqueLangTagsMatcherFactory nonUniqueLangTagsMatcherFactory(Provider<AnnotationAssertionAxiomsIndex> p1) {
        return new NonUniqueLangTagsMatcherFactory(p1);
    }

    @Bean
    EntityAnnotationMatcherFactory entityAnnotationMatcherFactory(Provider<AnnotationAssertionAxiomsIndex> p1) {
        return new EntityAnnotationMatcherFactory(p1);
    }

    @Bean
    IriAnnotationsMatcherFactory iriAnnotationsMatcherFactory(Provider<AnnotationAssertionAxiomsIndex> p1) {
        return new IriAnnotationsMatcherFactory(p1);
    }

    @Bean
    EntityRelationshipMatcherFactory entityRelationshipMatcherFactory(Provider<ProjectOntologiesIndex> p1,
                                                                      Provider<SubClassOfAxiomsBySubClassIndex> p2,
                                                                      Provider<PropertyAssertionAxiomsBySubjectIndex> p3,
                                                                      Provider<AxiomPropertyValueTranslator> p4) {
        return new EntityRelationshipMatcherFactory(p1, p2, p3, p4);
    }

    @Bean
    MatcherFactory relationshipMatcherFactory(SubClassOfMatcherFactory p1,
                                              LeafClassMatcherFactory p2,
                                              NotSubClassOfMatcherFactory p3,
                                              InstanceOfMatcherFactory p4,
                                              ConflictingBooleanValuesMatcherFactory p5,
                                              EntityIsDeprecatedMatcherFactory p6,
                                              AnnotationValuesAreNotDisjointMatcherFactory p7,
                                              NonUniqueLangTagsMatcherFactory p8,
                                              EntityAnnotationMatcherFactory p9,
                                              IriAnnotationsMatcherFactory p10,
                                              EntityRelationshipMatcherFactory p11) {
        return new MatcherFactory(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11);
    }
}
