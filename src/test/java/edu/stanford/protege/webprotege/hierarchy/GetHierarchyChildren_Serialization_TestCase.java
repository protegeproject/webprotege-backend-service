package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import edu.stanford.protege.webprotege.pagination.Page;
import edu.stanford.protege.webprotege.pagination.PageRequest;
import edu.stanford.protege.webprotege.project.ProjectId;
import org.junit.Test;

import java.io.IOException;


import static edu.stanford.protege.webprotege.MockingUtils.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class GetHierarchyChildren_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = GetHierarchyChildrenAction.create(ProjectId.getNil(),
                                                       mockOWLClass(),
                                                       HierarchyId.CLASS_HIERARCHY,
                                                       PageRequest.requestFirstPage());
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = GetHierarchyChildrenResult.create(GraphNode.get(mockOWLClassNode(), true),
                                                       Page.emptyPage());
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}