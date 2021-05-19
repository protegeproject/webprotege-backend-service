package edu.stanford.protege.webprotege.crud.obo;

import edu.stanford.protege.webprotege.crud.uuid.UuidFormat;
import edu.stanford.protege.webprotege.crud.uuid.UuidSuffixKit;
import edu.stanford.protege.webprotege.crud.uuid.UuidSuffixSettings;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/08/2013
 */
public class UuidSuffixSettingsTestCase {

    public static final String EXPECTED_BASE_62_PREFIX = "R";

    public static final String EXPECTED_STANDARD_PREFIX = "id-";

    @Test
    public void getKitIdReturnsExpectedId() {
        UuidSuffixSettings suffixSettings = UuidSuffixSettings.get();
        assertEquals(UuidSuffixKit.getId(), suffixSettings.getKitId());
    }

    @Test
    public void differentObjectsAreEqualAndHaveEqualHashCodes() {
        UuidSuffixSettings settingsA = UuidSuffixSettings.get();
        UuidSuffixSettings settingsB = UuidSuffixSettings.get();
        assertEquals(settingsA, settingsB);
        assertEquals(settingsA.hashCode(), settingsB.hashCode());
    }

    @Test
    public void shouldBeBase62ByDefault() {
        UuidSuffixSettings settings = UuidSuffixSettings.get();
        assertSame(settings.getUuidFormat(), UuidFormat.BASE62);
    }

    @Test
    public void shouldProvideBase62Prefix() {
        UuidSuffixSettings settings = UuidSuffixSettings.get(UuidFormat.BASE62);
        assertEquals(EXPECTED_BASE_62_PREFIX, settings.getIdPrefix());
    }

    @Test
    public void shouldProvideStandardPrefix() {
        UuidSuffixSettings settings = UuidSuffixSettings.get(UuidFormat.STANDARD);
        assertEquals(EXPECTED_STANDARD_PREFIX, settings.getIdPrefix());
    }

    @Test
    public void shouldAcceptNullForBackwardCompatibility() {
        UuidSuffixSettings settings = UuidSuffixSettings.get(null);
        assertEquals(settings.getUuidFormat(), UuidFormat.BASE62);
    }
}
