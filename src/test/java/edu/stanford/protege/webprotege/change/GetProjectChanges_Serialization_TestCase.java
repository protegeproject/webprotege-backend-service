package edu.stanford.protege.webprotege.change;

import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import edu.stanford.protege.webprotege.pagination.Page;
import edu.stanford.protege.webprotege.pagination.PageRequest;
import org.junit.Test;

import java.io.IOException;
import java.util.Optional;


import static edu.stanford.protege.webprotege.MockingUtils.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class GetProjectChanges_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = GetProjectChangesAction.create(mockProjectId(),
                                                    Optional.empty(),
                                                    PageRequest.requestFirstPage());
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = GetProjectChangesResult.create(Page.emptyPage());
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}