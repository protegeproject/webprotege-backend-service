
package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.common.ProjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class RecentProjectRecord_TestCase {

    private RecentProjectRecord recentProjectRecord;

    private ProjectId projectId = ProjectId.generate();

    private long timestamp = 1L;

    @BeforeEach
    public void setUp() {
        recentProjectRecord = new RecentProjectRecord(projectId, timestamp);
    }

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
    assertThrows(NullPointerException.class, () -> { 
        new RecentProjectRecord(null, timestamp);
     });
}

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(recentProjectRecord.getProjectId(), is(this.projectId));
    }

    @Test
    public void shouldReturnSupplied_timestamp() {
        assertThat(recentProjectRecord.getTimestamp(), is(this.timestamp));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(recentProjectRecord, is(recentProjectRecord));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(recentProjectRecord.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(recentProjectRecord, is(new RecentProjectRecord(projectId, timestamp)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(recentProjectRecord, is(not(new RecentProjectRecord(ProjectId.generate(), timestamp))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_timestamp() {
        assertThat(recentProjectRecord, is(not(new RecentProjectRecord(projectId, 2L))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(recentProjectRecord.hashCode(), is(new RecentProjectRecord(projectId, timestamp).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(recentProjectRecord.toString(), startsWith("RecentProjectRecord"));
    }

    @Test
    public void shouldComeBefore() {
        RecentProjectRecord other = new RecentProjectRecord(ProjectId.generate(), 0L);
        assertThat(recentProjectRecord.compareTo(other), is(lessThan(0)));
    }

    @Test
    public void shouldComeAfter() {
        RecentProjectRecord other = new RecentProjectRecord(ProjectId.generate(), 2L);
        assertThat(recentProjectRecord.compareTo(other), is(greaterThan(0)));
    }
}
