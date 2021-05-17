
package edu.stanford.bmir.protege.web.shared.obo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class GetOboTermSynonymsResult_TestCase {

    private GetOboTermSynonymsResult getOboTermSynonymsResult;

    private List<OBOTermSynonym> synonyms = singletonList(mock(OBOTermSynonym.class));

    @Before
    public void setUp() {
        getOboTermSynonymsResult = GetOboTermSynonymsResult.create(synonyms);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_synonyms_IsNull() {
        GetOboTermSynonymsResult.create(null);
    }

    @Test
    public void shouldReturnSupplied_synonyms() {
        assertThat(getOboTermSynonymsResult.getSynonyms(), is(this.synonyms));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(getOboTermSynonymsResult, is(getOboTermSynonymsResult));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(getOboTermSynonymsResult.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(getOboTermSynonymsResult, is(GetOboTermSynonymsResult.create(synonyms)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_synonyms() {
        assertThat(getOboTermSynonymsResult, is(not(GetOboTermSynonymsResult.create(singletonList(mock(OBOTermSynonym.class))))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(getOboTermSynonymsResult.hashCode(), is(GetOboTermSynonymsResult.create(synonyms).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(getOboTermSynonymsResult.toString(), startsWith("GetOboTermSynonymsResult"));
    }

}
