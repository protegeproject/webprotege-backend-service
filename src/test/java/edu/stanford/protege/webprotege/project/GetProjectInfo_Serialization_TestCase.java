package edu.stanford.protege.webprotege.project;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.lang.DisplayNameSettings;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import edu.stanford.protege.webprotege.projectsettings.EntityDeprecationSettings;
import edu.stanford.protege.webprotege.projectsettings.ProjectSettings;
import edu.stanford.protege.webprotege.projectsettings.SlackIntegrationSettings;
import edu.stanford.protege.webprotege.projectsettings.WebhookSettings;
import edu.stanford.protege.webprotege.shortform.DictionaryLanguage;
import org.junit.Test;

import java.io.IOException;


import static edu.stanford.protege.webprotege.MockingUtils.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class GetProjectInfo_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = GetProjectInfoAction.create(mockProjectId());
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = GetProjectInfoResult.create(ProjectSettings.get(
                mockProjectId(),
                "The display name",
                "The description",
                DictionaryLanguage.localName(),
                DisplayNameSettings.empty(),
                SlackIntegrationSettings.get("url"),
                WebhookSettings.get(ImmutableList.of()),
                EntityDeprecationSettings.empty()
        ), ImmutableList.of());
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}