package edu.stanford.bmir.protege.web.server.perspective;

import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.shared.perspective.Node;
import edu.stanford.bmir.protege.web.shared.perspective.TerminalNode;
import edu.stanford.bmir.protege.web.shared.perspective.TerminalNodeId;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-30
 */
public class TerminalNode_Serialization_TestCase {

    private TerminalNode terminalNode;

    @Before
    public void setUp() throws Exception {
        terminalNode = new TerminalNode(TerminalNodeId.get());
    }

    @Test
    public void shouldSerializeTerminalNode() throws IOException {
        JsonSerializationTestUtil.testSerialization(terminalNode, Node.class);
    }
}
