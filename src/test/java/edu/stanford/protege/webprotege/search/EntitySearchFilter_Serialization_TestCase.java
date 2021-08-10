package edu.stanford.protege.webprotege.search;

import edu.stanford.protege.webprotege.lang.LanguageMap;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import edu.stanford.protege.webprotege.match.criteria.EntityIsDeprecatedCriteria;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-15
 */
public class EntitySearchFilter_Serialization_TestCase {

    @Test
    public void shouldSerializeAsJson() throws IOException {
        EntitySearchFilter in = EntitySearchFilter.get(EntitySearchFilterId.createFilterId(),
                                                       ProjectId.valueOf(UUID.randomUUID().toString()),
                                                       LanguageMap.of("en", "Hello"),
                                                       EntityIsDeprecatedCriteria.get());

        JsonSerializationTestUtil.testSerialization(in, EntitySearchFilter.class);
    }

}
