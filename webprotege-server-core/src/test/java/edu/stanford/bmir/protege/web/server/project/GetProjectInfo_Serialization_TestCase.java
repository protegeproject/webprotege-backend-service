package edu.stanford.bmir.protege.web.server.project;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.dispatch.Action;
import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.lang.DisplayNameSettings;
import edu.stanford.bmir.protege.web.server.project.GetProjectInfoAction;
import edu.stanford.bmir.protege.web.server.project.GetProjectInfoResult;
import edu.stanford.bmir.protege.web.server.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.server.projectsettings.EntityDeprecationSettings;
import edu.stanford.bmir.protege.web.server.projectsettings.ProjectSettings;
import edu.stanford.bmir.protege.web.server.projectsettings.SlackIntegrationSettings;
import edu.stanford.bmir.protege.web.server.projectsettings.WebhookSettings;
import edu.stanford.bmir.protege.web.server.shortform.DictionaryLanguage;
import org.junit.Test;

import java.io.IOException;


import static edu.stanford.bmir.protege.web.MockingUtils.*;

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