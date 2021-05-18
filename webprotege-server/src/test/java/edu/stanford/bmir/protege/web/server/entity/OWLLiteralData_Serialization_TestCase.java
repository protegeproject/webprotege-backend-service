package edu.stanford.bmir.protege.web.server.entity;

import edu.stanford.bmir.protege.web.MockingUtils;
import edu.stanford.bmir.protege.web.server.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-08
 */
public class OWLLiteralData_Serialization_TestCase {

    @Test
    public void shouldSerializeOWLLiteralData() throws IOException {
        OWLLiteralData data = OWLLiteralData.get(MockingUtils.mockLiteral());
        JsonSerializationTestUtil.testSerialization(data, OWLLiteralData.class);
    }
}
