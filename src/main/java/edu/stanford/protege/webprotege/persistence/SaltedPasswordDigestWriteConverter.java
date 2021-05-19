package edu.stanford.protege.webprotege.persistence;

import com.google.common.io.BaseEncoding;
import edu.stanford.protege.webprotege.auth.SaltedPasswordDigest;

import java.util.Locale;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 09/03/16
 */
public class SaltedPasswordDigestWriteConverter implements Converter<SaltedPasswordDigest, String> {

    @Override
    public String convert(SaltedPasswordDigest saltedPasswordDigest) {
        return BaseEncoding.base16().encode(saltedPasswordDigest.getBytes()).toLowerCase(Locale.ENGLISH);
    }
}
