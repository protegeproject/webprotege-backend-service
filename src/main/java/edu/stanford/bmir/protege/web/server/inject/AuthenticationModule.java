package edu.stanford.bmir.protege.web.server.inject;

import dagger.Module;
import dagger.Provides;
import edu.stanford.bmir.protege.web.server.auth.Md5MessageDigestAlgorithm;
import edu.stanford.bmir.protege.web.server.auth.MessageDigestAlgorithm;

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
