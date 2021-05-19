package edu.stanford.protege.webprotege.projectsettings;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.color.Color;
import edu.stanford.protege.webprotege.crud.EntityCrudKitPrefixSettings;
import edu.stanford.protege.webprotege.crud.EntityCrudKitSettings;
import edu.stanford.protege.webprotege.crud.gen.GeneratedAnnotationsSettings;
import edu.stanford.protege.webprotege.crud.uuid.UuidSuffixSettings;
import edu.stanford.protege.webprotege.lang.DisplayNameSettings;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import edu.stanford.protege.webprotege.match.criteria.EntityIsDeprecatedCriteria;
import edu.stanford.protege.webprotege.project.PrefixDeclaration;
import edu.stanford.protege.webprotege.project.ProjectId;
import edu.stanford.protege.webprotege.search.ProjectSearchSettings;
import edu.stanford.protege.webprotege.sharing.PersonId;
import edu.stanford.protege.webprotege.sharing.ProjectSharingSettings;
import edu.stanford.protege.webprotege.sharing.SharingPermission;
import edu.stanford.protege.webprotege.sharing.SharingSetting;
import edu.stanford.protege.webprotege.shortform.DictionaryLanguage;
import edu.stanford.protege.webprotege.tag.Tag;
import edu.stanford.protege.webprotege.tag.TagId;
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
