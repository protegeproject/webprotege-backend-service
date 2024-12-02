package edu.stanford.protege.webprotege.shortform;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.common.DictionaryLanguage;
import edu.stanford.protege.webprotege.common.EntityShortFormMatches;
import edu.stanford.protege.webprotege.common.Page;
import edu.stanford.protege.webprotege.common.PageRequest;
import edu.stanford.protege.webprotege.criteria.EntityMatchCriteria;
import edu.stanford.protege.webprotege.search.EntitySearchFilter;
import org.apache.lucene.queryparser.classic.ParseException;
import org.semanticweb.owlapi.model.EntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-07
 */
public class SearchableMultiLingualShortFormDictionaryLuceneImpl implements SearchableMultiLingualShortFormDictionary {

    private static final Logger logger = LoggerFactory.getLogger(SearchableMultiLingualShortFormDictionaryLuceneImpl.class);

    @Nonnull
    private final LuceneIndex luceneIndex;

    @Inject
    public SearchableMultiLingualShortFormDictionaryLuceneImpl(@Nonnull LuceneIndex luceneIndex) {
        this.luceneIndex = checkNotNull(luceneIndex);
    }

    @Nonnull
    @Override
    public Page<EntityShortFormMatches> getShortFormsContaining(@Nonnull List<SearchString> searchStrings,
                                                                @Nonnull Set<EntityType<?>> entityTypes,
                                                                @Nonnull List<DictionaryLanguage> languages,
                                                                @Nonnull ImmutableList<EntitySearchFilter> searchFilters,
                                                                @Nonnull PageRequest pageRequest,
                                                                @Nonnull EntityMatchCriteria resultsSetFilter) {
        try {
            var stopwatch = Stopwatch.createStarted();
            // TODO: Rewrite entity types
            var entities = luceneIndex.search(searchStrings, languages, searchFilters, entityTypes, pageRequest, resultsSetFilter);
            var elapsedTimeMs = stopwatch.elapsed().toMillis();
            if(entities.isPresent()) {
                var resultsPage = entities.get();
                logger.info("Found {} results in {} ms", resultsPage.getTotalElements(), elapsedTimeMs);
            }
            return entities.orElse(Page.emptyPage());
        } catch (IOException | ParseException e) {
            logger.error("Error performing search", e);
            return Page.emptyPage();
        }
    }
}
