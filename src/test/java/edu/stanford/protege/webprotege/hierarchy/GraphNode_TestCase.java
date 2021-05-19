package edu.stanford.protege.webprotege.hierarchy;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.MockingUtils;
import edu.stanford.protege.webprotege.entity.EntityNode;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.io.Serializable;

import static org.junit.Assert.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-21
 */
@RunWith(MockitoJUnitRunner.class)
public class GraphNode_TestCase<T extends Serializable> {

    @Mock
    private T userObject;

    @Mock
    private T otherUserObject;

    @Test
    public void shouldReturnEqualForNodesWithEqualUserObjects() {
        GraphNode<T> nodeA = new GraphNode<>(userObject, false);
        GraphNode<T> nodeB = new GraphNode<>(userObject, false);
        assertEquals(nodeA, nodeB);
    }

    @Test
    public void shouldReturnEqualHashCodesForNodesWithEqualUserObjects() {
        GraphNode<T> nodeA = new GraphNode<>(userObject, false);
        GraphNode<T> nodeB = new GraphNode<>(userObject, false);
        assertEquals(nodeA.hashCode(), nodeB.hashCode());
    }

    @Test
    public void shouldReturnEqualForNodesWithEqualUserObjectsButDifferentSinkFlags() {
        GraphNode<T> nodeA = new GraphNode<>(userObject, false);
        GraphNode<T> nodeB = new GraphNode<>(userObject, true);
        assertEquals(nodeA, nodeB);
    }

    @Test
    public void shouldReturnNotEqualForNodesWithDifferentUserObjects() {
        GraphNode<T> nodeA = new GraphNode<>(userObject, false);
        GraphNode<T> nodeB = new GraphNode<>(otherUserObject, false);
        assertFalse(nodeA.equals(nodeB));
    }

    @Test
    public void shouldReturnFalseForEqualsCalledWithNullArgument() {
        GraphNode<T> node = new GraphNode<>(userObject);
        assertFalse(node.equals(null));
    }

    @Test
    public void isSinkShouldReturnSuppliedSinkFlag() {
        GraphNode<T> node = new GraphNode<>(userObject, true);
        assertTrue(node.isSink());
    }

    @Test
    public void shouldSerializeAsJson() throws IOException {
        var graphNode = new GraphNode<>(EntityNode.get(MockingUtils.mockOWLClass(),
                                                       "Hello",
                                                       ImmutableList.of(),
                                                       true, ImmutableSet.of(),
                                                       3,
                                                       ImmutableSet.of()));
        JsonSerializationTestUtil.testSerialization(graphNode, GraphNode.class);
    }
}
