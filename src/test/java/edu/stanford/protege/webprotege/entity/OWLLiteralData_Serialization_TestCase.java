package edu.stanford.protege.webprotege.entity;

import edu.stanford.protege.webprotege.MockingUtils;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
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
