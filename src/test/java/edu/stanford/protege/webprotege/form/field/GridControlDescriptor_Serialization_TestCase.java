package edu.stanford.protege.webprotege.form.field;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.lang.LanguageMap;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-05-05
 */
public class GridControlDescriptor_Serialization_TestCase {

    private GridControlDescriptor descriptor;

    @Before
    public void setUp() {
        descriptor = GridControlDescriptor.get(
                ImmutableList.of(
                        GridColumnDescriptor.get(
                                GridColumnId.get(UUID.randomUUID().toString()),
                                Optionality.OPTIONAL,
                                Repeatability.NON_REPEATABLE,
                                null,
                                LanguageMap.empty(),
                                TextControlDescriptor.getDefault()
                        )
                ),
                null
        );
    }

    @Test
    public void shouldRoundTrip() throws IOException {
        JsonSerializationTestUtil.testSerialization(descriptor, FormControlDescriptor.class);
    }
}
