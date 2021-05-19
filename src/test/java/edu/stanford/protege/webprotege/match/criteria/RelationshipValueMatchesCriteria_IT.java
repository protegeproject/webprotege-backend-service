package edu.stanford.protege.webprotege.match.criteria;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.jackson.ObjectMapperProvider;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-04
 */
public class RelationshipValueMatchesCriteria_IT {

    private RelationshipValueMatchesCriteria criteria = RelationshipValueMatchesCriteria.get(
            CompositeRootCriteria.get(ImmutableList.of(EntityIsDeprecatedCriteria.get()),
                                      MultiMatchType.ALL)
    );

    @Test
    public void shouldSerialize_AnyRelationshipPropertyCriteria() throws IOException {
        testSerialization(criteria);
    }

    @Test
    public void shouldSerialize_AnyRelationshipPropertyCriteriaWithEntityCriteria() throws IOException {
        var ser = "{\"match\":\"ValueMatches\",\"matchCriteria\": {\"match\":\"EntityIsDeprecated\"}}";
        var mapperProvider = new ObjectMapperProvider();
        var mapper = mapperProvider.get();
        var read = mapper.readValue(ser, RelationshipValueCriteria.class);
        MatcherAssert.assertThat(read, Matchers.is(criteria));
    }

    private static <V extends RelationshipValueCriteria> void testSerialization(V value) throws IOException {
        JsonSerializationTestUtil.testSerialization(value, RelationshipValueCriteria.class);
    }
}
