
package edu.stanford.protege.webprotege.obo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(MockitoJUnitRunner.class)
public class GetOboTermRelationshipsResult_TestCase {

    private GetOboTermRelationshipsResult getOboTermRelationshipsResult;
    @Mock
    private OBOTermRelationships relationships;

    @Before
    public void setUp() {
        getOboTermRelationshipsResult = GetOboTermRelationshipsResult.create(relationships);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_relationships_IsNull() {
        GetOboTermRelationshipsResult.create(null);
    }

    @Test
    public void shouldReturnSupplied_relationships() {
        assertThat(getOboTermRelationshipsResult.getRelationships(), is(this.relationships));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(getOboTermRelationshipsResult, is(getOboTermRelationshipsResult));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(getOboTermRelationshipsResult.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(getOboTermRelationshipsResult, is(GetOboTermRelationshipsResult.create(relationships)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_relationships() {
        assertThat(getOboTermRelationshipsResult, is(not(GetOboTermRelationshipsResult.create(Mockito.mock(OBOTermRelationships.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(getOboTermRelationshipsResult.hashCode(), is(GetOboTermRelationshipsResult.create(relationships).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(getOboTermRelationshipsResult.toString(), startsWith("GetOboTermRelationshipsResult"));
    }

}
