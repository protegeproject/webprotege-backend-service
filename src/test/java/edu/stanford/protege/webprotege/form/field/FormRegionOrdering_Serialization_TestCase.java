package edu.stanford.protege.webprotege.form.field;

import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;

public class FormRegionOrdering_Serialization_TestCase {

    @Test
    public void shouldSerializeOrderBy() throws IOException {
        var orderBy = FormRegionOrdering.get(GridColumnId.get("12345678-1234-1234-1234-123456789abc"),
                                             FormRegionOrderingDirection.DESC);
        JsonSerializationTestUtil.testSerialization(orderBy, FormRegionOrdering.class);
    }
}
