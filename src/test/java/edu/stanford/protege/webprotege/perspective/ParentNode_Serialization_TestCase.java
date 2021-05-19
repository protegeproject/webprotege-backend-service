package edu.stanford.protege.webprotege.perspective;

import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-30
 */
public class ParentNode_Serialization_TestCase {

    private ParentNode parentNode;

    @Before
    public void setUp() throws Exception {
        parentNode = new ParentNode(Direction.ROW);
    }

    @Test
    public void shouldSerializeParentNode() throws IOException {
        JsonSerializationTestUtil.testSerialization(parentNode, Node.class);
    }
}
