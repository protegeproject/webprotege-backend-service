package edu.stanford.protege.webprotege;

import edu.stanford.protege.webprotege.common.LangTagFilter;
import edu.stanford.protege.webprotege.forms.*;
import edu.stanford.protege.webprotege.forms.data.*;
import edu.stanford.protege.webprotege.frame.FrameComponentRenderer;
import edu.stanford.protege.webprotege.frame.FrameComponentSessionRenderer;
import edu.stanford.protege.webprotege.index.EntitiesInProjectSignatureByIriIndex;
import edu.stanford.protege.webprotege.match.HierarchyPositionMatchingEngine;
import edu.stanford.protege.webprotege.match.LiteralMatcherFactory;
import edu.stanford.protege.webprotege.match.MatcherFactory;
import edu.stanford.protege.webprotege.match.MatchingEngine;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.springframework.context.annotation.Bean;

import javax.inject.Provider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-04-12
 */
public class FormDataBuilderConfiguration {

    @Bean
    FrameComponentSessionRenderer frameComponentSessionRenderer(FrameComponentRenderer p1) {
        return new FrameComponentSessionRenderer(p1);
    }

    @Bean
    FormDataBuilderSessionRenderer formDataBuilderSessionRenderer(FrameComponentSessionRenderer p1) {
        return new FormDataBuilderSessionRenderer(p1);
    }


    @Bean
    EntityFrameFormDataDtoBuilder entityFrameFormDataDtoBuilder(FormDataBuilderSessionRenderer p1,
                                                                TextControlValuesBuilder p2,
                                                                NumberControlValuesBuilder p3,
                                                                MultiChoiceControlValueBuilder p4,
                                                                SingleChoiceControlValuesBuilder p5,
                                                                EntityNameControlValuesBuilder p6,
                                                                ImageControlValuesBuilder p7,
                                                                GridControlValuesBuilder p8,
                                                                SubFormControlValuesBuilder p9,
                                                                LangTagFilter p10,
                                                                FormPageRequestIndex p11,
                                                                FormRegionFilterIndex p12,
                                                                FormDescriptorDtoTranslator p13) {
        return new EntityFrameFormDataDtoBuilder(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13);
    }

    @Bean
    EntityFrameFormDataDtoBuilderFactory entityFrameFormDataDtoBuilderFactory() {
        return new EntityFrameFormDataDtoBuilderFactoryImpl();
    }



    @Bean
    TextControlDataDtoComparator textControlDataDtoComparator() {
        return new TextControlDataDtoComparator();
    }

    @Bean
    TextControlValuesBuilder textControlValuesBuilder(BindingValuesExtractor p1, TextControlDataDtoComparator p2) {
        return new TextControlValuesBuilder(p1, p2);
    }

    @Bean
    NumberControlDataDtoComparator numberControlDataDtoComparator() {
        return new NumberControlDataDtoComparator();
    }

    @Bean
    NumberControlValuesBuilder numberControlValuesBuilder(BindingValuesExtractor p1, NumberControlDataDtoComparator p2) {
        return new NumberControlValuesBuilder(p1, p2);
    }

    @Bean
    PrimitiveFormControlDataDtoRenderer primitiveFormControlDataDtoRenderer(EntitiesInProjectSignatureByIriIndex p1,
                                                                            FormDataBuilderSessionRenderer p2) {
        return new PrimitiveFormControlDataDtoRenderer(p1, p2);
    }

    @Bean
    MultiChoiceControlDataDtoComparator multiChoiceControlDataDtoComparator() {
        return new MultiChoiceControlDataDtoComparator();
    }

    @Bean
    MultiChoiceControlValueBuilder multiChoiceControlValueBuilder(BindingValuesExtractor p1,
                                                                  PrimitiveFormControlDataDtoRenderer p2) {
        return new MultiChoiceControlValueBuilder(p1, p2);
    }

    @Bean
    SingleChoiceControlDataDtoComparator singleChoiceControlDataDtoComparator() {
        return new SingleChoiceControlDataDtoComparator();
    }

    @Bean
    SingleChoiceControlValuesBuilder singleChoiceControlValuesBuilder(BindingValuesExtractor p1,
                                                                      PrimitiveFormControlDataDtoRenderer p2,
                                                                      LangTagFilter p3) {
        return new SingleChoiceControlValuesBuilder(p1, p2, p3);
    }

    @Bean
    EntityNameControlDataDtoComparator entityNameControlDataDtoComparator() {
        return new EntityNameControlDataDtoComparator();
    }

    @Bean
    EntityNameControlValuesBuilder entityNameControlValuesBuilder(BindingValuesExtractor p1,
                                                                  EntitiesInProjectSignatureByIriIndex p2,
                                                                  FormDataBuilderSessionRenderer p3,
                                                                  EntityNameControlDataDtoComparator p4) {
        return new EntityNameControlValuesBuilder(p1, p2, p3, p4);
    }

    @Bean
    ImageControlDataDtoComparator imageControlDataDtoComparator() {
        return new ImageControlDataDtoComparator();
    }

    @Bean
    ImageControlValuesBuilder imageControlValuesBuilder(BindingValuesExtractor p1, ImageControlDataDtoComparator p2) {
        return new ImageControlValuesBuilder(p1, p2);
    }

    @Bean
    GridControlValuesBuilder gridControlValuesBuilder(BindingValuesExtractor p1,
                                                      Provider<EntityFrameFormDataDtoBuilder> p2,
                                                      FormDataBuilderSessionRenderer p3, FormRegionOrderingIndex p4,
                                                      LangTagFilter p5, FormPageRequestIndex p6,
                                                      GridRowDataDtoComparatorFactory p7,
                                                      FormControlDataDtoComparator p8, FormRegionFilterIndex p9,
                                                      FormFilterMatcherFactory p10,
                                                      FormRegionFilterPredicateManager p11,
                                                      OWLDataFactory p12) {
        return new GridControlValuesBuilder(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12);
    }

    @Bean
    GridCellDataDtoComparator gridCellDataDtoComparator(FormControlDataDtoComparator p1) {
        return new GridCellDataDtoComparator(p1);
    }

    @Bean
    GridRowDataDtoComparatorFactory gridRowDataDtoComparatorFactory(GridCellDataDtoComparator p1,
                                                                    FormRegionOrderingIndex p2) {
        return new GridRowDataDtoComparatorFactory(p1, p2);
    }

    @Bean
    SubFormControlValuesBuilder subFormControlValuesBuilder(BindingValuesExtractor p1,
                                                            Provider<EntityFrameFormDataDtoBuilder> p2,
                                                            FormSubjectTranslator p3) {
        return new SubFormControlValuesBuilder(p1, p2, p3);
    }

    @Bean
    FormControlDataDtoComparator formControlDataDtoComparator(Provider<GridControlDataDtoComparator> p1,
                                                              EntityNameControlDataDtoComparator p2,
                                                              ImageControlDataDtoComparator p3,
                                                              MultiChoiceControlDataDtoComparator p4,
                                                              SingleChoiceControlDataDtoComparator p5,
                                                              Provider<FormDataDtoComparator> p6,
                                                              NumberControlDataDtoComparator p7,
                                                              TextControlDataDtoComparator p8) {
        return new FormControlDataDtoComparator(p1, p2, p3, p4, p5, p6, p7, p8);
    }

    @Bean
    FormFilterMatcherFactory formFilterMatcherFactory(LiteralMatcherFactory p1, MatcherFactory p2) {
        return new FormFilterMatcherFactory(p1, p2);
    }


    @Bean
    FormRegionFilterPredicateManager formRegionFilterPredicateManager(FormFilterMatcherFactory p1,
                                                                      FormRegionFilterIndex p2) {
        return new FormRegionFilterPredicateManager(p1, p2);
    }



    @Bean
    FormDescriptorDtoTranslator formDescriptorDtoTranslator(ChoiceDescriptorCache p1) {
        return new FormDescriptorDtoTranslator(p1);
    }

    @Bean
    ChoiceDescriptorCache choiceDescriptorCache(ChoiceDescriptorDtoSupplier p1) {
        return new ChoiceDescriptorCache(p1);
    }

    @Bean
    ChoiceDescriptorDtoSupplier choiceDescriptorDtoSupplier(FixedListChoiceDescriptorDtoSupplier p1,
                                                            DynamicListChoiceDescriptorDtoSupplier p2,
                                                            FormDataBuilderSessionRenderer p3) {
        return new ChoiceDescriptorDtoSupplier(p1, p2, p3);
    }

    @Bean
    FixedListChoiceDescriptorDtoSupplier fixedListChoiceDescriptorDtoSupplier(PrimitiveFormControlDataDtoRenderer p1) {
        return new FixedListChoiceDescriptorDtoSupplier(p1);
    }

    @Bean
    DynamicListChoiceDescriptorDtoSupplier dynamicListChoiceDescriptorDtoSupplier(MatchingEngine p1,
                                                                                  HierarchyPositionMatchingEngine p2,
                                                                                  FormDataBuilderSessionRenderer p3) {
        return new DynamicListChoiceDescriptorDtoSupplier(p1, p2, p3);
    }
}
