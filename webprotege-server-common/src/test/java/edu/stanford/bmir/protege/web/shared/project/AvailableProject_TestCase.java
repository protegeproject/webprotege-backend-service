
package edu.stanford.bmir.protege.web.shared.project;

import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(value = MockitoJUnitRunner.class)
public class AvailableProject_TestCase {

    private static final String DISPLAY_NAME = "DISPLAY_NAME";

    private static final String DESCRIPTION = "DESCRIPTION";

    public static final long CREATED_AT = 33L;

    public static final long MODIFIED_AT = 55L;

    private AvailableProject availableProject;

    private boolean downloadable = true;

    private boolean trashable = true;

    @Mock
    private ProjectId projectId;

    @Mock
    private UserId modifiedBy, owner, createdBy;

    private long lastOpenedTimestamp = 11L;

    private boolean inTrash = true;

    @Before
    public void setUp() {
        availableProject = AvailableProject.get(projectId, DISPLAY_NAME, DESCRIPTION, owner, inTrash, CREATED_AT, createdBy, MODIFIED_AT, modifiedBy, downloadable, trashable, lastOpenedTimestamp);
    }

    @Test
    public void shouldReturnSupplied_downloadable() {
        assertThat(availableProject.isDownloadable(), is(this.downloadable));
    }

    @Test
    public void shouldReturnSupplied_trashable() {
        assertThat(availableProject.isTrashable(), is(this.trashable));
    }

    @Test
    public void shouldReturnSupplied_lastOpened() {
        assertThat(availableProject.getLastOpenedAt(), is(this.lastOpenedTimestamp));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(availableProject, is(availableProject));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(availableProject.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(availableProject, is(AvailableProject.get(projectId, DISPLAY_NAME, DESCRIPTION, owner, inTrash, CREATED_AT, createdBy, MODIFIED_AT, modifiedBy,  downloadable, trashable, lastOpenedTimestamp)));
    }
    
    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_downloadable() {
        assertThat(availableProject, is(not(AvailableProject.get(projectId, DISPLAY_NAME, DESCRIPTION, owner, inTrash, CREATED_AT, createdBy, MODIFIED_AT, modifiedBy,  false, trashable, lastOpenedTimestamp))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_trashable() {
        assertThat(availableProject, is(not(AvailableProject.get(projectId, DISPLAY_NAME, DESCRIPTION, owner, inTrash, CREATED_AT, createdBy, MODIFIED_AT, modifiedBy,  downloadable, false, lastOpenedTimestamp))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_lastOpened() {
        assertThat(availableProject, is(not(AvailableProject.get(projectId, DISPLAY_NAME, DESCRIPTION, owner, inTrash, CREATED_AT, createdBy, MODIFIED_AT, modifiedBy,  downloadable, trashable, 33L))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(availableProject.hashCode(), is(AvailableProject.get(projectId, DISPLAY_NAME, DESCRIPTION, owner, inTrash, CREATED_AT, createdBy, MODIFIED_AT, modifiedBy,  downloadable, trashable, lastOpenedTimestamp).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(availableProject.toString(), startsWith("AvailableProject"));
    }

    @Test
    public void should_getProjectId() {
        assertThat(availableProject.getProjectId(), is(projectId));
    }

    @Test
    public void should_getOwner() {
        assertThat(availableProject.getOwner(), is(owner));
    }

    @Test
    public void should_getDESCRIPTION() {
        assertThat(availableProject.getDescription(), is(DESCRIPTION));
    }

    @Test
    public void shouldReturn_true_For_isInTrash() {
        assertThat(availableProject.isInTrash(), is(true));
    }

    @Test
    public void should_getCREATED_AT() {
        assertThat(availableProject.getCreatedAt(), is(CREATED_AT));
    }

    @Test
    public void should_getCreatedBy() {
        assertThat(availableProject.getCreatedBy(), is(createdBy));
    }

    @Test
    public void should_getMODIFIED_AT() {
        assertThat(availableProject.getLastModifiedAt(), is(MODIFIED_AT));
    }

    @Test
    public void should_getmodifiedBy() {
        assertThat(availableProject.getLastModifiedBy(), is(modifiedBy));
    }

    @Test
    public void shouldReturn_true_For_isDownloadable() {
        assertThat(availableProject.isDownloadable(), is(true));
    }

    @Test
    public void shouldReturn_true_For_isTrashable() {
        assertThat(availableProject.isTrashable(), is(true));
    }

    @Test
    public void should_getDISPLAY_NAME() {
        assertThat(availableProject.getDisplayName(), is(DISPLAY_NAME));
    }

}
