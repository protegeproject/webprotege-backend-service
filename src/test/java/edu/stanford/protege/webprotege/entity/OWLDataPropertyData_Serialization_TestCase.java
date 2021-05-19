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
public class OWLDataPropertyData_Serialization_TestCase {

    @Test
    public void shouldSerializeOWLDataPropertyData() throws IOException {
        OWLDataPropertyData data = OWLDataPropertyData.get(MockingUtils.mockOWLDataProperty(),
                                                           ImmutableMap.of(DictionaryLanguage.localName(), "The short form"),
                                                           true);
        JsonSerializationTestUtil.testSerialization(data, OWLEntityData.class);
    }
}
