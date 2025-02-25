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

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 8 Jun 2018
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class LiteralInLexicalSpaceMatcher_TestCase {

    private LiteralInLexicalSpaceMatcher matcher;

    @Mock
    private OWLLiteral literal;

    @Mock
    private OWLDatatype datatype;

    @BeforeEach
    public void setUp() {
        matcher = new LiteralInLexicalSpaceMatcher();
        when(literal.getDatatype()).thenReturn(datatype);
        when(datatype.isBuiltIn()).thenReturn(true);
        when(datatype.getBuiltInDatatype()).thenReturn(OWL2Datatype.XSD_INTEGER);
    }

    @Test
    public void shouldMatchLiteralInLexicalSpace() {
        when(literal.getLiteral()).thenReturn("33");
        assertThat(matcher.matches(literal), is(true));
    }

    @Test
    public void shouldNotMatchLiteralNotInLexicalSpace() {
        when(literal.getLiteral()).thenReturn("abc");
        assertThat(matcher.matches(literal), is(false));
    }
}
