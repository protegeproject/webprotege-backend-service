package edu.stanford.bmir.protege.web.shared.project;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.csv.DocumentId;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.lang.DisplayNameSettings;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.shared.projectsettings.EntityDeprecationSettings;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.Test;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-06
 */
public class CreateNewProject_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = new CreateNewProjectAction(NewProjectSettings.get(UserId.getGuest(),
                                                                       "DisplayName",
                                                                       "en",
                                                                       "The description",
                                                                       new DocumentId("TheDocId")
        ));
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = new CreateNewProjectResult(ProjectDetails.get(
                ProjectId.getNil(),
                "The display name",
                "The description",
                UserId.getGuest(),
                true, DictionaryLanguage.localName(),
                DisplayNameSettings.get(ImmutableList.of(), ImmutableList.of()),
                1,
                UserId.getGuest(),
                3,
                UserId.getGuest(),
                EntityDeprecationSettings.empty()
        ));
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}
