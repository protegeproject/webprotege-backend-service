package edu.stanford.protege.webprotege.crud.supplied;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.protege.webprotege.jackson.ObjectMapperProvider;
import edu.stanford.protege.webprotege.crud.EntityCrudKitSuffixSettings;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-07
 */
public class SuppliedNameSuffixSettings_SerializationTestCase {

    @Test
    public void shouldRoundTrip() throws IOException {
        var settings = SuppliedNameSuffixSettings.get(WhiteSpaceTreatment.REPLACE_WITH_DASHES);
        JsonSerializationTestUtil.testSerialization(settings, EntityCrudKitSuffixSettings.class);
    }

    @Test
    public void shouldDeserializeFullClassNameForBackwardsCompatibility() throws IOException {
        var serialization = "{\"_class\" : \"edu.stanford.protege.webprotege.crud.supplied.SuppliedNameSuffixSettings\"}";
        ObjectMapperProvider objectMapperProvider = new ObjectMapperProvider();
        ObjectMapper objectMapper = objectMapperProvider.get();
        var deserializedObject = objectMapper.readValue(serialization, EntityCrudKitSuffixSettings.class);
        assertThat(deserializedObject, is(SuppliedNameSuffixSettings.get()));
    }
}
