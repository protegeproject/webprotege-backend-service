package edu.stanford.protege.webprotege.forms.json;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import edu.stanford.protege.webprotege.common.LanguageMap;
import edu.stanford.protege.webprotege.forms.data.TextControlData;
import edu.stanford.protege.webprotege.forms.field.LineMode;
import edu.stanford.protege.webprotege.forms.field.StringType;
import edu.stanford.protege.webprotege.forms.field.TextControlDescriptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi.model.OWLLiteral;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import static org.assertj.core.api.Assertions.assertThat;

class Json2TextControlDataTest {

    private Json2TextControlData json2TextControlData;

    @BeforeEach
    public void setup() {
        json2TextControlData = new Json2TextControlData(new OWLDataFactoryImpl());
    }

    @Test
    public void shouldConvertSimpleStringText() {
        var node = JsonNodeFactory.instance.textNode("abc");
        var descriptor = new TextControlDescriptor(LanguageMap.empty(),
                StringType.SIMPLE_STRING,
                null,
                LineMode.SINGLE_LINE,
                null,
                null);
        var data = json2TextControlData.convert(node, descriptor);
        var literal = data.flatMap(TextControlData::getValue);
        assertThat(literal).map(OWLLiteral::getLiteral).contains("abc");
        assertThat(literal).map(OWLLiteral::getLang).contains("");
    }

    @Test
    public void shouldConvertSimpleStringTextWithLang() {
        var node = JsonNodeFactory.instance.textNode("abc@gr");
        var descriptor = new TextControlDescriptor(LanguageMap.empty(),
                StringType.SIMPLE_STRING,
                null,
                LineMode.SINGLE_LINE,
                null,
                null);
        var data = json2TextControlData.convert(node, descriptor);
        var literal = data.flatMap(TextControlData::getValue);
        assertThat(literal).map(OWLLiteral::getLiteral).contains("abc@gr");
        assertThat(literal).map(OWLLiteral::getLang).contains("");
    }

    @Test
    public void shouldConvertSpecificLanguageTagText() {
        var node = JsonNodeFactory.instance.textNode("abc");
        var descriptor = new TextControlDescriptor(LanguageMap.empty(),
                StringType.SPECIFIC_LANG_STRING,
                "es",
                LineMode.SINGLE_LINE,
                null,
                null);
        var data = json2TextControlData.convert(node, descriptor);
        var literal = data.flatMap(TextControlData::getValue);
        assertThat(literal).map(OWLLiteral::getLiteral).contains("abc");
        assertThat(literal).map(OWLLiteral::getLang).contains("es");
    }

    @Test
    public void shouldConvertLanguageTag() {
        var node = JsonNodeFactory.instance.textNode("abc@pt");
        var descriptor = new TextControlDescriptor(LanguageMap.empty(),
                StringType.LANG_STRING,
                null,
                LineMode.SINGLE_LINE,
                null,
                null);
        var data = json2TextControlData.convert(node, descriptor);
        var literal = data.flatMap(TextControlData::getValue);
        assertThat(literal).map(OWLLiteral::getLiteral).contains("abc");
        assertThat(literal).map(OWLLiteral::getLang).contains("pt");
    }

    @Test
    public void shouldConvertEmptyLanguageTag() {
        var node = JsonNodeFactory.instance.textNode("abc");
        var descriptor = new TextControlDescriptor(LanguageMap.empty(),
                StringType.LANG_STRING,
                null,
                LineMode.SINGLE_LINE,
                null,
                null);
        var data = json2TextControlData.convert(node, descriptor);
        var literal = data.flatMap(TextControlData::getValue);
        assertThat(literal).map(OWLLiteral::getLiteral).contains("abc");
        assertThat(literal).map(OWLLiteral::getLang).contains("");
    }

}