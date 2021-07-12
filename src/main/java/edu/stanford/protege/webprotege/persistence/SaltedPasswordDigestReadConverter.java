package edu.stanford.protege.webprotege.persistence;

import com.google.common.io.BaseEncoding;
import edu.stanford.protege.webprotege.auth.SaltedPasswordDigest;

import java.util.Locale;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 09/03/16
 */
public class SaltedPasswordDigestReadConverter implements Converter<String, SaltedPasswordDigest> {

    @Override
    public SaltedPasswordDigest convert(String s) {
        byte [] bytes = BaseEncoding.base16().decode(s.toUpperCase(Locale.ENGLISH));
        return new SaltedPasswordDigest(bytes);
    }
}