package edu.stanford.protege.webprotege.persistence;

import edu.stanford.protege.webprotege.user.EmailAddress;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/20/13
 */

public class EmailAddressReadConverter implements Converter<String, EmailAddress> {

    public EmailAddress convert(String source) {
        return new EmailAddress(source);
    }
}