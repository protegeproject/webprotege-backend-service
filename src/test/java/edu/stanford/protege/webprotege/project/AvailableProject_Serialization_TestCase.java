package edu.stanford.protege.webprotege.project;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.lang.DisplayNameSettings;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import edu.stanford.protege.webprotege.projectsettings.EntityDeprecationSettings;
import edu.stanford.protege.webprotege.shortform.DictionaryLanguage;
import edu.stanford.protege.webprotege.user.UserId;
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
