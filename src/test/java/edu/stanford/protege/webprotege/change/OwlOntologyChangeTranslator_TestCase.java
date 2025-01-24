package edu.stanford.protege.webprotege.change;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.model.OWLOntologyChange;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-29
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class OwlOntologyChangeTranslator_TestCase {

    private OwlOntologyChangeTranslator translator;

    @Mock
    private OwlOntologyChangeTranslatorVisitor visitor;

    @Mock
    private OWLOntologyChange change;

    @Mock
    private OntologyChange ontologyChange;

    @BeforeEach
    public void setUp() {
        translator = new OwlOntologyChangeTranslator(visitor);
        when(change.accept(visitor))
                .thenReturn(ontologyChange);
    }

    @Test
    public void shouldGetChange() {
        var result = translator.toOntologyChange(change);
        assertThat(result, is(ontologyChange));
    }
}
