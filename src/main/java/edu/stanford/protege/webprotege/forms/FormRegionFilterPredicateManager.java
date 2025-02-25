package edu.stanford.protege.webprotege.forms;

import edu.stanford.protege.webprotege.forms.data.FormControlDataDto;
import edu.stanford.protege.webprotege.forms.data.FormRegionFilter;
import edu.stanford.protege.webprotege.forms.field.FormRegionId;
import edu.stanford.protege.webprotege.match.Matcher;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-06-23
 */
@FormDataBuilderSession
public class FormRegionFilterPredicateManager {

    @Nonnull
    private final FormFilterMatcherFactory matcherFactory;

    @Nonnull
    private final FormRegionFilterIndex formRegionFilterIndex;

    @Nonnull
    private final Map<FormRegionId, Predicate<FormControlDataDto>> predicateMap = new HashMap<>();

    @Inject
    public FormRegionFilterPredicateManager(@Nonnull FormFilterMatcherFactory matcherFactory,
                                            @Nonnull FormRegionFilterIndex formRegionFilterIndex) {
        this.matcherFactory = checkNotNull(matcherFactory);
        this.formRegionFilterIndex = checkNotNull(formRegionFilterIndex);
    }

    @Nonnull
    public Predicate<FormControlDataDto> getFilterPredicate(@Nonnull FormRegionId formRegionId) {
        var predicate = predicateMap.get(formRegionId);
        if(predicate == null) {
            predicate = loadMatchingPredicate(formRegionId);
            predicateMap.put(formRegionId, predicate);
        }
        return predicate;
    }

    private Predicate<FormControlDataDto> loadMatchingPredicate(@Nonnull FormRegionId formRegionId) {
        return formRegionFilterIndex.getFilters()
                .stream()
                .filter(f -> isForFormRegionId(formRegionId, f))
                .findAny()
                .map(matcherFactory::getMatcher)
                .map(this::toPredicate)
                .orElse(v -> true);
    }

    public boolean isForFormRegionId(@Nonnull FormRegionId formRegionId, FormRegionFilter f) {
        return f.getFormRegionId()
                .equals(formRegionId);
    }

    private Predicate<FormControlDataDto> toPredicate(Matcher<FormControlDataDto> matcher) {
        return matcher::matches;
    }

    public boolean isFiltered(FormRegionId formRegionId) {
        return formRegionFilterIndex.hasFilter(formRegionId);
    }
}
