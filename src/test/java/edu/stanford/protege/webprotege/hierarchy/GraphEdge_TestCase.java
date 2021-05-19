package edu.stanford.protege.webprotege.hierarchy;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-21
 */

import edu.stanford.protege.webprotege.MockingUtils;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 24/01/2014
 */
public class GraphEdge_TestCase {

    @Test
    public void edgesWithSameSuccessorAndSamePredecessorShouldBeEqual() {
        GraphNode predecessor = mock(GraphNode.class);
        GraphNode successor = mock(GraphNode.class);
        GraphEdge edgeA = new GraphEdge(predecessor, successor);
        GraphEdge edgeB = new GraphEdge(predecessor, successor);
        assertEquals(edgeA, edgeB);
    }

    @Test
    public void edgesWithSameSuccessorAndSamePredecessorShouldHaveEqualHashCodes() {
        GraphNode predecessor = mock(GraphNode.class);
        GraphNode successor = mock(GraphNode.class);
        GraphEdge edgeA = new GraphEdge(predecessor, successor);
        GraphEdge edgeB = new GraphEdge(predecessor, successor);
        assertEquals(edgeA.hashCode(), edgeB.hashCode());
    }

    @Test
    public void getPredecessorShouldReturnSuppliedPredecessor() {
        GraphNode predecessor = mock(GraphNode.class);
        GraphNode successor = mock(GraphNode.class);
        GraphEdge edge = new GraphEdge(predecessor, successor);
        assertEquals(predecessor, edge.getPredecessor());
    }

    @Test
    public void getSuccessorShouldReturnSuppliedSuccessor() {
        GraphNode predecessor = mock(GraphNode.class);
        GraphNode successor = mock(GraphNode.class);
        GraphEdge edge = new GraphEdge(predecessor, successor);
        assertEquals(predecessor, edge.getPredecessor());
    }

    @Test
    public void shouldSerializeAsJson() throws IOException {
        JsonSerializationTestUtil.testSerialization(
                new GraphEdge<>(
                        new GraphNode<>(MockingUtils.mockOWLClassNode()),
                        new GraphNode<>(MockingUtils.mockOWLClassNode())
                ),
                GraphEdge.class
        );
    }
}
