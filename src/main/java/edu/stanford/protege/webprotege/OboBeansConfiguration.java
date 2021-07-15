package edu.stanford.protege.webprotege;

import edu.stanford.protege.webprotege.index.*;
import edu.stanford.protege.webprotege.msg.MessageFormatter;
import edu.stanford.protege.webprotege.obo.*;
import edu.stanford.protege.webprotege.project.DefaultOntologyIdManager;
import edu.stanford.protege.webprotege.project.chg.ChangeManager;
import edu.stanford.protege.webprotege.renderer.RenderingManager;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.springframework.context.annotation.Bean;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-07-14
 */
public class OboBeansConfiguration {

    @Bean
    OboUtil oboUtil(OWLDataFactory p1,
                    AnnotationAssertionAxiomsBySubjectIndex p2,
                    ProjectOntologiesIndex p3,
                    DefaultOntologyIdManager p4) {
        return new OboUtil(p1, p2, p3, p4);
    }


    @Bean
    XRefConverter xRefConverter() {
        return new XRefConverter();
    }

    @Bean
    XRefExtractor xRefExtractor(AnnotationToXRefConverter p1, ProjectAnnotationAssertionAxiomsBySubjectIndex p2) {
        return new XRefExtractor(p1, p2);
    }

    @Bean
    AnnotationToXRefConverter annotationToXRefConverter(XRefConverter p1, OWLDataFactory p2) {
        return new AnnotationToXRefConverter(p1, p2);
    }

    @Bean
    TermIdManager termIdManager(OboUtil p1, ChangeManager p2) {
        return new TermIdManager(p1, p2);
    }

    @Bean
    TermDefinitionManagerImpl termDefinitionManager(OWLDataFactory p1,
                                                    AnnotationToXRefConverter p2,
                                                    ChangeManager p3,
                                                    XRefExtractor p4,
                                                    RenderingManager p5,
                                                    ProjectOntologiesIndex p6,
                                                    AnnotationAssertionAxiomsBySubjectIndex p7,
                                                    DefaultOntologyIdManager p8, MessageFormatter p9) {
        return new TermDefinitionManagerImpl(p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }

    @Bean
    OBONamespaceCache oboNamespaceCache(OntologyAnnotationsIndex p1,
                                        DefaultOntologyIdManager p2,
                                        AxiomsByEntityReferenceIndex p3, ProjectOntologiesIndex p4, OWLDataFactory p5) {
        return new OBONamespaceCache(p1, p2, p3, p4, p5);
    }


    @Bean
    TermRelationshipsManager termRelationshipsManager(OWLDataFactory p1,
                                                      ProjectOntologiesIndex p2,
                                                      AnnotationAssertionAxiomsBySubjectIndex p3,
                                                      SubClassOfAxiomsBySubClassIndex p4,
                                                      RenderingManager p5,
                                                      ChangeManager p6, RelationshipConverter p7) {
        return new TermRelationshipsManager(p1, p2, p3, p4, p5, p6, p7);
    }

    @Bean
    RelationshipConverter relationshipConverter(OWLDataFactory p1, RenderingManager p2) {
        return new RelationshipConverter(p1, p2);
    }

    @Bean
    TermCrossProductsManager termCrossProductsManager(RenderingManager p1,
                                                      OWLDataFactory p2,
                                                      ChangeManager p3,
                                                      RelationshipConverter p4,
                                                      EquivalentClassesAxiomsIndex p5,
                                                      DefaultOntologyIdManager p6, ProjectOntologiesIndex p7) {
        return new TermCrossProductsManager(p1, p2, p3, p4, p5, p6, p7);
    }


    @Bean
    TermSynonymsManager termSynonymsManager(OWLDataFactory p1,
                                            AnnotationToXRefConverter p2,
                                            XRefExtractor p3,
                                            ChangeManager p4,
                                            ProjectOntologiesIndex p5,
                                            AnnotationAssertionAxiomsBySubjectIndex p6) {
        return new TermSynonymsManager(p1, p2, p3, p4, p5, p6);
    }

    @Bean
    TermXRefsManager termXRefsManager(ChangeManager p1,
                                      OWLDataFactory p2,
                                      XRefExtractor p3,
                                      AnnotationToXRefConverter p4,
                                      ProjectOntologiesIndex p5,
                                      AnnotationAssertionAxiomsBySubjectIndex p6) {
        return new TermXRefsManager(p1, p2, p3, p4, p5, p6);
    }

}
