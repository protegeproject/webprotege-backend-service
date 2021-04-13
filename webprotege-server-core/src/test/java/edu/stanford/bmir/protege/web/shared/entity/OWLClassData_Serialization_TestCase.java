package edu.stanford.bmir.protege.web.shared.entity;

import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.MockingUtils;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import org.junit.Test;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-08
 */
public class OWLClassData_Serialization_TestCase {

    @Test
    public void shouldSerializeOWLClassData() throws IOException {
        OWLClassData clsData = OWLClassData.get(MockingUtils.mockOWLClass(),
                                                ImmutableMap.of(DictionaryLanguage.localName(), "The short form"),
                                                true);
        JsonSerializationTestUtil.testSerialization(clsData, OWLEntityData.class);
    }
}
