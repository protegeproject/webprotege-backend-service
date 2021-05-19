package edu.stanford.protege.webprotege.persistence;

import edu.stanford.protege.webprotege.user.EmailAddress;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/20/13
 */
public class EmailAddressWriteConverterTestCase {

    @Test
    public void convertShoudldReturnStringEqualToSuppliedEmailAddress() {
        EmailAddressWriteConverter converter = new EmailAddressWriteConverter();
        String suppliedAddress = "jane.doe@stanford.edu";
        EmailAddress emailAddress = new EmailAddress(suppliedAddress);
        String convertedAddress = converter.convert(emailAddress);
        assertEquals(suppliedAddress, convertedAddress);
    }
}
