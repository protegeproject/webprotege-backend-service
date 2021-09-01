package edu.stanford.protege.webprotege;

import edu.stanford.protege.webprotege.change.ReverseEngineeredChangeDescriptionGeneratorFactory;
import edu.stanford.protege.webprotege.crud.DeleteEntitiesChangeListGeneratorFactory;
import edu.stanford.protege.webprotege.entity.CreateEntityFromFormDataChangeListGeneratorFactory;
import edu.stanford.protege.webprotege.entity.EntityRenamer;
import edu.stanford.protege.webprotege.forms.*;
import edu.stanford.protege.webprotege.forms.processor.*;
import edu.stanford.protege.webprotege.frame.EmptyEntityFrameFactory;
import edu.stanford.protege.webprotege.frame.FrameChangeGeneratorFactory;
import edu.stanford.protege.webprotege.index.*;
import edu.stanford.protege.webprotege.inject.ProjectComponent;
import edu.stanford.protege.webprotege.match.MatchingEngine;
import edu.stanford.protege.webprotege.msg.MessageFormatter;
import edu.stanford.protege.webprotege.project.DefaultOntologyIdManager;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.renderer.ContextRenderer;
import edu.stanford.protege.webprotege.renderer.RenderingManager;
import edu.stanford.protege.webprotege.util.EntityDeleter;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntityByTypeProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import javax.inject.Provider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-07-14
 */
public class FormBeansConfiguration {

    @Bean
    EntityFormChangeListGeneratorFactory entityFormChangeListGeneratorFactory(FormDataConverter p1,
                                                                              ReverseEngineeredChangeDescriptionGeneratorFactory p2,
                                                                              MessageFormatter p3,
                                                                              FrameChangeGeneratorFactory p4,
                                                                              FormFrameConverter p5,
                                                                              EmptyEntityFrameFactory p6,
                                                                              RenderingManager p7,
                                                                              OWLDataFactory p8,
                                                                              DefaultOntologyIdManager p9,
                                                                              DeleteEntitiesChangeListGeneratorFactory p10) {
        return new EntityFormChangeListGeneratorFactory(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10);
    }

    @Bean
    @Scope("prototype")
    FormFrameConverter formFrameConverter() {
        return new FormFrameConverter();
    }

    @Bean
    @Scope("prototype")
    FormDataConverter formDataConverter(FormSubjectResolver p1, FormDataProcessor p2) {
        return new FormDataConverter(p1, p2);
    }

    @Bean
    @Scope("prototype")
    FormSubjectResolver formSubjectResolver(EntityFormSubjectFactory p1) {
        return new FormSubjectResolver(p1);
    }

    @Bean
    @Scope("prototype")
    EntityFormSubjectFactory entityFormSubjectFactory(OWLEntityByTypeProvider p1) {
        return new EntityFormSubjectFactory(p1);
    }

    @Bean
    @Scope("prototype")
    FormDataProcessor formDataProcessor(Provider<FormFrameBuilder> p1, FormFieldProcessor p2) {
        return new FormDataProcessor(p1, p2);
    }

    @Bean
    @Scope("prototype")
    FormFieldProcessor formFieldProcessor(FormControlDataProcessor p1) {
        return new FormFieldProcessor(p1);
    }

    @Bean
    @Scope("prototype")
    FormControlDataProcessor formControlDataProcessor(GridControlDataProcessor p1, Provider<FormDataProcessor> p2) {
        return new FormControlDataProcessor(p1, p2);
    }

    @Bean
    @Scope("prototype")
    GridControlDataProcessor gridControlDataProcessor(GridRowDataProcessor p1) {
        return new GridControlDataProcessor(p1);
    }

    @Bean
    @Scope("prototype")
    GridRowDataProcessor gridRowDataProcessor(Provider<FormFrameBuilder> p1, GridCellDataProcessor p2) {
        return new GridRowDataProcessor(p1, p2);
    }

    @Bean
    @Scope("prototype")
    GridCellDataProcessor gridCellDataProcessor(Provider<FormControlDataProcessor> p1) {
        return new GridCellDataProcessor(p1);
    }

    @Bean
    @Scope("prototype")
    FormFrameBuilder formFrameBuilder() {
        return new FormFrameBuilder();
    }

    @Bean
    @Scope("prototype")
    EmptyEntityFrameFactory emptyEntityFrameFactory(ContextRenderer p1) {
        return new EmptyEntityFrameFactory(p1);
    }

    @Bean
    EntityFormManager entityFormManager(EntityFormRepository p1, EntityFormSelectorRepository p2, MatchingEngine p3) {
        return new EntityFormManager(p1, p2, p3);
    }

    @Bean
    FormsCopierFactory formsCopierFactory(Provider<EntityFormRepository> p1, Provider<EntityFormSelectorRepository> p2) {
        return new FormsCopierFactory(p1, p2);
    }

    @Bean
    CreateEntityFromFormDataChangeListGeneratorFactory createEntityFromFormDataChangeListGeneratorFactory(Provider<EntityFormChangeListGeneratorFactory> p1,
                                                                                                          Provider<OWLDataFactory> p2,
                                                                                                          Provider<DefaultOntologyIdManager> p3,
                                                                                                          Provider<RenderingManager> p4) {
        return new CreateEntityFromFormDataChangeListGeneratorFactory(p1, p2, p3, p4);
    }

    @Bean
    DeprecateEntityByFormChangeListGeneratorFactory deprecateEntityByFormChangeListGeneratorFactory(Provider<EntityDeleter> p1,
                                                                                                    Provider<EntityFormChangeListGeneratorFactory> p2,
                                                                                                    Provider<EntityFormManager> p3,
                                                                                                    Provider<MessageFormatter> p4,
                                                                                                    Provider<ProjectId> p5,
                                                                                                    Provider<ProjectComponent> p6,
                                                                                                    Provider<EntityRenamer> p7,
                                                                                                    Provider<DefaultOntologyIdManager> p8,
                                                                                                    Provider<OWLDataFactory> p9,
                                                                                                    Provider<ProjectOntologiesIndex> p10,
                                                                                                    Provider<SubClassOfAxiomsBySubClassIndex> p11,
                                                                                                    Provider<SubObjectPropertyAxiomsBySubPropertyIndex> p12,
                                                                                                    Provider<SubDataPropertyAxiomsBySubPropertyIndex> p13,
                                                                                                    Provider<SubAnnotationPropertyAxiomsBySubPropertyIndex> p14,
                                                                                                    Provider<ClassAssertionAxiomsByIndividualIndex> p15) {
        return new DeprecateEntityByFormChangeListGeneratorFactory(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15);
    }
}
