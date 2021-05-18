package edu.stanford.bmir.protege.web.server.util;

import javax.annotation.Nonnull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Jul 2017
 */
public class UUIDUtil {

    /**
     * A regular expression that specifies a pattern for a UUID
     */
    public static final transient String UUID_PATTERN = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";

    private static final transient Pattern REG_EXP = Pattern.compile(UUID_PATTERN);

    private static final String ZEROED_OUT = "00000000-0000-0000-0000-000000000000";

    /**
     * Checks that the specified string matches the UUID pattern {@link #UUID_PATTERN}.
     * @param id The string to check.
     * @return The specified string.
     */
    public static boolean isWellFormed(@Nonnull String id) {
        Matcher matcher = REG_EXP.matcher(checkNotNull(id));
        return matcher.matches();
    }

    /**
     * Get the regular expression that specifies the lexical format of UUIDs.  The returned regular expression
     * specifies a UUID format consisting of a series of characters from the range a-z0-9 separated by dashes.  The
     * first block contains 8 characters, the second block 4 characters, the third block 4 characters, the fourth
     * block 4 characters, and the fifth block 12 characters.  For example, cb88785a-bfc5-4299-9b5b-7920451aba06.
     * @return The {@link Pattern} for UUID lexical values.  Not {@code null}.
     */
    public static Pattern getIdRegExp() {
        return REG_EXP;
    }

    /**
     * Gets the nill UUID.  This is the UUID with all bits set to zero.
     */
    public static String getNilUuid() {
        return ZEROED_OUT;
    }
}
