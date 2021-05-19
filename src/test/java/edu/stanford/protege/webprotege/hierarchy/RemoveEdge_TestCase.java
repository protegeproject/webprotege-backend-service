package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.MockingUtils;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.io.Serializable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-21
 */
@RunWith(MockitoJUnitRunner.class)
public class RemoveEdge_TestCase<T extends Serializable> {

    @Mock
    private GraphEdge<T> edge;

    @Test
    public void equalsShouldReturnTrueForEqualEdges() {
        RemoveEdge<T> removeEdgeA = new RemoveEdge<>(edge);
        RemoveEdge<T> removeEdgeB = new RemoveEdge<>(edge);
        assertThat(removeEdgeA, equalTo(removeEdgeB));
    }

    @Test
    public void hashCodeShouldReturnTrueForEqualEdges() {
        RemoveEdge<T> removeEdgeA = new RemoveEdge<>(edge);
        RemoveEdge<T> removeEdgeB = new RemoveEdge<>(edge);
        assertThat(removeEdgeA.hashCode(), equalTo(removeEdgeB.hashCode()));
    }

    @Test
    public void equalToNullShouldReturnFalse() {
        RemoveEdge<T> removeEdge = new RemoveEdge<>(edge);
        assertThat(removeEdge.equals(null), equalTo(false));
    }

    @Test
    public void shouldSerializeAsJson() throws IOException {
        JsonSerializationTestUtil.testSerialization(
                new RemoveEdge<>(
                        new GraphEdge<>(
                                new GraphNode<>(MockingUtils.mockOWLClassNode()),
                                new GraphNode<>(MockingUtils.mockOWLClassNode())
                        )
                ),
                GraphModelChange.class
        );
    }
}
