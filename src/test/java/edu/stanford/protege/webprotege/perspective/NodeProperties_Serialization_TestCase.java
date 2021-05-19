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
public class NodeProperties_Serialization_TestCase {

    private NodeProperties nodeProperties;

    @Before
    public void setUp() throws Exception {
        nodeProperties = NodeProperties.builder()
                .setValue("a", "b")
                .build();
    }

    @Test
    public void shouldSerializeNodeProperties() throws IOException {
        JsonSerializationTestUtil.testSerialization(nodeProperties, NodeProperties.class);
    }
}
