package edu.stanford.bmir.protege.web.server.projectsettings;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.color.Color;
import edu.stanford.bmir.protege.web.server.crud.EntityCrudKitPrefixSettings;
import edu.stanford.bmir.protege.web.server.crud.EntityCrudKitSettings;
import edu.stanford.bmir.protege.web.server.crud.gen.GeneratedAnnotationsSettings;
import edu.stanford.bmir.protege.web.server.crud.uuid.UuidSuffixSettings;
import edu.stanford.bmir.protege.web.server.lang.DisplayNameSettings;
import edu.stanford.bmir.protege.web.server.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.server.match.criteria.EntityIsDeprecatedCriteria;
import edu.stanford.bmir.protege.web.server.project.PrefixDeclaration;
import edu.stanford.bmir.protege.web.server.project.ProjectId;
import edu.stanford.bmir.protege.web.server.search.ProjectSearchSettings;
import edu.stanford.bmir.protege.web.server.sharing.PersonId;
import edu.stanford.bmir.protege.web.server.sharing.ProjectSharingSettings;
import edu.stanford.bmir.protege.web.server.sharing.SharingPermission;
import edu.stanford.bmir.protege.web.server.sharing.SharingSetting;
import edu.stanford.bmir.protege.web.server.shortform.DictionaryLanguage;
import edu.stanford.bmir.protege.web.server.tag.Tag;
import edu.stanford.bmir.protege.web.server.tag.TagId;
import org.junit.Test;

import java.io.IOException;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-24
 */
public class AllProjectSettings_Serialization_TestCase {

    @Test
    public void shouldRoundTripSettings() throws IOException {
        var projectId = ProjectId.getNil();
        var projectSettings = ProjectSettings.get(projectId,
                                                  "My project",
                                                  "My project description",
                                                  DictionaryLanguage.rdfsLabel("fr"),
                                                  DisplayNameSettings.get(ImmutableList.of(), ImmutableList.of()),
                                                  SlackIntegrationSettings.get("http://payloadurl"),
                                                  WebhookSettings.get(ImmutableList.of()),
                                                  EntityDeprecationSettings.empty());
        var creationSettings = EntityCrudKitSettings.get(EntityCrudKitPrefixSettings.get(), UuidSuffixSettings.get(),
                                                         GeneratedAnnotationsSettings.empty());
        var prefixDeclarations = ImmutableList.of(PrefixDeclaration.get("ex:", "http://example.org/hello/"));
        var tags = ImmutableList.of(Tag.get(TagId.createTagId(),
                                            projectId,
                                            "My Tag",
                                            "My tag description",
                                            Color.getWhite(),
                                            Color.getWhite(),
                                            ImmutableList.of(EntityIsDeprecatedCriteria.get())));
        var sharingSettings = new ProjectSharingSettings(
                projectId,
                Optional.empty(),
                ImmutableList.of(
                        new SharingSetting(PersonId.get("Someone"),
                                           SharingPermission.EDIT)
                )
        );

        var searchSettings = ProjectSearchSettings.get(
                projectId,
                ImmutableList.of()
        );

        var settings = AllProjectSettings.get(projectSettings, creationSettings, prefixDeclarations, tags, sharingSettings, searchSettings);
        JsonSerializationTestUtil.testSerialization(settings, AllProjectSettings.class);
    }
}
