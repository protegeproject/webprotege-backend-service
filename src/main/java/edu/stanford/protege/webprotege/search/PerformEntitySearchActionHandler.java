package edu.stanford.protege.webprotege.search;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import edu.stanford.protege.webprotege.lang.LanguageManager;
import edu.stanford.protege.webprotege.pagination.Page;
import edu.stanford.protege.webprotege.pagination.PageRequest;
import edu.stanford.protege.webprotege.shortform.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static edu.stanford.protege.webprotege.shortform.DictionaryLanguageFilter.EmptyLangTagTreatment.INCLUDE_EMPTY_LANG_TAGS;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Apr 2017
 */
public class PerformEntitySearchActionHandler extends AbstractProjectActionHandler<PerformEntitySearchAction, PerformEntitySearchResult> {

    @Nonnull
    private final EntitySearcherFactory entitySearcherFactory;

    @Nonnull
    private final LanguageManager languageManager;

    @Inject
    public PerformEntitySearchActionHandler(@Nonnull AccessManager accessManager,
                                            @Nonnull EntitySearcherFactory entitySearcherFactory,
                                            @Nonnull LanguageManager languageManager) {
        super(accessManager);
        this.entitySearcherFactory = entitySearcherFactory;
        this.languageManager = languageManager;
    }

    @Nonnull
    @Override
    public Class<PerformEntitySearchAction> getActionClass() {
        return PerformEntitySearchAction.class;
    }

    @Nonnull
    @Override
    public PerformEntitySearchResult execute(@Nonnull PerformEntitySearchAction action,
                                             @Nonnull ExecutionContext executionContext) {
        var entityTypes = action.getEntityTypes();
        var searchString = action.getSearchString();
        var languages = ImmutableList.<DictionaryLanguage>builder();
        var langTagFilter = action.getLangTagFilter();
        var dictionaryLanguageFilter = DictionaryLanguageFilter.get(langTagFilter, INCLUDE_EMPTY_LANG_TAGS);
        languageManager.getLanguages().stream().filter(dictionaryLanguageFilter::isIncluded).forEach(languages::add);
        languages.add(OboIdDictionaryLanguage.get());
        languages.add(LocalNameDictionaryLanguage.get());
        languages.add(PrefixedNameDictionaryLanguage.get());

        var searchFilters = action.getSearchFilters();

        var entitySearcher = entitySearcherFactory.create(entityTypes,
                                                          searchString,
                                                          executionContext.getUserId(),
                                                          languages.build(),
                                                          searchFilters);
        PageRequest pageRequest = action.getPageRequest();
        entitySearcher.setPageRequest(pageRequest);
        entitySearcher.invoke();

        Page<EntitySearchResult> results = entitySearcher.getResults();
        return PerformEntitySearchResult.create(searchString, results);
    }
}

