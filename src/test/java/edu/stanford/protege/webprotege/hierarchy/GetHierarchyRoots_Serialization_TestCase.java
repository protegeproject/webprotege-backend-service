package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import edu.stanford.protege.webprotege.project.ProjectId;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class GetHierarchyRoots_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = GetHierarchyRootsAction.create(ProjectId.getNil(),
                                                    HierarchyId.CLASS_HIERARCHY);
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = GetHierarchyRootsResult.create(Collections.emptyList());
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}