package edu.stanford.protege.webprotege.viz;

import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-11
 */
public class EntityGraphFilter_SerializationTestCase {

    @Test
    public void shouldSerialize_EntityGraphFilter() throws IOException {
        testSerialization(EntityGraphFilter.get(FilterName.get("TheName"),
                                                "TheDescription",
                                                CompositeEdgeCriteria.anyEdge(),
                                                CompositeEdgeCriteria.noEdge(),
                                                true));
    }

    private static <V extends EntityGraphFilter> void testSerialization(V value) throws IOException {
        JsonSerializationTestUtil.testSerialization(value, EntityGraphFilter.class);
    }
}
