package edu.stanford.protege.webprotege.search;



import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import edu.stanford.protege.webprotege.DataFactory;
import edu.stanford.protege.webprotege.common.*;
import edu.stanford.protege.webprotege.criteria.EntityMatchCriteria;
import edu.stanford.protege.webprotege.entity.EntityNodeRenderer;
import edu.stanford.protege.webprotege.match.MatcherFactory;
import edu.stanford.protege.webprotege.shortform.*;
import edu.stanford.protege.webprotege.shortform.SearchString;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.IRI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Apr 2017
 * <p>
 * Performs searches against a stream of entities.
 * Instances of this class are not thread safe.
 */
public class EntitySearcher {

    /**
     * The default limit for the returned results.
     */
    public static final int DEFAULT_LIMIT = 50;

    private static final Pattern OBO_ID_PATTERN = Pattern.compile("([a-z]|[A-Z]+)_([0-9]+)");

    private static final Logger logger = LoggerFactory.getLogger(EntitySearcher.class);

    private final LocalNameExtractor localNameExtractor = new LocalNameExtractor();

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final UserId userId;

    @Nonnull
    private final DictionaryManager dictionaryManager;

    @Nonnull
    private final Set<EntityType<?>> entityTypes;

    @Nonnull
    private final String searchString;

    @Nonnull
    private final ImmutableList<SearchString> searchWords;

    @Nonnull
    private final ImmutableList<DictionaryLanguage> searchLanguages;

    @Nonnull
    private Page<EntitySearchResult> results = Page.emptyPage();

    private PageRequest pageRequest = PageRequest.requestFirstPage();

    private final ImmutableList<EntitySearchFilter> searchFilters;

    private final EntityNodeRenderer entityNodeRenderer;
    @Nonnull
    private final EntityMatchCriteria resultsSetFilter;
    @Nonnull
    private final MatcherFactory matcherFactory;


    public EntitySearcher(@Nonnull ProjectId projectId,
                          @Nonnull DictionaryManager dictionaryManager,
                          @Nonnull Set<EntityType<?>> entityTypes,
                          @Nonnull String searchString,
                          @Nonnull UserId userId,
                          @Nonnull ImmutableList<DictionaryLanguage> searchLanguages,
                          ImmutableList<EntitySearchFilter> searchFilters,
                          @Nonnull EntityNodeRenderer entityNodeRenderer,
                          @Nonnull EntityMatchCriteria resultsSetFilter,
                          @Nonnull MatcherFactory matcherFactory) {
        this.projectId = checkNotNull(projectId);
        this.userId = checkNotNull(userId);
        this.dictionaryManager = checkNotNull(dictionaryManager);
        this.entityTypes = new HashSet<>(checkNotNull(entityTypes));
        this.searchString = checkNotNull(searchString);
        this.searchWords = Stream.of(searchString.split("\\s+"))
                                 .filter(s -> !s.isEmpty())
                                 .map(SearchString::parseSearchString)
                                 .collect(toImmutableList());
        this.searchLanguages = checkNotNull(searchLanguages);
        this.searchFilters = checkNotNull(searchFilters);
        this.entityNodeRenderer = entityNodeRenderer;
        this.resultsSetFilter = resultsSetFilter;
        this.matcherFactory = checkNotNull(matcherFactory);
    }

    public void setPageRequest(@Nonnull PageRequest pageRequest) {
        this.pageRequest = checkNotNull(pageRequest);
    }

    /**
     * Gets the results from the last search.  The page is determined by the set page request.
     * Note that changing the page request settings after
     * invoking a search will not have any effect on these results.
     *
     * @return A possible subset of the total results.
     */
    @Nonnull
    public Page<EntitySearchResult> getResults() {
        return results;
    }

    public void invoke() {
        var entityMatches = dictionaryManager.getShortFormsContaining(searchWords,
                                                                      entityTypes,
                                                                      searchLanguages,
                                                                      searchFilters,
                                                                      pageRequest,
                resultsSetFilter);
        results = entityMatches.transform(matches -> {
            var entity = matches.getEntity();
            var shortForms = dictionaryManager.getShortForms(entity);
            var entityNode = entityNodeRenderer.render(entity);
            var matchesForEntity = matches.getShortFormMatches().stream().map(shortFormMatch -> {
                var matchedLanguage = shortFormMatch.getLanguage();
                var matchedShortForm = shortFormMatch.getShortForm();
                var matchedPositions = shortFormMatch.getMatchPositions()
                                                     .stream()
                                                     .map(shortFormMatchPosition -> SearchResultMatchPosition.get(
                                                             shortFormMatchPosition.getStart(),
                                                             shortFormMatchPosition.getEnd()))
                                                     .collect(toImmutableList());
                var matchedLanguageRendering = getLanguageRendering(matchedLanguage);
                return SearchResultMatch.get(entityNode,
                                             matchedLanguage,
                                             matchedShortForm,
                                             matchedPositions);


            }).collect(toImmutableList());
            return EntitySearchResult.get(entityNode, matchesForEntity);
        });
    }

    public ImmutableMap<DictionaryLanguage, String> getLanguageRendering(DictionaryLanguage matchedLanguage) {
        return matchedLanguage.accept(new DictionaryLanguageVisitor<>() {
            @Override
            public ImmutableMap<DictionaryLanguage, String> getDefault() {
                return ImmutableMap.of();
            }

            @Override
            public ImmutableMap<DictionaryLanguage, String> visit(@Nonnull AnnotationAssertionDictionaryLanguage language) {
                var annotationPropertyIri = language.getAnnotationPropertyIri();
                return getAnnotationPropertyRendering(annotationPropertyIri);
            }

            @Override
            public ImmutableMap<DictionaryLanguage, String> visit(@Nonnull AnnotationAssertionPathDictionaryLanguage language) {
                return getAnnotationPropertyRendering(language.getAnnotationPropertyPath().get(0));
            }

            private ImmutableMap<DictionaryLanguage, String> getAnnotationPropertyRendering(IRI annotationPropertyIri) {
                var annotationProperty = DataFactory.getOWLAnnotationProperty(annotationPropertyIri);
                return dictionaryManager.getShortForms(annotationProperty);
            }

        });
    }
}
