package edu.stanford.protege.webprotege.forms.json;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.stanford.protege.webprotege.common.LanguageMap;
import edu.stanford.protege.webprotege.forms.data.PrimitiveFormControlData;
import edu.stanford.protege.webprotege.forms.data.SingleChoiceControlData;
import edu.stanford.protege.webprotege.forms.field.ChoiceDescriptor;
import edu.stanford.protege.webprotege.forms.field.FixedChoiceListSourceDescriptor;
import edu.stanford.protege.webprotege.forms.field.SingleChoiceControlDescriptor;
import edu.stanford.protege.webprotege.forms.field.SingleChoiceControlType;
import edu.stanford.protege.webprotege.index.ProjectAnnotationAssertionAxiomsBySubjectIndex;
import edu.stanford.protege.webprotege.shortform.MultiLingualDictionary;
import edu.stanford.protege.webprotege.shortform.MultiLingualDictionaryLuceneImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.semanticweb.owlapi.model.IRI;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class Json2SingleChoiceControlDataTest {

    public static final String CLS_IRI = "http://example.org/A";
    private Json2SingleChoiceControlData json2SingleChoiceControlData;

    @Mock
    private MultiLingualDictionary multiLingualDictionary;

    @Mock
    private ProjectAnnotationAssertionAxiomsBySubjectIndex index;

    private OWLDataFactoryImpl dataFactory;

    @BeforeEach
    void setUp() {
        dataFactory = new OWLDataFactoryImpl();
        json2SingleChoiceControlData = new Json2SingleChoiceControlData(
                new Json2Entity(dataFactory, multiLingualDictionary),
                new PrimitiveFormControlDataConverter(
                        new LiteralConverter(),
                        new IriConverter(new JsonNameExtractor(index)),
                        new EntityConverter(new JsonNameExtractor(index))
                )
        );
    }

    @Test
    public void shouldConvertChoice() {
        var node = JsonNodeFactory.instance.objectNode();
        node.set("@id", JsonNodeFactory.instance.textNode(CLS_IRI));
        node.set("@type", JsonNodeFactory.instance.textNode("Class"));

        var entity = dataFactory.getOWLClass(IRI.create(CLS_IRI));
        var descriptor = SingleChoiceControlDescriptor.get(SingleChoiceControlType.COMBO_BOX,
                FixedChoiceListSourceDescriptor.get(List.of(ChoiceDescriptor.choice(LanguageMap.empty(), PrimitiveFormControlData.get(entity)))));
        var data = json2SingleChoiceControlData.convert(node, descriptor);
        assertThat(data).flatMap(SingleChoiceControlData::getChoice).flatMap(PrimitiveFormControlData::asEntity).contains(entity);
    }

    @Test
    public void shouldConvertChoiceToEmptyIfNotInList() {
        var node = JsonNodeFactory.instance.objectNode();
        node.set("@id", JsonNodeFactory.instance.textNode(CLS_IRI));
        node.set("@type", JsonNodeFactory.instance.textNode("ObjectProperty"));

        var entity = dataFactory.getOWLClass(IRI.create(CLS_IRI));
        var descriptor = SingleChoiceControlDescriptor.get(SingleChoiceControlType.COMBO_BOX,
                FixedChoiceListSourceDescriptor.get(List.of(ChoiceDescriptor.choice(LanguageMap.empty(), PrimitiveFormControlData.get(entity)))));
        var data = json2SingleChoiceControlData.convert(node, descriptor);
        assertThat(data).flatMap(SingleChoiceControlData::getChoice).flatMap(PrimitiveFormControlData::asEntity).isEmpty();
    }

}