package edu.stanford.bmir.protege.web.server.change;

import edu.stanford.bmir.protege.web.server.dispatch.Action;
import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.server.pagination.Page;
import org.junit.Test;

import java.io.IOException;


import static edu.stanford.bmir.protege.web.MockingUtils.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class GetWatchedEntityChanges_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = GetWatchedEntityChangesAction.create(mockProjectId(),
                                                          mockUserId());
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = GetWatchedEntityChangesResult.create(Page.emptyPage());
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}