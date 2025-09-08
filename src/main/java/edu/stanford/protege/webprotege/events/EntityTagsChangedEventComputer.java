package edu.stanford.protege.webprotege.events;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import edu.stanford.protege.webprotege.change.ChangeApplicationResult;
import edu.stanford.protege.webprotege.change.OntologyChange;
import edu.stanford.protege.webprotege.change.OntologyChangeSubjectProvider;
import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.common.EventId;
import edu.stanford.protege.webprotege.inject.ProjectSingleton;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.revision.Revision;
import edu.stanford.protege.webprotege.tag.EntityTagsChangedEvent;
import edu.stanford.protege.webprotege.tag.Tag;
import edu.stanford.protege.webprotege.tag.TagsManager;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Jun 2018
 */
public class EntityTagsChangedEventComputer implements EventTranslator {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final OntologyChangeSubjectProvider provider;

    @Nonnull
    private final TagsManager tagsManager;

    private final SetMultimap<OWLEntity, Tag> beforeChangesTags = HashMultimap.create();

    private final EventTranslatorSessionChecker sessionChecker = new EventTranslatorSessionChecker();

    @Inject
    public EntityTagsChangedEventComputer(@Nonnull ProjectId projectId, @Nonnull OntologyChangeSubjectProvider provider,
                                          @Nonnull TagsManager tagsManager) {
        this.projectId = checkNotNull(projectId);
        this.provider = checkNotNull(provider);
        this.tagsManager = checkNotNull(tagsManager);
    }

    @Override
    public void prepareForOntologyChanges(EventTranslatorSessionId sessionId, List<OntologyChange> submittedChanges) {
        sessionChecker.startSession(sessionId);
        submittedChanges.forEach(chg -> {
            provider.getChangeSubjects(chg).forEach(entity -> {
                beforeChangesTags.putAll(entity, tagsManager.getTags(entity));
            });
        });
    }

    @Override
    public void translateOntologyChanges(EventTranslatorSessionId sessionId, Revision revision,
                                         ChangeApplicationResult<?> changes,
                                         List<HighLevelProjectEventProxy> projectEventList,
                                         ChangeRequestId changeRequestId) {
        sessionChecker.finishSession(sessionId);
        changes.getChangeList().forEach(chg -> {
            provider.getChangeSubjects(chg).forEach(entity -> {
                Collection<Tag> tags = tagsManager.getTags(entity);
                if(!tags.equals(beforeChangesTags.get(entity))) {
                    projectEventList.add(SimpleHighLevelProjectEventProxy.wrap(new EntityTagsChangedEvent(EventId.generate(), projectId, entity, tags)));
                }
            });
        });
    }

    @Override
    public void closeSession(EventTranslatorSessionId sessionId) {
        sessionChecker.finishSession(sessionId);
    }
}
