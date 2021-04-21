package edu.stanford.bmir.protege.web.server.perspective;

import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.shared.perspective.Direction;
import edu.stanford.bmir.protege.web.shared.perspective.Node;
import edu.stanford.bmir.protege.web.shared.perspective.ParentNode;
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
