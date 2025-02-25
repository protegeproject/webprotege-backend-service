package edu.stanford.protege.webprotege.shortform;

import edu.stanford.protege.webprotege.common.*;
import edu.stanford.protege.webprotege.criteria.EntityMatchCriteria;
import edu.stanford.protege.webprotege.search.EntitySearchFilter;
import org.apache.lucene.queryparser.classic.ParseException;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-07
 */
public interface LuceneIndex {

    @Nonnull
    Stream<EntityShortForms> find(@Nonnull OWLEntity entity,
                                  @Nonnull List<DictionaryLanguage> languages) throws IOException;

    @Nonnull
    Optional<Page<EntityShortFormMatches>> search(@Nonnull List<SearchString> queryString,
                                                  @Nonnull List<DictionaryLanguage> dictionaryLanguages,
                                                  @Nonnull List<EntitySearchFilter> searchFilters,
                                                  @Nonnull Set<EntityType<?>> entityTypes,
                                                  @Nonnull PageRequest pageRequest,
                                                  @Nullable EntityMatchCriteria resultsSetFilter) throws IOException, ParseException;

    /**
     * Finds entities that have the specified short form.
     * @param shortForm The short form.  Entities that have a short form equal to this specified short form
     *                  will be retrieved
     * @param languages The languages that should be considered for the short form.
     * @return A stream of {@link OWLEntity} where each entity has one or more short forms that are equal to the
     * specified short form in the specified list of languages
     */
    @Nonnull
    Stream<OWLEntity> findEntities(String shortForm, List<DictionaryLanguage> languages) throws ParseException, IOException;
}