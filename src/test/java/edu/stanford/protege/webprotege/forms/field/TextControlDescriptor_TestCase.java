
package edu.stanford.protege.webprotege.forms.field;

import edu.stanford.protege.webprotege.common.LanguageMap;
import org.hamcrest.Matchers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TextControlDescriptor_TestCase {

    private TextControlDescriptor textFieldDescriptor;

    private final LanguageMap placeholder = LanguageMap.of("en", "The placeholder");

    private final StringType stringType = StringType.SIMPLE_STRING;

    private final String specificLangTag = "";

    private final LineMode lineMode = LineMode.SINGLE_LINE;

    private final String pattern = "The pattern";

    private final LanguageMap patternViolationErrorMessage = LanguageMap.of("en", "The patternViolationErrorMessage");

    @BeforeEach
    public void setUp()
        throws Exception
    {
        textFieldDescriptor = new TextControlDescriptor(placeholder, stringType, specificLangTag, lineMode, pattern, patternViolationErrorMessage);
    }

    @Test
    public void shouldReturnSupplied_placeholder() {
        assertThat(textFieldDescriptor.getPlaceholder(), is(this.placeholder));
    }

    @SuppressWarnings("ConstantConditions")
//    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_stringType_IsNull() {
        new TextControlDescriptor(placeholder, null, specificLangTag, lineMode, pattern, patternViolationErrorMessage);
    }

    @Test
    public void shouldReturnSupplied_stringType() {
        assertThat(textFieldDescriptor.getStringType(), is(this.stringType));
    }

    @SuppressWarnings("ConstantConditions")
//    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_lineMode_IsNull() {
        new TextControlDescriptor(placeholder, stringType, specificLangTag, null, pattern, patternViolationErrorMessage);
    }

    @Test
    public void shouldReturnSupplied_lineMode() {
        assertThat(textFieldDescriptor.getLineMode(), is(this.lineMode));
    }

    @SuppressWarnings("ConstantConditions")
//    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_pattern_IsNull() {
        new TextControlDescriptor(placeholder, stringType, specificLangTag, lineMode, null, patternViolationErrorMessage);
    }

    @Test
    public void shouldReturnSupplied_pattern() {
        assertThat(textFieldDescriptor.getPattern(), is(this.pattern));
    }

    @SuppressWarnings("ConstantConditions")
//    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_patternViolationErrorMessage_IsNull() {
        new TextControlDescriptor(placeholder, stringType, specificLangTag, lineMode, pattern, null);
    }

    @Test
    public void shouldReturnSupplied_patternViolationErrorMessage() {
        assertThat(textFieldDescriptor.getPatternViolationErrorMessage(), is(this.patternViolationErrorMessage));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(textFieldDescriptor, is(textFieldDescriptor));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(textFieldDescriptor.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(textFieldDescriptor, is(new TextControlDescriptor(placeholder, stringType, specificLangTag, lineMode, pattern, patternViolationErrorMessage)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_placeholder() {
        assertThat(textFieldDescriptor, is(not(new TextControlDescriptor(LanguageMap.of("en", "String-1a3f1304-8c2f-48b2-8c60-3407b27d579f"), stringType, specificLangTag, lineMode, pattern, patternViolationErrorMessage))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_stringType() {
        assertThat(textFieldDescriptor, is(not(new TextControlDescriptor(placeholder, StringType.LANG_STRING, "de",  lineMode, pattern, patternViolationErrorMessage))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_lineMode() {
        assertThat(textFieldDescriptor, is(not(new TextControlDescriptor(placeholder, stringType, specificLangTag, LineMode.MULTI_LINE, pattern, patternViolationErrorMessage))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_pattern() {
        assertThat(textFieldDescriptor, is(not(new TextControlDescriptor(placeholder, stringType, specificLangTag, lineMode, "String-ac3398da-4b52-48b5-a858-1f8571134db7", patternViolationErrorMessage))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_patternViolationErrorMessage() {
        assertThat(textFieldDescriptor, is(not(new TextControlDescriptor(placeholder, stringType, specificLangTag, lineMode, pattern, LanguageMap.of("en", "String-1abd718d-0eb4-4530-b465-02aed8dd66a2")))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(textFieldDescriptor.hashCode(), is(new TextControlDescriptor(placeholder, stringType, specificLangTag, lineMode, pattern, patternViolationErrorMessage).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(textFieldDescriptor.toString(), Matchers.startsWith("TextControlDescriptor"));
    }

    @Test
    public void should_getAssociatedType() {
        assertThat(textFieldDescriptor.getAssociatedType(), is("TEXT"));
    }

    @Test
    public void should_getType() {
        assertThat(TextControlDescriptor.getType(), is("TEXT"));
    }

}
