
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
public class GetOboTermCrossProductResult_TestCase {

    private GetOboTermCrossProductResult getOboTermCrossProductResult;
    @Mock
    private OBOTermCrossProduct crossProduct;

    @Before
    public void setUp()
        throws Exception
    {
        getOboTermCrossProductResult = GetOboTermCrossProductResult.create(crossProduct);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_crossProduct_IsNull() {
        GetOboTermCrossProductResult.create(null);
    }

    @Test
    public void shouldReturnSupplied_crossProduct() {
        assertThat(getOboTermCrossProductResult.getCrossProduct(), is(this.crossProduct));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(getOboTermCrossProductResult, is(getOboTermCrossProductResult));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(getOboTermCrossProductResult.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(getOboTermCrossProductResult, is(GetOboTermCrossProductResult.create(crossProduct)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_crossProduct() {
        assertThat(getOboTermCrossProductResult, is(not(GetOboTermCrossProductResult.create(Mockito.mock(OBOTermCrossProduct.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(getOboTermCrossProductResult.hashCode(), is(GetOboTermCrossProductResult.create(crossProduct).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(getOboTermCrossProductResult.toString(), startsWith("GetOboTermCrossProductResult"));
    }

}
