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
public class AddRootNode_TestCase<T extends Serializable> {

    @Mock
    private GraphNode<T> node;

    @Test
    public void equalsShouldReturnTrueForEqualNodes() {
        AddRootNode<T> addRootNodeA = new AddRootNode<>(node);
        AddRootNode<T> addRootNodeB = new AddRootNode<>(node);
        assertThat(addRootNodeA, equalTo(addRootNodeB));
    }

    @Test
    public void hashCodeShouldReturnTrueForEqualNodes() {
        AddRootNode<T> addRootNodeA = new AddRootNode<>(node);
        AddRootNode<T> addRootNodeB = new AddRootNode<>(node);
        assertThat(addRootNodeA.hashCode(), equalTo(addRootNodeB.hashCode()));
    }

    @Test
    public void equalToNullShouldReturnFalse() {
        AddRootNode<T> addRootNode = new AddRootNode<>(node);
        assertThat(addRootNode.equals(null), equalTo(false));
    }

    @Test
    public void shouldSerializeAsJson() throws IOException {
        JsonSerializationTestUtil.testSerialization(
                new AddRootNode<>(
                        new GraphNode<>(MockingUtils.mockOWLClassNode())
                ),
                GraphModelChange.class
        );
    }
}
