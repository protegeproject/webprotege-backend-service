package edu.stanford.protege.webprotege.crud.obo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.jackson.ObjectMapperProvider;
import edu.stanford.protege.webprotege.crud.EntityCrudKitSuffixSettings;
import edu.stanford.protege.webprotege.crud.oboid.OboIdSuffixSettings;
import edu.stanford.protege.webprotege.crud.oboid.UserIdRange;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import edu.stanford.protege.webprotege.user.UserId;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-07
 */
public class OboIdSuffixSettings_SerializationTestCase {

    public static final int TOTAL_DIGITS = 77;

    public static final int START = 100;

    public static final int END = 203;

    public static final UserId THE_USER = UserId.getUserId("TheUser");

    private OboIdSuffixSettings settings;

    @Before
    public void setUp() {
        settings = OboIdSuffixSettings.get(
                TOTAL_DIGITS,
                ImmutableList.of(
                        UserIdRange.get(THE_USER, START, END)
                )
        );
    }

    @Test
    public void shouldRoundTripSettings() throws IOException {
        JsonSerializationTestUtil.testSerialization(settings, EntityCrudKitSuffixSettings.class);
    }

    @Test
    public void shouldDeserializeLongClassNameForBackwardsCompatibility() throws IOException {
        var serialization = "{\"_class\":\"edu.stanford.bmir.protege.web.shared.crud.oboid.OBOIdSuffixSettings\",\"totalDigits\":77,\"userIdRanges\":[{\"userId\":\"TheUser\",\"start\":100,\"end\":203}]}";
        ObjectMapperProvider objectMapperProvider = new ObjectMapperProvider();
        ObjectMapper objectMapper = objectMapperProvider.get();
        var deserializedObject = objectMapper.readValue(serialization, EntityCrudKitSuffixSettings.class);
        assertThat(deserializedObject, is(settings));
    }
}
