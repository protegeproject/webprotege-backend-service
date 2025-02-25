package edu.stanford.protege.webprotege.crud;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.model.IRI;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 17/04/2014
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class IRIParserTestCase {

    private IRIParser parser;

    @BeforeEach
    public void setUp() {
        parser = new IRIParser();
    }

    @Test
    public void shouldReturnAbsentForEmptyString() {
        Optional<IRI> value = parser.parseIRI("");
        assertThat(value.isPresent(), is(false));
    }

    @Test
    public void shouldReturnAbsentForEmptyIRIContent() {
        Optional<IRI> value = parser.parseIRI("<>");
        assertThat(value.isPresent(), is(false));
    }

    @Test
    public void shouldReturnAbsentForContentWithSpace() {
        Optional<IRI> value = parser.parseIRI("<a b>");
        assertThat(value.isPresent(), is(false));
    }

    @Test
    public void shouldReturnIRIWithParseableContent() {
        String content = "stuff.com";
        Optional<IRI> value = parser.parseIRI("<" + content + ">");
        assertThat(value.isPresent(), is(true));
        assertThat(value.get().toString(), is(equalTo(content)));
    }


}
