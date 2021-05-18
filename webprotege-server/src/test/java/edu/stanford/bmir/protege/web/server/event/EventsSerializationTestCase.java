package edu.stanford.bmir.protege.web.server.event;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.MockingUtils;
import edu.stanford.bmir.protege.web.server.entity.EntityNode;
import edu.stanford.bmir.protege.web.server.hierarchy.EntityHierarchyChangedEvent;
import edu.stanford.bmir.protege.web.server.hierarchy.GraphModelChange;
import edu.stanford.bmir.protege.web.server.hierarchy.GraphModelChangedEvent;
import edu.stanford.bmir.protege.web.server.hierarchy.HierarchyId;
import edu.stanford.bmir.protege.web.server.issues.*;
import edu.stanford.bmir.protege.web.server.lang.DisplayNameSettings;
import edu.stanford.bmir.protege.web.server.lang.DisplayNameSettingsChangedEvent;
import edu.stanford.bmir.protege.web.server.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.server.projectsettings.*;
import edu.stanford.bmir.protege.web.server.permissions.PermissionsChangedEvent;
import edu.stanford.bmir.protege.web.server.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.server.revision.RevisionSummary;
import edu.stanford.bmir.protege.web.server.shortform.DictionaryLanguage;
import edu.stanford.bmir.protege.web.server.tag.EntityTagsChangedEvent;
import edu.stanford.bmir.protege.web.server.tag.ProjectTagsChangedEvent;
import edu.stanford.bmir.protege.web.server.watches.Watch;
import edu.stanford.bmir.protege.web.server.watches.WatchAddedEvent;
import edu.stanford.bmir.protege.web.server.watches.WatchRemovedEvent;
import edu.stanford.bmir.protege.web.server.watches.WatchType;
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
                new ClassFrameChangedEvent(MockingUtils.mockOWLClass(), MockingUtils.mockProjectId(), MockingUtils.mockUserId()),
                WebProtegeEvent.class);
    }

    @Test
    public void shouldSerializeObjectPropertyFrameChangedEvent() throws IOException {
        JsonSerializationTestUtil.testSerialization(
                new ObjectPropertyFrameChangedEvent(MockingUtils.mockOWLObjectProperty(), MockingUtils.mockProjectId(), MockingUtils
                        .mockUserId()),
                WebProtegeEvent.class);
    }

    @Test
    public void shouldSerializeDataPropertyFrameChangedEvent() throws IOException {
        JsonSerializationTestUtil.testSerialization(
                new DataPropertyFrameChangedEvent(MockingUtils.mockOWLDataProperty(), MockingUtils.mockProjectId(), MockingUtils
                        .mockUserId()),
                WebProtegeEvent.class);
    }

    @Test
    public void shouldSerializeAnnotationPropertyFrameChangedEvent() throws IOException {
        JsonSerializationTestUtil.testSerialization(
                new AnnotationPropertyFrameChangedEvent(MockingUtils.mockOWLAnnotationProperty(), MockingUtils.mockProjectId(), MockingUtils
                        .mockUserId()),
                WebProtegeEvent.class);
    }

    @Test
    public void shouldSerializeBrowserTextChangedEvent() throws IOException {
        JsonSerializationTestUtil.testSerialization(
                new BrowserTextChangedEvent(MockingUtils.mockOWLClass(), "New", MockingUtils.mockProjectId(), ImmutableMap.of()),
                WebProtegeEvent.class
        );
    }

    @Test
    public void shouldSerializeCommentPostedEvent() throws IOException {
        JsonSerializationTestUtil.testSerialization(
                new CommentPostedEvent(MockingUtils.mockProjectId(),
                                       ThreadId.create(),
                                       new Comment(CommentId.create(),
                                                   MockingUtils.mockUserId(),
                                                   1L,
                                                   Optional.of(3L),
                                                   "The Body",
                                                   "The rendered body"),
                                       Optional.of(MockingUtils.mockOWLClassData()),
                                       2,
                                       3),
                WebProtegeEvent.class
        );
    }

    @Test
    public void shouldSerializeCommentUpdatedEvent() throws IOException {
        JsonSerializationTestUtil.testSerialization(
                new CommentUpdatedEvent(MockingUtils.mockProjectId(),
                                        ThreadId.create(),
                                        new Comment(CommentId.create(),
                                                    MockingUtils.mockUserId(),
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
                        MockingUtils.mockProjectId(),
                        MockingUtils.mockOWLClass(),
                        Status.CLOSED,
                        ImmutableList.of()
                )),
                WebProtegeEvent.class
        );
    }

    @Test
    public void shouldSerializeDiscussionThreadStatusChangedEvent() throws IOException {
        JsonSerializationTestUtil.testSerialization(
                new DiscussionThreadStatusChangedEvent(MockingUtils.mockProjectId(),
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
                        MockingUtils.mockProjectId(),
                        DisplayNameSettings.empty()
                ),
                WebProtegeEvent.class
        );
    }

    @Test
    public void shouldSerializeEntityDeprecatedChangedEvent() throws IOException {
        JsonSerializationTestUtil.testSerialization(
                new EntityDeprecatedChangedEvent(MockingUtils.mockProjectId(),
                                                 MockingUtils.mockOWLClass(),
                                                 true),
                WebProtegeEvent.class
        );
    }

    @Test
    public void shouldSerializeEntityHierarchyChangedEvent() throws IOException {
        var changes = ImmutableList.<GraphModelChange<EntityNode>>of();
        JsonSerializationTestUtil.testSerialization(
                new EntityHierarchyChangedEvent(MockingUtils.mockProjectId(),
                                                HierarchyId.CLASS_HIERARCHY,
                                                GraphModelChangedEvent.create(changes)),
                WebProtegeEvent.class
        );
    }

    @Test
    public void shouldSerializeEntityTagsChangedEvent() throws IOException {
        JsonSerializationTestUtil.testSerialization(
                new EntityTagsChangedEvent(MockingUtils.mockProjectId(),
                                           MockingUtils.mockOWLClass(),
                                           Collections.emptySet()),
                WebProtegeEvent.class
        );
    }

    @Test
    public void shouldSerializeLargeNumberOfChangesEvent() throws IOException {
        JsonSerializationTestUtil.testSerialization(
                new LargeNumberOfChangesEvent(MockingUtils.mockProjectId()),
                WebProtegeEvent.class
        );
    }

    @Test
    public void shouldSerializeOntologyFrameChangedEvent() throws IOException {
        JsonSerializationTestUtil.testSerialization(
                new OntologyBrowserTextChangedEvent(MockingUtils.mockOWLOntologyID(),
                                                    "Old",
                                                    "new"),
                WebProtegeEvent.class
        );
    }

    @Test
    public void shouldSerializePermissionsChangedEvent() throws IOException {
        JsonSerializationTestUtil.testSerialization(
                new PermissionsChangedEvent(MockingUtils.mockProjectId()),
                WebProtegeEvent.class
        );
    }

    @Test
    public void shouldSerializeProjectChangedEvent() throws IOException {
        JsonSerializationTestUtil.testSerialization(
                new ProjectChangedEvent(MockingUtils.mockProjectId(),
                                        new RevisionSummary(
                                                RevisionNumber.getHeadRevisionNumber(),
                                                MockingUtils.mockUserId(),
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
                new ProjectMovedFromTrashEvent(MockingUtils.mockProjectId()),
                WebProtegeEvent.class
        );
    }

    @Test
    public void shouldSerializeProjectMovedToTrashEvent() throws IOException {
        JsonSerializationTestUtil.testSerialization(
                new ProjectMovedToTrashEvent(MockingUtils.mockProjectId()),
                WebProtegeEvent.class
        );
    }

    @Test
    public void shouldSerializeProjectSettingsChangedEvent() throws IOException {
        JsonSerializationTestUtil.testSerialization(
                new ProjectSettingsChangedEvent(ProjectSettings.get(
                        MockingUtils.mockProjectId(),
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
                new ProjectTagsChangedEvent(MockingUtils.mockProjectId(),
                                            Collections.emptySet()),
                WebProtegeEvent.class
        );
    }

    @Test
    public void shouldSerializeWatchAddedEvent() throws IOException {
        JsonSerializationTestUtil.testSerialization(
                new WatchAddedEvent(MockingUtils.mockProjectId(),
                                    Watch.create(MockingUtils.mockUserId(),
                                                 MockingUtils.mockOWLClass(),
                                                 WatchType.ENTITY)),
                WebProtegeEvent.class
        );
    }

    @Test
    public void shouldSerializeWatchRemovedEvent() throws IOException {
        JsonSerializationTestUtil.testSerialization(
                new WatchRemovedEvent(MockingUtils.mockProjectId(),
                                      Watch.create(MockingUtils.mockUserId(),
                                                   MockingUtils.mockOWLClass(),
                                                   WatchType.ENTITY)),
                WebProtegeEvent.class
        );
    }
}
