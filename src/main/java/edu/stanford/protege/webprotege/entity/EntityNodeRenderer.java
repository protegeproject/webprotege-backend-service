package edu.stanford.protege.webprotege.entity;

import edu.stanford.protege.webprotege.issues.EntityDiscussionThreadRepository;
import edu.stanford.protege.webprotege.lang.LanguageManager;
import edu.stanford.protege.webprotege.mansyntax.render.DeprecatedEntityChecker;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.shortform.DictionaryManager;
import edu.stanford.protege.webprotege.tag.TagsManager;
import edu.stanford.protege.webprotege.watches.WatchManager;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;

import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 29 Nov 2017
 */
public class EntityNodeRenderer {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final DictionaryManager dictionaryManager;

    @Nonnull
    private final DeprecatedEntityChecker deprecatedEntityChecker;

    @Nonnull
    private final WatchManager watchManager;

    @Nonnull
    private final EntityDiscussionThreadRepository discussionThreadRepository;

    @Nonnull
    private final TagsManager tagsManager;

    @Nonnull
    private final LanguageManager languageManager;

    @Inject
    public EntityNodeRenderer(@Nonnull ProjectId projectId,
                              @Nonnull DictionaryManager dictionaryManager,
                              @Nonnull DeprecatedEntityChecker deprecatedEntityChecker,
                              @Nonnull WatchManager watchManager,
                              @Nonnull EntityDiscussionThreadRepository discussionThreadRepository,
                              @Nonnull TagsManager tagsManager, @Nonnull LanguageManager languageManager) {
        this.projectId = checkNotNull(projectId);
        this.dictionaryManager = checkNotNull(dictionaryManager);
        this.deprecatedEntityChecker = checkNotNull(deprecatedEntityChecker);
        this.watchManager = checkNotNull(watchManager);
        this.discussionThreadRepository = checkNotNull(discussionThreadRepository);
        this.tagsManager = checkNotNull(tagsManager);
        this.languageManager = checkNotNull(languageManager);
    }

    /**
     * Renders the node for the specified entity.
     * @param entity The entity to be rendered.
     * @return The node for the specified entity.
     */
    @Nonnull
    public EntityNode render(@Nonnull OWLEntity entity) {
        return EntityNode.get(
                entity,
                dictionaryManager.getShortForm(entity, languageManager.getLanguages()),
                dictionaryManager.getShortForms(entity),
                deprecatedEntityChecker.isDeprecated(entity),
                watchManager.getDirectWatches(entity),
                discussionThreadRepository.getOpenCommentsCount(projectId, entity),
                tagsManager.getTags(entity),
                Set.of());
    }
}
