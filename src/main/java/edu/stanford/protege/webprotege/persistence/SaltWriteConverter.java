package edu.stanford.protege.webprotege.persistence;

import com.google.common.io.BaseEncoding;
import edu.stanford.protege.webprotege.auth.Salt;

import java.util.Locale;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 09/03/16
 */
public class SaltWriteConverter implements Converter<Salt, String> {

    @Override
    public String convert(Salt salt) {
        return BaseEncoding.base16().encode(salt.getBytes()).toLowerCase(Locale.ENGLISH);
    }
}
