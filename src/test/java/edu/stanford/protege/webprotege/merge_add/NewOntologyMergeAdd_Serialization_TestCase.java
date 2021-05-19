package edu.stanford.protege.webprotege.merge_add;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;


import static edu.stanford.protege.webprotege.MockingUtils.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class NewOntologyMergeAdd_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = NewOntologyMergeAddAction.create(mockProjectId(),
                                                      mockDocumentId(),
                                                      "Iri",
                                                      ImmutableList.of());
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = NewOntologyMergeAddResult.create();
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}