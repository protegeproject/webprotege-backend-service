package edu.stanford.protege.webprotege.perspective;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import edu.stanford.protege.webprotege.project.ProjectId;
import edu.stanford.protege.webprotege.user.UserId;
import org.junit.Test;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-01
 */
public class PerspectiveDescriptorsRecord_Serialization_TestCase {

    @Test
    public void shouldSerialize() throws IOException {
        var record = PerspectiveDescriptorsRecord.get(ProjectId.getNil(),
                                                      UserId.getUserId("Matthew"), ImmutableList.of());
        JsonSerializationTestUtil.testSerialization(record, PerspectiveDescriptorsRecord.class);
    }
}
