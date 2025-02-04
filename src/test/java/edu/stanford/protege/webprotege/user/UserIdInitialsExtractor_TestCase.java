package edu.stanford.protege.webprotege.user;

import edu.stanford.protege.webprotege.common.UserId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10/03/16
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserIdInitialsExtractor_TestCase {

    private UserId userId = edu.stanford.protege.webprotege.MockingUtils.mockUserId();

    @Test
    public void shouldExtractFirstAndLastNameInitials() {
        extractInitials("First Last", "FL");
    }

    @Test
    public void shouldExtractFirstAndLastNameInitialsIgnoringMiddleName() {
        extractInitials("First Middle Last", "FL");
    }

    @Test
    public void shouldExtractFirstNameInitial() {
        extractInitials("First", "F");
    }

    @Test
    public void shouldExtractInitialsAroundDots() {
        extractInitials("First.Last", "FL");
    }

    @Test
    public void shouldNotIncludeQuotationMarks() {
        extractInitials("\"First Last\"", "FL");
    }

    @Test
    public void shouldExtractIntialsFromEmailAddress() {
        extractInitials("First.Last@email.com", "FL");
    }

    @Test
    public void shouldIgnoreCase() {
        extractInitials("first last", "FL");
    }

    @Test
    public void shouldExtractTwitterName() {
        extractInitials("@FirstLast", "FL");
    }

    @Test
    public void shouldExtractCamelCaseInitials() {
        extractInitials("FirstLast", "FL");
    }

    @Test
    public void shouldExtractHyphenSeparatedNames() {
        extractInitials("First-Last", "FL");
    }

    @Test
    public void shouldExtractUnderScoreSeparatedNames() {
        extractInitials("First_Last", "FL");
    }

    @Test
    public void shouldExtractPlainInitials() {
        extractInitials("FL", "FL");
    }

    @Test
    public void shouldExtractNumeralSeparatedNames() {
        extractInitials("First2Last", "FL");
    }

    @Test
    public void shouldExtractFirstAndLastWithInitialsRun() {
        extractInitials("FLast", "FL");
    }

    @Test
    public void shouldExtractFirstAndLastWithTripleInitialsRun() {
        extractInitials("FMLast", "FL");
    }

    private void extractInitials(String name, String expectedInitials) {
        userId = UserId.valueOf(expectedInitials);
        String initials = UserIdInitialsExtractor.getInitials(userId);
        assertThat(initials, is(expectedInitials));
    }

}
