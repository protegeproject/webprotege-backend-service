package edu.stanford.protege.webprotege.shortform;

import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;

public class ShortForm_Serialization_TestCase {

    @Test
    public void shouldSerializeLocalNameShortForm() throws IOException {
        var shortForm = ShortForm.get(
                DictionaryLanguage.localName(),
                "Hello"
        );
        JsonSerializationTestUtil.testSerialization(shortForm, ShortForm.class);
    }

    @Test
    public void shouldSerializeAnnotationBasedShortFormWithEmptyLangTag() throws IOException {
        var shortForm = ShortForm.get(
                DictionaryLanguage.rdfsLabel(""),
                "Hello"
        );
        JsonSerializationTestUtil.testSerialization(shortForm, ShortForm.class);
    }

    @Test
    public void shouldSerializeAnnotationBasedShortFormWithNonEmptyLangTag() throws IOException {
        var shortForm = ShortForm.get(
                DictionaryLanguage.rdfsLabel("en"),
                "Hello"
        );
        JsonSerializationTestUtil.testSerialization(shortForm, ShortForm.class);
    }
}
