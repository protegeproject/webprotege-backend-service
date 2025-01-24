package edu.stanford.protege.webprotege.persistence;

import edu.stanford.protege.webprotege.common.UserId;
import org.junit.jupiter.api.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/20/13
 */
public class UserIdReadConverterTestCase {

    @Test
    public void convertShouldReturnUserIdWithSuppliedUserName() {
        UserIdReadConverter converter = new UserIdReadConverter();
        String suppliedName = "janedoe";
        UserId UserId = converter.convert(suppliedName);
        assertEquals(suppliedName, UserId.id());
    }
}
