package edu.stanford.protege.webprotege.shortform;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import edu.stanford.protege.webprotege.common.DictionaryLanguage;
import edu.stanford.protege.webprotege.common.EntityShortFormMatches;
import edu.stanford.protege.webprotege.criteria.EntityMatchCriteria;
import edu.stanford.protege.webprotege.inject.ProjectSingleton;
import edu.stanford.protege.webprotege.lang.LanguageManager;
import edu.stanford.protege.webprotege.common.Page;
import edu.stanford.protege.webprotege.common.PageRequest;
import edu.stanford.protege.webprotege.search.EntitySearchFilter;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import jakarta.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Apr 2018
 */
@ProjectSingleton
public class DictionaryManager {

    @Nonnull
    private final LanguageManager languageManager;

    @Nonnull
    private final MultiLingualDictionary dictionary;

    @Nonnull
    private final MultilingualDictionaryUpdater updatableDictionary;

    @Nonnull
    private final BuiltInShortFormDictionary builtInShortFormDictionary;

    @Inject
    public DictionaryManager(@Nonnull LanguageManager languageManager,
                             @Nonnull MultiLingualDictionary dictionary,
                             @Nonnull MultilingualDictionaryUpdater updatableDictionary,
                             @Nonnull BuiltInShortFormDictionary builtInShortFormDictionary) {
        this.languageManager = checkNotNull(languageManager);
        this.dictionary = checkNotNull(dictionary);
        this.updatableDictionary = updatableDictionary;
        this.builtInShortFormDictionary = checkNotNull(builtInShortFormDictionary);
    }

    /**
     * Gets the entities that exactly match the specified short form
     * @param shortForm The short form.  The short form may be quoted.  If it is, the match will
     *                  be performed on the non-quoted version of the specified short form.
     * @return The entities that exactly match the specified short form.
     */
    public Stream<OWLEntity> getEntities(@Nonnull String shortForm) {
        return dictionary.getEntities(ShortFormQuotingUtils.getUnquotedShortForm(shortForm),
                                      languageManager.getLanguages());
    }

    @Nonnull
    public String getShortForm(@Nonnull OWLEntity entity,
                               @Nonnull List<DictionaryLanguage> languages) {
        var builtInEntityShortForm = builtInShortFormDictionary.getShortForm(entity, null);
        if (builtInEntityShortForm != null) {
            return builtInEntityShortForm;
        }
        return dictionary.getShortForm(entity, languages, "");
    }

    @Nonnull
    public String getQuotedShortForm(@Nonnull OWLEntity entity,
                                     @Nonnull List<DictionaryLanguage> languages) {
        var shortForm = getShortForm(entity, languages);
        return ShortFormQuotingUtils.getQuotedShortForm(shortForm);
    }

    @Nonnull
    public String getShortForm(@Nonnull OWLEntity entity) {
        return getShortForm(entity,
                            languageManager.getLanguages());
    }

    /**
     * Gets the short forms containing the specified search strings.
     * @param searchStrings The search strings
     * @param entityTypes The types of entities to be matched.
     * @param languages The languages of the short forms.
     * @return A stream of matching short forms.
     */
    @Nonnull
    public Page<EntityShortFormMatches> getShortFormsContaining(@Nonnull List<SearchString> searchStrings,
                                                                @Nonnull Set<EntityType<?>> entityTypes,
                                                                @Nonnull List<DictionaryLanguage> languages,
                                                                @Nonnull ImmutableList<EntitySearchFilter> searchFilters,
                                                                @Nonnull PageRequest pageRequest,
                                                                @Nullable EntityMatchCriteria resultsSetFilter) {
        return dictionary.getShortFormsContaining(searchStrings, entityTypes, languages, searchFilters, pageRequest, resultsSetFilter);
    }

    public void update(@Nonnull Collection<OWLEntity> entities) {
        updatableDictionary.update(entities,
                                   languageManager.getLanguages());
    }

    @Nonnull
    public ImmutableMap<DictionaryLanguage, String> getShortForms(OWLEntity entity) {
        var languages = languageManager.getLanguages();
        return dictionary.getShortForms(entity, languages);
    }
}
