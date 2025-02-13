package edu.stanford.protege.webprotege.search;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.common.DictionaryLanguage;
import edu.stanford.protege.webprotege.criteria.CompositeRootCriteria;
import edu.stanford.protege.webprotege.criteria.EntityMatchCriteria;
import edu.stanford.protege.webprotege.entity.EntityNodeRenderer;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.match.MatcherFactory;
import edu.stanford.protege.webprotege.shortform.DictionaryManager;
import edu.stanford.protege.webprotege.common.UserId;
import org.semanticweb.owlapi.model.EntityType;

import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-07-13
 */
public class EntitySearcherFactory {

    private final ProjectId projectId;

    private final DictionaryManager dictionaryManger;

    private final EntityNodeRenderer entityNodeRenderer;
    private final MatcherFactory matcherFactory;

    public EntitySearcherFactory(ProjectId projectId,
                                 DictionaryManager dictionaryManger,
                                 EntityNodeRenderer entityNodeRenderer,
                                 MatcherFactory matcherFactory) {
        this.projectId = projectId;
        this.dictionaryManger = dictionaryManger;
        this.entityNodeRenderer = entityNodeRenderer;
        this.matcherFactory = matcherFactory;
    }

    public EntitySearcher create(Set<EntityType<?>> entityTypes,
                                 String searchString,
                                 UserId userId,
                                 ImmutableList<DictionaryLanguage> languages,
                                 ImmutableList<EntitySearchFilter> searchFilters,
                                 EntityMatchCriteria resultsSetFilter) {
        return new EntitySearcher(projectId,
                                  dictionaryManger,
                                  entityTypes,
                                  searchString,
                                  userId,
                                  languages,
                                  searchFilters,
                                  entityNodeRenderer,
                resultsSetFilter,
                matcherFactory
                );
    }
}
