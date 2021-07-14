package edu.stanford.protege.webprotege.form;

import edu.stanford.protege.webprotege.entity.OWLEntityData;
import edu.stanford.protege.webprotege.form.data.*;
import edu.stanford.protege.webprotege.match.LiteralMatcherFactory;
import edu.stanford.protege.webprotege.match.Matcher;
import edu.stanford.protege.webprotege.match.MatcherFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-06-19
 */
public class FormFilterMatcherFactory {

    @Nonnull
    private final LiteralMatcherFactory literalMatcherFactory;

    @Nonnull
    private final MatcherFactory matcherFactory;

    @Inject
    public FormFilterMatcherFactory(@Nonnull LiteralMatcherFactory literalMatcherFactory,
                                    @Nonnull MatcherFactory matcherFactory) {
        this.literalMatcherFactory = literalMatcherFactory;
        this.matcherFactory = matcherFactory;
    }

    public Matcher<FormControlDataDto> getMatcher(@Nonnull FormRegionFilter formRegionFilter) {
        var matchCriteria = formRegionFilter.getMatchCriteria();
        return getMatcher(matchCriteria);
    }

    public Matcher<FormControlDataDto> getMatcher(PrimitiveFormControlDataMatchCriteria matchCriteria) {
        return matchCriteria.accept(new PrimitiveFormControlDataMatchCriteriaVisitor<>() {
            @Override
            public Matcher<FormControlDataDto> visit(EntityFormControlDataMatchCriteria criteria) {
                var entityMatcher = matcherFactory.getMatcher(criteria.getEntityMatchCriteria());

                return data -> {
                    return data.accept(new FormControlDataDtoVisitorEx<Boolean>() {
                        @Override
                        public Boolean visit(@Nonnull EntityNameControlDataDto entityNameControlData) {
                            return entityNameControlData.getEntity()
                                                        .map(OWLEntityData::getEntity)
                                                        .map(entityMatcher::matches)
                                                        .orElse(false);
                        }

                        @Override
                        public Boolean visit(@Nonnull FormDataDto formData) {
                            return false;
                        }

                        @Override
                        public Boolean visit(@Nonnull GridControlDataDto gridControlData) {
                            return false;
                        }

                        @Override
                        public Boolean visit(@Nonnull ImageControlDataDto imageControlData) {
                            return false;
                        }

                        @Override
                        public Boolean visit(@Nonnull MultiChoiceControlDataDto multiChoiceControlData) {
                            return false;
                        }

                        @Override
                        public Boolean visit(@Nonnull SingleChoiceControlDataDto singleChoiceControlData) {
                            return singleChoiceControlData.getChoice()
                                                          .map(PrimitiveFormControlDataDto::toPrimitiveFormControlData)
                                                          .flatMap(PrimitiveFormControlData::asEntity)
                                                          .map(entityMatcher::matches)
                                                          .orElse(false);
                        }

                        @Override
                        public Boolean visit(@Nonnull NumberControlDataDto numberControlData) {
                            return false;
                        }

                        @Override
                        public Boolean visit(@Nonnull TextControlDataDto textControlData) {
                            return false;
                        }
                    });
                };
            }

            @Override
            public Matcher<FormControlDataDto> visit(LiteralFormControlDataMatchCriteria literalFormControlDataMatchCriteria) {
                var literalCriteria = literalFormControlDataMatchCriteria.getLexicalValueCriteria();
                var literalMatcher = literalMatcherFactory.getMatcher(literalCriteria);
                return data -> data.accept(new FormControlDataDtoVisitorEx<Boolean>() {
                    @Override
                    public Boolean visit(@Nonnull EntityNameControlDataDto entityNameControlData) {
                        return false;
                    }

                    @Override
                    public Boolean visit(@Nonnull FormDataDto formData) {
                        return false;
                    }

                    @Override
                    public Boolean visit(@Nonnull GridControlDataDto gridControlData) {
                        return false;
                    }

                    @Override
                    public Boolean visit(@Nonnull ImageControlDataDto imageControlData) {
                        return false;
                    }

                    @Override
                    public Boolean visit(@Nonnull MultiChoiceControlDataDto multiChoiceControlData) {
                        return false;
                    }

                    @Override
                    public Boolean visit(@Nonnull SingleChoiceControlDataDto singleChoiceControlData) {
                        return false;
                    }

                    @Override
                    public Boolean visit(@Nonnull NumberControlDataDto numberControlData) {
                        return numberControlData.getValue().map(literalMatcher::matches).orElse(Boolean.FALSE);
                    }

                    @Override
                    public Boolean visit(@Nonnull TextControlDataDto textControlData) {
                        return textControlData.getValue().map(literalMatcher::matches).orElse(Boolean.TRUE);
                    }
                });
            }

            @Override
            public Matcher<FormControlDataDto> visit(CompositePrimitiveFormControlDataMatchCriteria compositeCriteria) {
                return m -> true;
            }
        });
    }
}
