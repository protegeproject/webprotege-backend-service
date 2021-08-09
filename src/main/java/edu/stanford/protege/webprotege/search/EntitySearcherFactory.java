package edu.stanford.protege.webprotege.search;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.entity.EntityNodeRenderer;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.shortform.DictionaryLanguage;
import edu.stanford.protege.webprotege.shortform.DictionaryManager;
import edu.stanford.protege.webprotege.user.UserId;
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

    public EntitySearcherFactory(ProjectId projectId,
                                 DictionaryManager dictionaryManger,
                                 EntityNodeRenderer entityNodeRenderer) {
        this.projectId = projectId;
        this.dictionaryManger = dictionaryManger;
        this.entityNodeRenderer = entityNodeRenderer;
    }

    public EntitySearcher create(Set<EntityType<?>> entityTypes,
                                 String searchString,
                                 UserId userId,
                                 ImmutableList<DictionaryLanguage> languages,
                                 ImmutableList<EntitySearchFilter> searchFilters) {
        return new EntitySearcher(projectId,
                                  dictionaryManger,
                                  entityTypes,
                                  searchString,
                                  userId,
                                  languages,
                                  searchFilters,
                                  entityNodeRenderer);
    }
}
