package edu.stanford.protege.webprotege.persistence;

import edu.stanford.protege.webprotege.common.UserId;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/20/13
 */
public class UserIdWriteConverterTestCase {

    @Test
    public void convertShouldReturnSuppliedUserName() {
        UserIdWriteConverter converter = new UserIdWriteConverter();
        String suppliedUserName = "janedoe";
        UserId userId = UserId.valueOf(suppliedUserName);
        String convertedUserName = converter.convert(userId);
        assertEquals(suppliedUserName, convertedUserName);
    }


}
