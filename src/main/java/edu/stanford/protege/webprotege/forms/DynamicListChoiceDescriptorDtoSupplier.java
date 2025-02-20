package edu.stanford.protege.webprotege.forms;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import edu.stanford.protege.webprotege.common.*;
import edu.stanford.protege.webprotege.entity.OWLEntityData;
import edu.stanford.protege.webprotege.forms.data.PrimitiveFormControlDataDto;
import edu.stanford.protege.webprotege.forms.field.ChoiceDescriptorDto;
import edu.stanford.protege.webprotege.forms.field.DynamicChoiceListSourceDescriptor;
import edu.stanford.protege.webprotege.match.HierarchyPositionMatchingEngine;
import edu.stanford.protege.webprotege.match.MatchingEngine;
import edu.stanford.protege.webprotege.criteria.HierarchyPositionCriteria;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

@FormDataBuilderSession
public class DynamicListChoiceDescriptorDtoSupplier {

    public static final int CHOICE_LIMIT = 1000;

    @Nonnull
    private final MatchingEngine matchingEngine;

    @Nonnull
    private final HierarchyPositionMatchingEngine hierarchyPositionMatchingEngine;

    @Nonnull
    private final FormDataBuilderSessionRenderer sessionRenderer;

    @Inject
    public DynamicListChoiceDescriptorDtoSupplier(@Nonnull MatchingEngine matchingEngine,
                                                  @Nonnull HierarchyPositionMatchingEngine hierarchyPositionMatchingEngine,
                                                  @Nonnull FormDataBuilderSessionRenderer sessionRenderer) {
        this.matchingEngine = checkNotNull(matchingEngine);
        this.hierarchyPositionMatchingEngine = checkNotNull(hierarchyPositionMatchingEngine);
        this.sessionRenderer = checkNotNull(sessionRenderer);
    }

    @Nonnull
    public ImmutableList<ChoiceDescriptorDto> getChoices(@Nonnull DynamicChoiceListSourceDescriptor descriptor) {
        var matchCriteria = descriptor.getCriteria();
        // Optimization here, we don't necessarily need to iterate over all entities
        // Use any hierarchy position query to filter the list down to something this is (likely) small
        return descriptor.getCriteria()
                  .asCompositeRootCriteria()
                  .getRootCriteria()
                  .stream()
                  .filter(c -> c instanceof HierarchyPositionCriteria)
                  .map(c -> (HierarchyPositionCriteria) c)
                  .flatMap(hierarchyPositionMatchingEngine::getMatchingEntities)
                  .filter(entity -> matchingEngine.matches(entity, matchCriteria))
                  .map(sessionRenderer::getEntityRendering)
                  .sorted()
                  .limit(CHOICE_LIMIT)
                  .map(this::toChoiceDescriptorDto)
                  .collect(toImmutableList());
    }

    private ChoiceDescriptorDto toChoiceDescriptorDto(OWLEntityData entityData) {
        var shortForms = entityData.getShortFormsMap();
        var dto = PrimitiveFormControlDataDto.get(entityData);
        var languageMap = fromDictionaryMap(shortForms);
        return ChoiceDescriptorDto.get(dto, languageMap);
    }

    public static LanguageMap fromDictionaryMap(@Nonnull Map<DictionaryLanguage, String> dictionaryMap) {
        Map<String, String> langMap = new HashMap<>();
        dictionaryMap.forEach((dict, value) -> {
            dict.accept(new DictionaryLanguageVisitor<String>() {
                @Override
                public String visit(@Nonnull AnnotationAssertionDictionaryLanguage language) {
                    langMap.putIfAbsent(language.getLang(), value);
                    return null;
                }

                @Override
                public String visit(@Nonnull AnnotationAssertionPathDictionaryLanguage language) {
                    langMap.putIfAbsent(language.getLang(), value);
                    return null;
                }

                @Override
                public String visit(@Nonnull LocalNameDictionaryLanguage language) {
                    langMap.putIfAbsent("", value);
                    return null;
                }
            });
        });
        return LanguageMap.get(ImmutableMap.copyOf(langMap));
    }

}
