package edu.stanford.bmir.protege.web.server.search;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.base.Stopwatch;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import edu.stanford.bmir.protege.web.server.mansyntax.render.HasGetRendering;
import edu.stanford.bmir.protege.web.server.shortform.DictionaryManager;
import edu.stanford.bmir.protege.web.server.shortform.LanguageManager;
import edu.stanford.bmir.protege.web.server.shortform.LocalNameExtractor;
import edu.stanford.bmir.protege.web.server.shortform.ShortFormMatch;
import edu.stanford.bmir.protege.web.server.tag.TagsManager;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.search.EntitySearchResult;
import edu.stanford.bmir.protege.web.shared.tag.Tag;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.server.logging.Markers.BROWSING;
import static edu.stanford.bmir.protege.web.shared.search.SearchField.displayName;
import static java.util.Arrays.asList;
import static java.util.Comparator.comparing;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static org.apache.commons.lang.StringUtils.startsWithIgnoreCase;

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
    private final LanguageManager languageManager;

    @Nonnull
    private final Set<EntityType<?>> entityTypes;

    @Nonnull
    private final String searchString;

    @Nonnull
    private final TagsManager tagsManager;

    private final Map<String, Tag> tagsByLabel = new HashMap<>();

    private final Multimap<OWLEntity, Tag> tagsByEntity = HashMultimap.create();

    @Nonnull
    private final String[] searchWords;

    private final Counter searchCounter = new Counter();

    private final Counter matchCounter = new Counter();

    private final List<EntitySearchResult> results = new ArrayList<>();

    private int skip = 0;

    private int limit = DEFAULT_LIMIT;

    @AutoFactory
    public EntitySearcher(@Provided @Nonnull ProjectId projectId,
                          @Provided @Nonnull OWLOntology rootOntology,
                          @Provided @Nonnull HasGetRendering renderingSupplier,
                          @Provided @Nonnull DictionaryManager dictionaryManager,
                          @Provided @Nonnull LanguageManager languageManager,
                          @Nonnull Set<EntityType<?>> entityTypes,
                          @Provided @Nonnull TagsManager tagsManager,
                          @Nonnull String searchString,
                          @Nonnull UserId userId) {
        this.projectId = checkNotNull(projectId);
        this.userId = checkNotNull(userId);
        this.dictionaryManager = checkNotNull(dictionaryManager);
        this.languageManager = languageManager;
        this.entityTypes = new HashSet<>(checkNotNull(entityTypes));
        this.searchString = checkNotNull(searchString);
        this.searchWords = searchString.split("\\s+");
        this.tagsManager = checkNotNull(tagsManager);
    }

    private static Pattern compileSearchPattern(String[] searchWords) {
        StringBuilder searchWordsPattern = new StringBuilder();
        for (int i = 0; i < searchWords.length; i++) {
            searchWordsPattern.append(Pattern.quote(searchWords[i]));
            if (i < searchWords.length - 1) {
                searchWordsPattern.append("|");
            }
        }
        return Pattern.compile(searchWordsPattern.toString(), CASE_INSENSITIVE);
    }

    private static void highlightSearchResult(Pattern searchPattern, String rendering, StringBuilder highlighted) {
        Matcher matcher = searchPattern.matcher(rendering);
        int cur = 0;
        while (matcher.find()) {
            int start = matcher.start();
            if (cur != start) {
                highlighted.append("<span style='white-space: pre;'>");
                highlighted.append(rendering.substring(cur, start));
                highlighted.append("</span>");
            }
            highlighted.append("<strong span style='white-space: pre;'>");
            int end = matcher.end();
            highlighted.append(rendering.substring(start, end));
            highlighted.append("</strong>");
            cur = end;
        }
        if (cur < rendering.length()) {
            highlighted.append("<span style='white-space: pre;'>");
            highlighted.append(rendering.substring(cur));
            highlighted.append("</span>");
        }
    }

    /**
     * Gets the skip setting.  The default value is 0.
     *
     * @return The skip setting.
     */
    public int getSkip() {
        return skip;
    }

    /**
     * Sets the skip setting.  This is used to determine the results returned by
     * {@link #getResults()}.
     *
     * @param skip The skip setting, which should be 0 or larger.
     */
    public void setSkip(int skip) {
        checkArgument(skip > -1, "skip must be zero or positive integer");
        this.skip = skip;
    }

    /**
     * Gets the limit setting.  This is used to determine the results returned by
     * {@link #getResults()}.
     *
     * @return The limit setting.  The default value is set to {@link #DEFAULT_LIMIT}.
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Sets the limit setting.  This is used to determine the results returned by
     * {@link #getResults()}.
     *
     * @param limit The limit setting.  This should be a positive integer.
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * Gets a count of the search results from the last search.
     *
     * @return A count of the search results from the last search.  If a search has not been
     * performed then the returned value will be 0.
     */
    public int getSearchResultsCount() {
        return matchCounter.getCounter();
    }

    /**
     * Gets a subset of the results from the last search as determined by {@link #getSkip()}
     * and {@link #getLimit()}.  Note that changing the skip and limit settings after
     * invoking a search will not have any effect on these results.
     *
     * @return A possible subset of the total results.
     */
    public List<EntitySearchResult> getResults() {
        return new ArrayList<>(results);
    }

    public void invoke() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        matchCounter.reset();
        searchCounter.reset();
        results.clear();
        tagsByLabel.clear();
        tagsManager.getProjectTags().forEach(tag -> tagsByLabel.put(tag.getLabel().toLowerCase(), tag));
        tagsByEntity.clear();
        tagsByEntity.putAll(tagsManager.getTags(projectId));

        Pattern searchPattern = compileSearchPattern(searchWords);
        Counter filledCounter = new Counter();
        tagsByLabel.entrySet().stream()
                   .filter(e -> e.getKey().startsWith(searchString.toLowerCase()))
                   .map(e -> e.getValue().getTagId())
                   .flatMap(tagId -> tagsManager.getTaggedEntities(tagId).stream())
                   .filter(entity -> entityTypes.contains(entity.getEntityType()))
                   .distinct()
                   .peek(result -> matchCounter.increment())
                   .sorted(comparing(dictionaryManager::getShortForm))
                   .skip(skip)
                   .peek(entity -> filledCounter.increment())
                   .limit(limit)
                   .map(entity -> {
                       String rendering = dictionaryManager.getShortForm(entity);
                       return toSearchResult(searchPattern, entity, rendering, MatchType.TAG);
                   })
                   .sorted(comparing(EntitySearchResult::getFieldRendering))
                   .forEach(results::add);
        System.out.printf("Matched %d,  Filled %d, Limit %d\n", matchCounter.getCounter(), filledCounter.getCounter(), limit);
        int limitRemainder = limit - filledCounter.getCounter();
        if (limitRemainder > 0) {
            int skipRemainder = Math.max(skip - filledCounter.getCounter(), 0);
            dictionaryManager.getShortFormsContaining(asList(searchWords),
                                                      languageManager.getLanguages())
                             .filter(this::isRequiredEntityType)
                             .map(this::performMatch)
                             .filter(Objects::nonNull)
                             .peek(this::incrementMatchCounter)
                             .sorted()
                             .skip(skipRemainder)
                             .limit(limitRemainder)
                             .map(m -> toSearchResult(searchPattern, m))
                             .forEach(results::add);
        }

        logger.info(BROWSING,
                    "{} {} Performed entity search for \"{}\".  Found {} matches in {} ms.",
                    projectId,
                    userId,
                    searchString,
                    matchCounter.getCounter(),
                    stopwatch.elapsed(TimeUnit.MILLISECONDS));

    }

    private boolean isRequiredEntityType(ShortFormMatch m) {
        return entityTypes.contains(m.getEntity().getEntityType());
    }

    private void incrementSearchCounter(OWLEntity entity) {
        searchCounter.increment();
    }

    private void incrementMatchCounter(SearchMatch match) {
        matchCounter.increment();
    }

    private EntitySearchResult toSearchResult(Pattern searchPattern, SearchMatch match) {
        String rendering;
        OWLEntity entity = match.getEntity();
        if (match.getMatchType() == MatchType.IRI) {
            rendering = dictionaryManager.getShortForm(entity);
        }
        else {
            rendering = match.getShortForm();
        }
        return toSearchResult(searchPattern, entity, rendering, match.getMatchType());
    }

    private EntitySearchResult toSearchResult(Pattern searchPattern,
                                              OWLEntity entity,
                                              String rendering,
                                              MatchType matchType) {
//        ShortFormMatch match = ren.getMatch();
        StringBuilder highlighted = new StringBuilder();
        highlightSearchResult(searchPattern, rendering, highlighted);
        String localName = localNameExtractor.getLocalName(entity.getIRI());
        Matcher matcher = OBO_ID_PATTERN.matcher(localName);
        if (matcher.matches()) {
            highlighted.append("<div style='color: #b4b4b4; margin-left: 5px;'>");
            highlightSearchResult(searchPattern, matcher.group(1), highlighted);
            highlighted.append(":");
            highlightSearchResult(searchPattern, matcher.group(2), highlighted);
            highlighted.append("<div>");
        }
        else if (matchType == MatchType.IRI) {
            // Matched the IRI local name
            highlighted.append("<div style='color: #b4b4b4; margin-left: 5px;'>");
            IRI iri = entity.getIRI();
            highlightSearchResult(searchPattern, iri.toString(), highlighted);
            highlighted.append("</div>");
        }

        if (matchType == MatchType.TAG) {
            for (Tag tag : tagsByEntity.get(entity)) {
                if (startsWithIgnoreCase(tag.getLabel(), searchString)) {
                    highlighted.append("<div class='wp-tag wp-tag--inline-tag' style='color: ")
                               .append(tag.getColor().getHex())
                               .append("; background-color:")
                               .append(tag.getBackgroundColor().getHex()).append(";'>");
                    highlighted.append(tag.getLabel());
                    highlighted.append("</div>");
                }
            }
        }
        return new EntitySearchResult(DataFactory.getOWLEntityData(entity, rendering),
                                      displayName(),
                                      highlighted.toString());
    }

    @Nullable
    private SearchMatch performMatch(@Nonnull ShortFormMatch m) {
        MatchType matchType = null;

        if (m.getLanguage().isAnnotationBased()) {
            matchType = MatchType.RENDERING;
        }

        // If we didn't match the rendering then search the IRI remainder
        IRI entityIri = m.getEntity().getIRI();
        if (matchType == null && !m.getLanguage().isAnnotationBased()) {
            // Used to do this
            // entityIri.toString().startsWith(Obo2OWLConstants.DEFAULT_IRI_PREFIX)
            matchType = MatchType.IRI;
        }
        if (matchType != null) {
            return new SearchMatch(searchWords, m, matchType);
        }
        else {
            return null;
        }
    }


    private enum MatchType {
        RENDERING,
        IRI,
        TAG
    }

    private static class Counter {

        private int counter;

        public void increment() {
            counter++;
        }

        public int getCounter() {
            return counter;
        }

        public void reset() {
            counter = 0;
        }
    }

    private static class SearchMatch implements Comparable<SearchMatch> {

        private static final int BEFORE = -1;

        private static final int AFTER = 1;

        private final static Comparator<SearchMatch> comparator = comparing(SearchMatch::getMatchType)
                .thenComparing(SearchMatch::getFirstMatchIndex)
                .thenComparing(SearchMatch::getShortForm);

        private final String[] searchWords;

        private final ShortFormMatch match;

        private final MatchType matchType;

        public SearchMatch(@Nonnull String[] searchWords,
                           @Nonnull ShortFormMatch match,
                           @Nonnull MatchType matchType) {
            this.searchWords = checkNotNull(searchWords);
            this.match = checkNotNull(match);
            this.matchType = checkNotNull(matchType);
        }

        public MatchType getMatchType() {
            return matchType;
        }

        @Override
        public int compareTo(@Nonnull SearchMatch other) {
            return comparator.compare(this, other);
        }

        @Nonnull
        public OWLEntity getEntity() {
            return match.getEntity();
        }

        @Nonnull
        public String getShortForm() {
            return match.getShortForm();
        }

        public int getFirstMatchIndex() {
            return match.getFirstMatchIndex();
        }
    }
}
