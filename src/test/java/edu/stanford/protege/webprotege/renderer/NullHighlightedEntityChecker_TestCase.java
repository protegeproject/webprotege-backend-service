package edu.stanford.protege.webprotege.renderer;

import edu.stanford.protege.webprotege.mansyntax.render.NullHighlightedEntityChecker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.model.OWLEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.mock;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 29/01/15
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class NullHighlightedEntityChecker_TestCase {


    private NullHighlightedEntityChecker checker;

    @BeforeEach
    public void setUp() throws Exception {
        checker = NullHighlightedEntityChecker.get();
    }

    @Test
    public void shouldReturnFalse() {
        assertThat(checker.isHighlighted(mock(OWLEntity.class)), is(false));
    }

    @Test
    public void shouldGenerateToString() {
        assertThat(checker.toString(), startsWith("NullHighlightedEntityChecker"));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(checker, is(equalTo(checker)));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(checker, is(not(equalTo(null))));
    }

    @Test
    public void shouldHaveSameHashCodeAsOther() {
        assertThat(checker.hashCode(), is(checker.hashCode()));
    }

}
