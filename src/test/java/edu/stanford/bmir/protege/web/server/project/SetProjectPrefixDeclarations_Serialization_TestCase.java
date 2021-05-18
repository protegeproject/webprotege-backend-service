package edu.stanford.bmir.protege.web.server.project;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.dispatch.Action;
import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.project.PrefixDeclaration;
import edu.stanford.bmir.protege.web.server.project.SetProjectPrefixDeclarationsAction;
import edu.stanford.bmir.protege.web.server.project.SetProjectPrefixDeclarationsResult;
import edu.stanford.bmir.protege.web.server.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;


import static edu.stanford.bmir.protege.web.MockingUtils.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class SetProjectPrefixDeclarations_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = SetProjectPrefixDeclarationsAction.create(mockProjectId(), ImmutableList.of(
                PrefixDeclaration.get("hello:", "http://example.org/")
        ));
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = SetProjectPrefixDeclarationsResult.create(mockProjectId(),
                                                               ImmutableList.of());
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}