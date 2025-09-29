package edu.stanford.protege.webprotege.icd.actions;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.common.*;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.lang.LanguageManager;
import edu.stanford.protege.webprotege.search.EntitySearchResult;
import edu.stanford.protege.webprotege.search.EntitySearcherFactory;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static edu.stanford.protege.webprotege.common.DictionaryLanguageFilter.EmptyLangTagTreatment.INCLUDE_EMPTY_LANG_TAGS;

public class GetExistingClassesForApiActionHandler extends AbstractProjectActionHandler<GetExistingClassesForApiAction, GetExistingClassesForApiResult> {

    private final EntitySearcherFactory entitySearcherFactory;

    private final LanguageManager languageManager;

    public GetExistingClassesForApiActionHandler(@NotNull AccessManager accessManager, EntitySearcherFactory entitySearcherFactory, LanguageManager languageManager) {
        super(accessManager);
        this.entitySearcherFactory = entitySearcherFactory;
        this.languageManager = languageManager;
    }

    @NotNull
    @Override
    public Class<GetExistingClassesForApiAction> getActionClass() {
        return GetExistingClassesForApiAction.class;
    }

    @NotNull
    @Override
    public GetExistingClassesForApiResult execute(@NotNull GetExistingClassesForApiAction action, @NotNull ExecutionContext executionContext) {

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
                searchFilters,
                action.resultsSetFilter(), action.deprecatedEntitiesTreatment());
        PageRequest pageRequest = action.pageRequest();
        entitySearcher.setPageRequest(pageRequest);
        entitySearcher.invoke();

        Page<EntitySearchResult> results = entitySearcher.getResults();
        List<GetExistingClassesForApiResult.ExistingClasses> existingClasses = entitySearcher.getResults().getPageElements()
                .stream()
                .flatMap(element -> element.getMatches().stream())
                .map(match -> new GetExistingClassesForApiResult.ExistingClasses(match.getEntity().getBrowserText(), match.getEntity().getEntity().getIRI()))
                .toList();
        return new GetExistingClassesForApiResult(Page.create(results.getPageNumber(), results.getPageCount(), existingClasses, results.getTotalElements()));
    }
}
