package edu.stanford.protege.webprotege.shortform;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.common.DictionaryLanguage;
import edu.stanford.protege.webprotege.common.EntityShortFormMatches;
import edu.stanford.protege.webprotege.common.Page;
import edu.stanford.protege.webprotege.common.PageRequest;
import edu.stanford.protege.webprotege.criteria.EntityMatchCriteria;
import edu.stanford.protege.webprotege.search.DeprecatedEntitiesTreatment;
import edu.stanford.protege.webprotege.search.EntitySearchFilter;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-02
 *
 * Represents an interface for searching multi-lingual short forms
 */
public interface SearchableMultiLingualShortFormDictionary {
    /**
     * Gets short forms containing the specified search strings
     *
     * @param searchStrings The search strings.
     * @param entityTypes   The types of entities to be retrieved.
     * @param languages     The list of languages to consider.
     * @param searchFilters A list of search filters.  An empty list indicates no filtering.
     * @param pageRequest   A page request for the short forms
     * @return A page of short forms.
     */
    @Nonnull
    Page<EntityShortFormMatches> getShortFormsContaining(@Nonnull List<SearchString> searchStrings,
                                                         @Nonnull Set<EntityType<?>> entityTypes,
                                                         @Nonnull List<DictionaryLanguage> languages,
                                                         @Nonnull ImmutableList<EntitySearchFilter> searchFilters,
                                                         @Nonnull PageRequest pageRequest,
                                                         @Nonnull EntityMatchCriteria resultsSetFilter,
                                                         @Nonnull DeprecatedEntitiesTreatment deprecatedEntitiesTreatment);

    /**
     * Gets short forms containing the specified search strings as a stream.
     * This method returns all matching results without pagination, allowing for
     * complete filtering before collecting results.
     *
     * @param searchStrings The search strings.
     * @param entityTypes   The types of entities to be retrieved.
     * @param languages     The list of languages to consider.
     * @param searchFilters A list of search filters.  An empty list indicates no filtering.
     * @param resultsSetFilter Optional filter for result matching criteria.
     * @param deprecatedEntitiesTreatment How to handle deprecated entities.
     * @return A stream of matching short forms.
     * @throws IOException If an I/O error occurs during the search.
     */
    @Nonnull
    Stream<EntityShortFormMatches> getShortFormsContainingAsStream(@Nonnull List<SearchString> searchStrings,
                                                                   @Nonnull Set<EntityType<?>> entityTypes,
                                                                   @Nonnull List<DictionaryLanguage> languages,
                                                                   @Nonnull ImmutableList<EntitySearchFilter> searchFilters,
                                                                   @Nullable EntityMatchCriteria resultsSetFilter,
                                                                   @Nonnull DeprecatedEntitiesTreatment deprecatedEntitiesTreatment) throws IOException;
}
