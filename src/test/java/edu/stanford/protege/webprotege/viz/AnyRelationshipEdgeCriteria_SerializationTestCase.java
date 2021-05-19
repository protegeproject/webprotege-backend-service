package edu.stanford.protege.webprotege.viz;

import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-06
 */
public class AnyRelationshipEdgeCriteria_SerializationTestCase {

    @Test
    public void shouldSerialize_AnyRelationshipEdgeCriteria() throws IOException {
        testSerialization(AnyRelationshipEdgeCriteria.get());
    }

    private static <V extends EdgeCriteria> void testSerialization(V value) throws IOException {
        JsonSerializationTestUtil.testSerialization(value, EdgeCriteria.class);
    }
}
