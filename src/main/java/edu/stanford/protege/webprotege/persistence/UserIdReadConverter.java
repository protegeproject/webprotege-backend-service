package edu.stanford.protege.webprotege.persistence;

import edu.stanford.protege.webprotege.user.UserId;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/20/13
 * <p>
 *     An implementation of {@link Converter} that reads a {@link UserId} from a {@link String}.
 * </p>
 */
public class UserIdReadConverter implements Converter<String, UserId> {

    public UserId convert(String userId) {
        return UserId.getUserId(userId);
    }
}
