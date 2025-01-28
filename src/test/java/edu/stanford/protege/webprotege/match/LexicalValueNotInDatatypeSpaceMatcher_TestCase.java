package edu.stanford.protege.webprotege.match;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Jun 2018
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class LexicalValueNotInDatatypeSpaceMatcher_TestCase {

    private LexicalValueNotInDatatypeSpaceMatcher matcher;

    @Mock
    private OWLLiteral literal;

    @Mock
    private OWLDatatype datatype;

    private String lexicalValue = "33";

    @BeforeEach
    public void setUp() {
        matcher = new LexicalValueNotInDatatypeSpaceMatcher();
        when(literal.getDatatype()).thenReturn(datatype);
        when(literal.getLiteral()).thenReturn(lexicalValue);
    }

    @Test
    public void shouldReturnFalseForNonOWL2Datatype() {
        when(datatype.isBuiltIn()).thenReturn(false);
        assertThat(matcher.matches(literal), is(false));
    }

    @Test
    public void shouldDelegateToOWL2DatatypeForBuiltInDatatypes() {
        when(datatype.isBuiltIn()).thenReturn(true);
        when(datatype.getBuiltInDatatype()).thenReturn(OWL2Datatype.XSD_INTEGER);
        matcher.matches(literal);
        verify(datatype, atLeastOnce()).getBuiltInDatatype();
    }

    @Test
    public void shouldMatchOWL2DatatypePatternAndReturnFalse() {
        when(datatype.isBuiltIn()).thenReturn(true);
        when(datatype.getBuiltInDatatype()).thenReturn(OWL2Datatype.XSD_INTEGER);
        assertThat(matcher.matches(literal), is(false));
    }

    @Test
    public void shouldNotMatchOWL2DatatypePatternAndReturnTrue() {
        when(datatype.isBuiltIn()).thenReturn(true);
        when(datatype.getBuiltInDatatype()).thenReturn(OWL2Datatype.XSD_DATE_TIME);
        assertThat(matcher.matches(literal), is(true));
    }
}
