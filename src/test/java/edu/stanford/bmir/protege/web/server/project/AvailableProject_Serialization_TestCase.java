package edu.stanford.bmir.protege.web.server.project;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.lang.DisplayNameSettings;
import edu.stanford.bmir.protege.web.server.project.AvailableProject;
import edu.stanford.bmir.protege.web.server.project.ProjectDetails;
import edu.stanford.bmir.protege.web.server.project.ProjectId;
import edu.stanford.bmir.protege.web.server.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.server.projectsettings.EntityDeprecationSettings;
import edu.stanford.bmir.protege.web.server.shortform.DictionaryLanguage;
import edu.stanford.bmir.protege.web.server.user.UserId;
import org.junit.Test;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-06
 */
public class AvailableProject_Serialization_TestCase {

    @Test
    public void shouldSerializeAvailableProject() throws IOException {
        var projectDetails = ProjectDetails.get(ProjectId.get("12345678-1234-1234-1234-123456789abc"),
                                                "The display name",
                                                "The description",
                                                UserId.getUserId("The Owner"),
                                                true,
                                                DictionaryLanguage.rdfsLabel("en-GB"),
                                                DisplayNameSettings.get(ImmutableList.of(DictionaryLanguage.rdfsLabel("en-GB"),
                                                                                         DictionaryLanguage.rdfsLabel("en"),
                                                                                         DictionaryLanguage.rdfsLabel("")),
                                                                        ImmutableList.of(DictionaryLanguage.rdfsLabel("de"))),
                                                2L,
                                                UserId.getUserId("The creator"),
                                                3L,
                                                UserId.getUserId("The modifier"),
                                                EntityDeprecationSettings.empty());
        var availableProject = AvailableProject.get(ProjectId.get("12345678-1234-1234-1234-123456789abc"),
                                                    "The display name",
                                                    "The description",
                                                    UserId.getUserId("The Owner"),
                                                    true, 1L, UserId.getUserId("Created By"),
                                                    2L,
                                                    UserId.getUserId("Modified By"),
                                                    true,
                                                    true,
                                                    4L);
        JsonSerializationTestUtil.testSerialization(availableProject, AvailableProject.class);
    }
}
