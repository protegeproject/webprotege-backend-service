package edu.stanford.protege.webprotege;

import edu.stanford.protege.webprotege.forms.FormRegionFilterIndex;
import edu.stanford.protege.webprotege.frame.translator.AxiomPropertyValueTranslator;
import edu.stanford.protege.webprotege.hierarchy.ClassHierarchyProvider;
import edu.stanford.protege.webprotege.index.*;
import edu.stanford.protege.webprotege.match.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import jakarta.inject.Provider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-07-13
 */
public class EntityMatcherBeansConfiguration {

    @Bean
    SubClassOfMatcherFactory subClassOfMatcherFactory(ClassHierarchyProvider p1) {
        return new SubClassOfMatcherFactory(p1);
    }

    @Bean
    LeafClassMatcherFactory leafClassMatcherFactory(ClassHierarchyProvider p1) {
        return new LeafClassMatcherFactory(p1);
    }

    @Bean
    NotSubClassOfMatcherFactory notSubClassOfMatcherFactory(ClassHierarchyProvider p1) {
        return new NotSubClassOfMatcherFactory(p1);
    }

    @Bean
    InstanceOfMatcherFactory instanceOfMatcherFactory(ClassHierarchyProvider p1,
                                                      ProjectOntologiesIndex p2,
                                                      ClassAssertionAxiomsByClassIndex p3,
                                                      ProjectSignatureByTypeIndex p4) {
        return new InstanceOfMatcherFactory(p1, p2, p3, p4);
    }

    @Bean
    ConflictingBooleanValuesMatcherFactory conflictingBooleanValuesMatcherFactory(AnnotationAssertionAxiomsIndex p1) {
        return new ConflictingBooleanValuesMatcherFactory(p1);
    }

    @Bean
    EntityIsDeprecatedMatcherFactory entityIsDeprecatedMatcherFactory(AnnotationAssertionAxiomsIndex p1) {
        return new EntityIsDeprecatedMatcherFactory(p1);
    }

    @Bean
    AnnotationValuesAreNotDisjointMatcherFactory annotationValuesAreNotDisjointMatcherFactory(AnnotationAssertionAxiomsIndex p1) {
        return new AnnotationValuesAreNotDisjointMatcherFactory(p1);
    }

    @Bean
    NonUniqueLangTagsMatcherFactory nonUniqueLangTagsMatcherFactory(AnnotationAssertionAxiomsIndex p1) {
        return new NonUniqueLangTagsMatcherFactory(p1);
    }

    @Bean
    EntityAnnotationMatcherFactory entityAnnotationMatcherFactory(AnnotationAssertionAxiomsIndex p1) {
        return new EntityAnnotationMatcherFactory(p1);
    }

    @Bean
    IriAnnotationsMatcherFactory iriAnnotationsMatcherFactory(AnnotationAssertionAxiomsIndex p1) {
        return new IriAnnotationsMatcherFactory(p1);
    }

    @Bean
    EntityRelationshipMatcherFactory entityRelationshipMatcherFactory(ProjectOntologiesIndex p1,
                                                                      SubClassOfAxiomsBySubClassIndex p2,
                                                                      PropertyAssertionAxiomsBySubjectIndex p3,
                                                                      AxiomPropertyValueTranslator p4) {
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

    @Bean
    LiteralMatcherFactory literalMatcherFactory() {
        return new LiteralMatcherFactory();
    }
}
