
package edu.stanford.protege.webprotege.obo;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.semanticweb.owlapi.model.OWLEntity;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class GetOboTermIdResult_TestCase {

    private GetOboTermIdResult getOboTermIdResult;
    @Mock
    private OWLEntity entity;
    @Mock
    private OBOTermId termId;

    @Before
    public void setUp()
        throws Exception
    {
        getOboTermIdResult = GetOboTermIdResult.create(entity, termId);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_entity_IsNull() {
        GetOboTermIdResult.create(null, termId);
    }

    @Test
    public void shouldReturnSupplied_entity() {
        MatcherAssert.assertThat(getOboTermIdResult.getEntity(), Matchers.is(this.entity));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_termId_IsNull() {
        GetOboTermIdResult.create(entity, null);
    }

    @Test
    public void shouldReturnSupplied_termId() {
        MatcherAssert.assertThat(getOboTermIdResult.getTermId(), Matchers.is(this.termId));
    }

    @Test
    public void shouldBeEqualToSelf() {
        MatcherAssert.assertThat(getOboTermIdResult, Matchers.is(getOboTermIdResult));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        MatcherAssert.assertThat(getOboTermIdResult.equals(null), Matchers.is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        MatcherAssert.assertThat(getOboTermIdResult, Matchers.is(GetOboTermIdResult.create(entity, termId)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_entity() {
        MatcherAssert.assertThat(getOboTermIdResult, Matchers.is(Matchers.not(GetOboTermIdResult.create(Mockito.mock(OWLEntity.class), termId))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_termId() {
        MatcherAssert.assertThat(getOboTermIdResult, Matchers.is(Matchers.not(GetOboTermIdResult.create(entity, Mockito.mock(
                OBOTermId.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        MatcherAssert.assertThat(getOboTermIdResult.hashCode(), Matchers.is(GetOboTermIdResult.create(entity, termId).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        MatcherAssert.assertThat(getOboTermIdResult.toString(), Matchers.startsWith("GetOboTermIdResult"));
    }

}
