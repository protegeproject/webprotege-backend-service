package edu.stanford.protege.webprotege.renderer;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.mansyntax.render.IRIOrdinalProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 04/10/2014
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class IRIOrdinalProvider_TestCase {

    private IRIOrdinalProvider provider;

    @Mock
    private IRI firstIRI, secondIRI;

    @Mock
    private IRI otherIRI;


    @BeforeEach
    public void setUp() throws Exception {
        ImmutableList<IRI> list = ImmutableList.of(firstIRI, secondIRI);
        provider = new IRIOrdinalProvider(list);
    }

    @Test
public void shouldThrowNullPointerException() {
    assertThrows(NullPointerException.class, () -> { 
        new IRIOrdinalProvider(null);
     });
}

    @Test
    public void shouldReturnDefaultIndexWithRDFSLabelFirst() {
        IRIOrdinalProvider iriOrdinalProvider = IRIOrdinalProvider.withDefaultAnnotationPropertyOrdering();
        int labelIndex = iriOrdinalProvider.getIndex(OWLRDFVocabulary.RDFS_LABEL.getIRI());
        assertThat(labelIndex, is(0));
    }

    @Test
    public void shouldReturnZero() {
        assertThat(provider.getIndex(firstIRI), is(0));
    }

    @Test
    public void shouldReturnOne() {
        assertThat(provider.getIndex(secondIRI), is(1));
    }

    @Test
    public void shouldReturnTwo() {
        assertThat(provider.getIndex(otherIRI), is(2));
    }

}
