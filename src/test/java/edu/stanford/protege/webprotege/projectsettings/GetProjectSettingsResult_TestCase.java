package edu.stanford.protege.webprotege.projectsettings;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25/11/14
 */
@RunWith(MockitoJUnitRunner.class)
public class GetProjectSettingsResult_TestCase {

    @Mock
    private ProjectSettings settings;

    private GetProjectSettingsResult result;

    @Before
    public void setUp() throws Exception {
        result = GetProjectSettingsResult.create(settings);
    }


    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_ProjectSettings_IsNull() {
        GetProjectSettingsResult.create(null);
    }

    @Test
    public void shouldReturnSupplied_ProjectSettings() {
        assertThat(result.getSettings(), is(settings));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(result, is(equalTo(result)));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(result, is(not(equalTo(null))));
    }

    @Test
    public void shouldBeEqualToOther() {
        GetProjectSettingsResult other = GetProjectSettingsResult.create(settings);
        assertThat(result, is(equalTo(other)));
    }

    @Test
    public void shouldHaveSameHashCode() {
        GetProjectSettingsResult other = GetProjectSettingsResult.create(settings);
        assertThat(result.hashCode(), is(other.hashCode()));
    }
}
