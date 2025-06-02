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
import java.util.List;
import java.util.Set;

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
}
