package edu.stanford.protege.webprotege.forms.field;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.DataFactory;
import edu.stanford.protege.webprotege.forms.data.LiteralFormControlData;
import edu.stanford.protege.webprotege.common.LanguageMap;
import edu.stanford.protege.webprotege.criteria.CompositeRootCriteria;
import edu.stanford.protege.webprotege.criteria.EntityTypeIsOneOfCriteria;
import edu.stanford.protege.webprotege.criteria.MultiMatchType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi.model.EntityType;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-11
 */
public class ChoiceListSourceDescriptor_Serialization_TestCase {

    ImmutableList<ChoiceDescriptor> choices;

    @BeforeEach
    public void setUp() {
        choices = ImmutableList.of(
                ChoiceDescriptor.choice(LanguageMap.empty(),
                                        LiteralFormControlData.get(DataFactory.getOWLLiteral("A"))),
                ChoiceDescriptor.choice(LanguageMap.empty(),
                                        LiteralFormControlData.get(DataFactory.getOWLLiteral("B")))
        );
    }

    @Test
    public void shouldSerialize_FixedList() throws IOException {
        testSerialization(
                FixedChoiceListSourceDescriptor.get(choices)
        );
    }

    @Test
    public void shouldSerialize_DynamicList() throws IOException {
        testSerialization(
                DynamicChoiceListSourceDescriptor.get(
                        CompositeRootCriteria.get(
                                ImmutableList.of(EntityTypeIsOneOfCriteria.get(ImmutableSet.of(EntityType.CLASS))),
                                MultiMatchType.ALL
                        )
                )
        );
    }

    private static <V extends ChoiceListSourceDescriptor> void testSerialization(V value) throws IOException {
//        JsonSerializationTestUtil.testSerialization(value, ChoiceListSourceDescriptor.class);
    }
}
