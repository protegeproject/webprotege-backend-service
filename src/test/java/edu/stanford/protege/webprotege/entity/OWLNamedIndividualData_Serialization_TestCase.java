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
public class OWLNamedIndividualData_Serialization_TestCase {

    @Test
    public void shouldSerializeOWLNamedIndividualData() throws IOException {
        OWLNamedIndividualData data = OWLNamedIndividualData.get(MockingUtils.mockOWLNamedIndividual(),
                                                                 ImmutableMap.of(DictionaryLanguage.localName(), "The short form"),
                                                                 true);
        JsonSerializationTestUtil.testSerialization(data, OWLEntityData.class);
    }
}
