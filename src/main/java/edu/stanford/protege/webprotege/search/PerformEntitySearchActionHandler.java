package edu.stanford.protege.webprotege.search;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.common.DictionaryLanguage;
import edu.stanford.protege.webprotege.common.DictionaryLanguageFilter;
import edu.stanford.protege.webprotege.common.LocalNameDictionaryLanguage;
import edu.stanford.protege.webprotege.common.OboIdDictionaryLanguage;
import edu.stanford.protege.webprotege.common.Page;
import edu.stanford.protege.webprotege.common.PageRequest;
import edu.stanford.protege.webprotege.common.PrefixedNameDictionaryLanguage;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.lang.LanguageManager;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static edu.stanford.protege.webprotege.common.DictionaryLanguageFilter.EmptyLangTagTreatment.INCLUDE_EMPTY_LANG_TAGS;

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
        var entityTypes = action.entityTypes();
        var searchString = action.searchString();
        var languages = ImmutableList.<DictionaryLanguage>builder();
        var langTagFilter = action.langTagFilter();
        var dictionaryLanguageFilter = DictionaryLanguageFilter.get(langTagFilter, INCLUDE_EMPTY_LANG_TAGS);
        languageManager.getLanguages().stream().filter(dictionaryLanguageFilter::isIncluded).forEach(languages::add);
        languages.add(OboIdDictionaryLanguage.get());
        languages.add(LocalNameDictionaryLanguage.get());
        languages.add(PrefixedNameDictionaryLanguage.get());

        var searchFilters = action.searchFilters();

        var entitySearcher = entitySearcherFactory.create(entityTypes,
                                                          searchString,
                                                          executionContext.userId(),
                                                          languages.build(),
                                                          searchFilters);
        PageRequest pageRequest = action.pageRequest();
        entitySearcher.setPageRequest(pageRequest);
        entitySearcher.invoke();

        Page<EntitySearchResult> results = entitySearcher.getResults();
        return new PerformEntitySearchResult(searchString, results);
    }
}

