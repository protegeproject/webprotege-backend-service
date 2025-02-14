
package edu.stanford.protege.webprotege.change;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.mockito.Mock;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ProjectChangeTimestampComparator_TestCase {

    private ProjectChangeTimestampComparator comparator;

    @Mock
    private ProjectChange projectChange1;
    @Mock
    private ProjectChange projectChange2;

    @BeforeEach
    public void setUp() {
        comparator = new ProjectChangeTimestampComparator();
    }

    @Test
    public void shouldPlaceSmallerBeforeLarger() {
        when(projectChange1.getTimestamp()).thenReturn(5l);
        when(projectChange2.getTimestamp()).thenReturn(Long.MAX_VALUE - 5);
        assertThat(comparator.compare(projectChange1, projectChange2), is(lessThan(0)));
    }

    @Test
    public void shouldPlaceLargerAfterSmaller() {
        when(projectChange1.getTimestamp()).thenReturn(Long.MAX_VALUE - 5);
        when(projectChange2.getTimestamp()).thenReturn(5l);
        assertThat(comparator.compare(projectChange1, projectChange2), is(greaterThan(0)));
    }

    @Test
    public void shouldCompareEqual() {
        when(projectChange1.getTimestamp()).thenReturn(5l);
        when(projectChange2.getTimestamp()).thenReturn(5l);
        assertThat(comparator.compare(projectChange1, projectChange2), is(0));
    }
}
