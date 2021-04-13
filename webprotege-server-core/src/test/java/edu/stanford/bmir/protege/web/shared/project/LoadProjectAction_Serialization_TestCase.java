package edu.stanford.bmir.protege.web.shared.project;

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
public class LoadProjectAction_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = new LoadProjectAction(ProjectId.getNil());
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = LoadProjectResult.get(ProjectId.getNil(), UserId.getGuest(),
                                           ProjectDetails.get(
                                                   ProjectId.getNil(),
                                                   "The display name",
                                                   "The description",
                                                   UserId.getGuest(),
                                                   false, DictionaryLanguage.localName(),
                                                   DisplayNameSettings.empty(),
                                                   1,
                                                   UserId.getGuest(),
                                                   2,
                                                   UserId.getGuest(),
                                                   EntityDeprecationSettings.empty()
                                           ));
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}
