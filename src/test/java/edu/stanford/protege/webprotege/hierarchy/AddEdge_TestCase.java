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
public class AddEdge_TestCase<T extends Serializable> {

    @Mock
    private GraphEdge<T> edge;

    @Test
    public void equalsShouldReturnTrueForEqualEdges() {
        AddEdge<T> addEdgeA = new AddEdge<>(edge);
        AddEdge<T> addEdgeB = new AddEdge<>(edge);
        assertThat(addEdgeA, equalTo(addEdgeB));
    }

    @Test
    public void hashCodeShouldReturnTrueForEqualEdges() {
        AddEdge<T> addEdgeA = new AddEdge<>(edge);
        AddEdge<T> addEdgeB = new AddEdge<>(edge);
        assertThat(addEdgeA.hashCode(), equalTo(addEdgeB.hashCode()));
    }

    @Test
    public void equalToNullShouldReturnFalse() {
        AddEdge<T> addEdge = new AddEdge<>(edge);
        assertThat(addEdge.equals(null), equalTo(false));
    }

    @Test
    public void shouldSerializeAsJson() throws IOException {
        JsonSerializationTestUtil.testSerialization(
                new AddEdge<>(
                        new GraphEdge<>(
                                new GraphNode<>(MockingUtils.mockOWLClassNode()),
                                new GraphNode<>(MockingUtils.mockOWLClassNode())
                        )
                ),
                GraphModelChange.class
        );
    }
}
