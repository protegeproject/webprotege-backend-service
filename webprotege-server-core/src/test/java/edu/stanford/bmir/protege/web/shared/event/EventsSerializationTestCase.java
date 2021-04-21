package edu.stanford.bmir.protege.web.shared.event;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.hierarchy.EntityHierarchyChangedEvent;
import edu.stanford.bmir.protege.web.shared.hierarchy.GraphModelChange;
import edu.stanford.bmir.protege.web.shared.hierarchy.GraphModelChangedEvent;
import edu.stanford.bmir.protege.web.shared.hierarchy.HierarchyId;
import edu.stanford.bmir.protege.web.shared.issues.*;
import edu.stanford.bmir.protege.web.shared.lang.DisplayNameSettings;
import edu.stanford.bmir.protege.web.shared.lang.DisplayNameSettingsChangedEvent;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionsChangedEvent;
import edu.stanford.bmir.protege.web.shared.projectsettings.*;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.revision.RevisionSummary;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.tag.EntityTagsChangedEvent;
import edu.stanford.bmir.protege.web.shared.tag.ProjectTagsChangedEvent;
import edu.stanford.bmir.protege.web.shared.watches.Watch;
import edu.stanford.bmir.protege.web.shared.watches.WatchAddedEvent;
import edu.stanford.bmir.protege.web.shared.watches.WatchRemovedEvent;
import edu.stanford.bmir.protege.web.shared.watches.WatchType;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import static edu.stanford.bmir.protege.web.MockingUtils.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-15
 */
public class EventsSerializationTestCase {

    @Test
    public void shouldSerializeClassFrameChangedEvent() throws IOException {
        JsonSerializationTestUtil.testSerialization(
                new ClassFrameChangedEvent(mockOWLClass(), mockProjectId(), mockUserId()),
                WebProtegeEvent.class);
    }

    @Test
    public void shouldSerializeObjectPropertyFrameChangedEvent() throws IOException {
        JsonSerializationTestUtil.testSerialization(
                new ObjectPropertyFrameChangedEvent(mockOWLObjectProperty(), mockProjectId(), mockUserId()),
                WebProtegeEvent.class);
    }

    @Test
    public void shouldSerializeDataPropertyFrameChangedEvent() throws IOException {
        JsonSerializationTestUtil.testSerialization(
                new DataPropertyFrameChangedEvent(mockOWLDataProperty(), mockProjectId(), mockUserId()),
                WebProtegeEvent.class);
    }

    @Test
    public void shouldSerializeAnnotationPropertyFrameChangedEvent() throws IOException {
        JsonSerializationTestUtil.testSerialization(
                new AnnotationPropertyFrameChangedEvent(mockOWLAnnotationProperty(), mockProjectId(), mockUserId()),
                WebProtegeEvent.class);
    }

    @Test
    public void shouldSerializeBrowserTextChangedEvent() throws IOException {
        JsonSerializationTestUtil.testSerialization(
                new BrowserTextChangedEvent(mockOWLClass(), "New", mockProjectId(), ImmutableMap.of()),
                WebProtegeEvent.class
        );
    }

    @Test
    public void shouldSerializeCommentPostedEvent() throws IOException {
        JsonSerializationTestUtil.testSerialization(
                new CommentPostedEvent(mockProjectId(),
                                       ThreadId.create(),
                                       new Comment(CommentId.create(),
                                                   mockUserId(),
                                                   1L,
                                                   Optional.of(3L),
                                                   "The Body",
                                                   "The rendered body"),
                                       Optional.of(mockOWLClassData()),
                                       2,
                                       3),
                WebProtegeEvent.class
        );
    }

    @Test
    public void shouldSerializeCommentUpdatedEvent() throws IOException {
        JsonSerializationTestUtil.testSerialization(
                new CommentUpdatedEvent(mockProjectId(),
                                        ThreadId.create(),
                                        new Comment(CommentId.create(),
                                                    mockUserId(),
                                                    1L,
                                                    Optional.of(3L),
                                                    "The Body",
                                                    "The rendered body")),
                WebProtegeEvent.class
        );
    }

    @Test
    public void shouldSerializeDiscussionThreadCreatedEvent() throws IOException {
        JsonSerializationTestUtil.testSerialization(
                new DiscussionThreadCreatedEvent(new EntityDiscussionThread(
                        ThreadId.create(),
                        mockProjectId(),
                        mockOWLClass(),
                        Status.CLOSED,
                        ImmutableList.of()
                )),
                WebProtegeEvent.class
        );
    }

    @Test
    public void shouldSerializeDiscussionThreadStatusChangedEvent() throws IOException {
        JsonSerializationTestUtil.testSerialization(
                new DiscussionThreadStatusChangedEvent(mockProjectId(),
                                                       ThreadId.create(),
                                                       Optional.empty(),
                                                       3,
                                                       Status.CLOSED),
                WebProtegeEvent.class
        );
    }

    @Test
    public void shouldSerializeDisplayNameSettingsChangedEvent() throws IOException {
        JsonSerializationTestUtil.testSerialization(
                DisplayNameSettingsChangedEvent.get(
                        mockProjectId(),
                        DisplayNameSettings.empty()
                ),
                WebProtegeEvent.class
        );
    }

    @Test
    public void shouldSerializeEntityDeprecatedChangedEvent() throws IOException {
        JsonSerializationTestUtil.testSerialization(
                new EntityDeprecatedChangedEvent(mockProjectId(),
                                                 mockOWLClass(),
                                                 true),
                WebProtegeEvent.class
        );
    }

    @Test
    public void shouldSerializeEntityHierarchyChangedEvent() throws IOException {
        var changes = ImmutableList.<GraphModelChange<EntityNode>>of();
        JsonSerializationTestUtil.testSerialization(
                new EntityHierarchyChangedEvent(mockProjectId(),
                                                HierarchyId.CLASS_HIERARCHY,
                                                GraphModelChangedEvent.create(changes)),
                WebProtegeEvent.class
        );
    }

    @Test
    public void shouldSerializeEntityTagsChangedEvent() throws IOException {
        JsonSerializationTestUtil.testSerialization(
                new EntityTagsChangedEvent(mockProjectId(),
                                           mockOWLClass(),
                                           Collections.emptySet()),
                WebProtegeEvent.class
        );
    }

    @Test
    public void shouldSerializeLargeNumberOfChangesEvent() throws IOException {
        JsonSerializationTestUtil.testSerialization(
                new LargeNumberOfChangesEvent(mockProjectId()),
                WebProtegeEvent.class
        );
    }

    @Test
    public void shouldSerializeOntologyFrameChangedEvent() throws IOException {
        JsonSerializationTestUtil.testSerialization(
                new OntologyBrowserTextChangedEvent(mockOWLOntologyID(),
                                                    "Old",
                                                    "new"),
                WebProtegeEvent.class
        );
    }

    @Test
    public void shouldSerializePermissionsChangedEvent() throws IOException {
        JsonSerializationTestUtil.testSerialization(
                new PermissionsChangedEvent(mockProjectId()),
                WebProtegeEvent.class
        );
    }

    @Test
    public void shouldSerializeProjectChangedEvent() throws IOException {
        JsonSerializationTestUtil.testSerialization(
                new ProjectChangedEvent(mockProjectId(),
                                        new RevisionSummary(
                                                RevisionNumber.getHeadRevisionNumber(),
                                                mockUserId(),
                                                2L,
                                                3,
                                                "Changes"
                                        ),
                                        Collections.emptySet()),
                WebProtegeEvent.class
        );
    }

    @Test
    public void shouldSerializeProjectMovedFromTrashEvent() throws IOException {
        JsonSerializationTestUtil.testSerialization(
                new ProjectMovedFromTrashEvent(mockProjectId()),
                WebProtegeEvent.class
        );
    }

    @Test
    public void shouldSerializeProjectMovedToTrashEvent() throws IOException {
        JsonSerializationTestUtil.testSerialization(
                new ProjectMovedToTrashEvent(mockProjectId()),
                WebProtegeEvent.class
        );
    }

    @Test
    public void shouldSerializeProjectSettingsChangedEvent() throws IOException {
        JsonSerializationTestUtil.testSerialization(
                new ProjectSettingsChangedEvent(ProjectSettings.get(
                        mockProjectId(),
                        "DisplayName",
                        "Desc",
                        DictionaryLanguage.localName(),
                        DisplayNameSettings.empty(),
                        SlackIntegrationSettings.get("url"),
                        WebhookSettings.get(ImmutableList.of()),
                        EntityDeprecationSettings.empty()
                )),
                WebProtegeEvent.class
        );
    }

    @Test
    public void shouldSerializeProjectTagsChangedEvent() throws IOException {
        JsonSerializationTestUtil.testSerialization(
                new ProjectTagsChangedEvent(mockProjectId(),
                                            Collections.emptySet()),
                WebProtegeEvent.class
        );
    }

    @Test
    public void shouldSerializeWatchAddedEvent() throws IOException {
        JsonSerializationTestUtil.testSerialization(
                new WatchAddedEvent(mockProjectId(),
                                    Watch.create(mockUserId(),
                                                 mockOWLClass(),
                                                 WatchType.ENTITY)),
                WebProtegeEvent.class
        );
    }

    @Test
    public void shouldSerializeWatchRemovedEvent() throws IOException {
        JsonSerializationTestUtil.testSerialization(
                new WatchRemovedEvent(mockProjectId(),
                                      Watch.create(mockUserId(),
                                                   mockOWLClass(),
                                                   WatchType.ENTITY)),
                WebProtegeEvent.class
        );
    }
}
