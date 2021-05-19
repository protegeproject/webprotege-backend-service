package edu.stanford.protege.webprotege.inject;

import dagger.Module;
import dagger.Provides;
import edu.stanford.protege.webprotege.auth.Md5MessageDigestAlgorithm;
import edu.stanford.protege.webprotege.auth.MessageDigestAlgorithm;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17/02/15
 */
@Module
public class AuthenticationModule {

    @Provides
    public MessageDigestAlgorithm provide(Md5MessageDigestAlgorithm algorithm) {
        return algorithm;
    }
}
