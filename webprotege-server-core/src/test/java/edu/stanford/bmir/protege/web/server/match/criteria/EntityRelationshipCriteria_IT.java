package edu.stanford.bmir.protege.web.server.match.criteria;

import edu.stanford.bmir.protege.web.server.match.criteria.AnyRelationshipPropertyCriteria;
import edu.stanford.bmir.protege.web.server.match.criteria.AnyRelationshipValueCriteria;
import edu.stanford.bmir.protege.web.server.match.criteria.EntityRelationshipCriteria;
import edu.stanford.bmir.protege.web.server.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.server.match.RelationshipPresence;
import org.junit.Test;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-04
 */
public class EntityRelationshipCriteria_IT {

    @Test
    public void shouldSerialize_AnyRelationshipPropertyCriteria() throws IOException {
        testSerialization(EntityRelationshipCriteria.get(
                RelationshipPresence.AT_LEAST_ONE,
                AnyRelationshipPropertyCriteria.get(),
                AnyRelationshipValueCriteria.get()
        ));
    }

    private static <V extends EntityRelationshipCriteria> void testSerialization(V value) throws IOException {
        JsonSerializationTestUtil.testSerialization(value, EntityRelationshipCriteria.class);
    }

}
