package edu.stanford.protege.webprotege.perspective;

import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import edu.stanford.protege.webprotege.project.ProjectId;
import edu.stanford.protege.webprotege.user.UserId;
import org.junit.Test;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-30
 */
public class PerspectiveLayoutRecord_Serialization_TestCase {

    @Test
    public void shouldSerializeRecordWithoutUserId() throws IOException {
        var record = PerspectiveLayoutRecord.get(ProjectId.getNil(), null, PerspectiveId.generate(), null);
        JsonSerializationTestUtil.testSerialization(record, PerspectiveLayoutRecord.class);
    }

    @Test
    public void shouldSerializeRecordWithUserId() throws IOException {
        var record = PerspectiveLayoutRecord.get(ProjectId.getNil(), UserId.getUserId("Matthew"), PerspectiveId.generate(), null);
        JsonSerializationTestUtil.testSerialization(record, PerspectiveLayoutRecord.class);
    }
}
