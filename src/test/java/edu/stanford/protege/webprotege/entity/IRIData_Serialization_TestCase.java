package edu.stanford.protege.webprotege.entity;

import com.google.common.collect.ImmutableMap;
import edu.stanford.protege.webprotege.MockingUtils;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import edu.stanford.protege.webprotege.shortform.DictionaryLanguage;
import org.junit.Test;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-08
 */
public class IRIData_Serialization_TestCase {

    @Test
    public void shouldSerializeIRIData() throws IOException {
        IRIData data = IRIData.get(MockingUtils.mockIRI(),
                                   ImmutableMap.of(DictionaryLanguage.localName(), "The short form"));
        JsonSerializationTestUtil.testSerialization(data, IRIData.class);
    }
}
