package edu.stanford.protege.webprotege.app;

import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import edu.stanford.protege.webprotege.user.EmailAddress;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class SetApplicationSettings_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = SetApplicationSettingsAction.create(new ApplicationSettings(
                "Name",
                new EmailAddress("Email"),
                new ApplicationLocation("scheme", "host", "path", 20),
                AccountCreationSetting.ACCOUNT_CREATION_ALLOWED,
                Collections.emptyList(),
                ProjectCreationSetting.EMPTY_PROJECT_CREATION_ALLOWED,
                Collections.emptyList(),
                ProjectUploadSetting.PROJECT_UPLOAD_ALLOWED,
                Collections.emptyList(),
                NotificationEmailsSetting.SEND_NOTIFICATION_EMAILS,
                300L
        ));
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = SetApplicationSettingsResult.create();
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}