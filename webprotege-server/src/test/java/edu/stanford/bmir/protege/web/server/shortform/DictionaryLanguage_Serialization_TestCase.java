package edu.stanford.bmir.protege.web.server.shortform;

import edu.stanford.bmir.protege.web.server.shortform.AnnotationAssertionDictionaryLanguage;
import edu.stanford.bmir.protege.web.server.shortform.DictionaryLanguage;
import edu.stanford.bmir.protege.web.server.shortform.LocalNameDictionaryLanguage;
import edu.stanford.bmir.protege.web.server.shortform.OboIdDictionaryLanguage;
import edu.stanford.bmir.protege.web.server.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;

public class DictionaryLanguage_Serialization_TestCase {

    @Test
    public void shouldSerializeLocalNameDictionaryLanguage() throws IOException {
        var dictionaryLanguage = LocalNameDictionaryLanguage.get();
        JsonSerializationTestUtil.testSerialization(dictionaryLanguage, DictionaryLanguage.class);
    }

    @Test
    public void shouldSerializeOboIdDictionaryLanguage() throws IOException {
        var dictionaryLanguage = OboIdDictionaryLanguage.get();
        JsonSerializationTestUtil.testSerialization(dictionaryLanguage, DictionaryLanguage.class);
    }

    @Test
    public void shouldSerializeAnnotationBasedDictionaryLanguage() throws IOException {
        var dictionaryLanguage = AnnotationAssertionDictionaryLanguage.rdfsLabel("en");
        JsonSerializationTestUtil.testSerialization(dictionaryLanguage, DictionaryLanguage.class);
    }

    @Test
    public void shouldDeserializeLocalNameLegacySerialization() throws IOException {
        var localName = LocalNameDictionaryLanguage.get();
        JsonSerializationTestUtil.testDeserialization("{}", localName, DictionaryLanguage.class);
    }

    @Test
    public void shouldDeserializeAnnotationAssertionWithEmptyLanguageTagLegacySerialization() throws IOException {
        var iriString = "http://example.org/prop";
        var dictionaryLanguage = AnnotationAssertionDictionaryLanguage.get(iriString, "");
        var serialization = String.format("{\"propertyIri\":\"%s\"}", iriString);
        JsonSerializationTestUtil.testDeserialization(serialization, dictionaryLanguage, DictionaryLanguage.class);
    }

    @Test
    public void shouldDeserializeAnnotationAssertionWithNonEmptyLanguageTagLegacySerialization() throws IOException {
        var iriString = "http://example.org/prop";
        var dictionaryLanguage = AnnotationAssertionDictionaryLanguage.get(iriString, "en");
        var serialization = String.format("{\"propertyIri\":\"%s\", \"lang\":\"en\"}", iriString);
        JsonSerializationTestUtil.testDeserialization(serialization, dictionaryLanguage, DictionaryLanguage.class);
    }
}
