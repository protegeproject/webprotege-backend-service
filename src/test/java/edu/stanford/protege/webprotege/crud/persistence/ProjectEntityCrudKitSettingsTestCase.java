package edu.stanford.protege.webprotege.crud.persistence;

import edu.stanford.protege.webprotege.crud.EntityCrudKitSettings;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.mockito.Mockito.mock;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/08/2013
 */
public class ProjectEntityCrudKitSettingsTestCase {

    public static final String PROJECT_ID_FIELD_NAME = "projectId";

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionForNullProjectIdInConstructor() {
        ProjectEntityCrudKitSettings.get(null, mock(EntityCrudKitSettings.class));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionForNullEntityCrudKitSettings() {
        ProjectEntityCrudKitSettings.get(ProjectId.generate(), null);
    }

    @Test
    public void getProjectIdShouldReturnValueEqualToProjectIdSuppliedInConstructor() {
        ProjectId projectId = ProjectId.generate();
        ProjectEntityCrudKitSettings settings = ProjectEntityCrudKitSettings.get(projectId, mock(EntityCrudKitSettings.class));
        assertEquals(projectId, settings.getProjectId());
    }

    @Test
    public void getSettingsShouldReturnValueEqualToSettingsSuppliedInConstructor() {
        EntityCrudKitSettings<?> entityCrudKitSettings = mock(EntityCrudKitSettings.class);
        ProjectEntityCrudKitSettings settings = ProjectEntityCrudKitSettings.get(ProjectId.generate(), entityCrudKitSettings);
        assertEquals(entityCrudKitSettings, settings.getSettings());
    }

    @Test
    public void hashCodeShouldBeEqualForSameProjectIdAndSettings() {
        ProjectId projectId = ProjectId.generate();
        EntityCrudKitSettings<?> settings = mock(EntityCrudKitSettings.class);
        ProjectEntityCrudKitSettings settingsA = ProjectEntityCrudKitSettings.get(projectId, settings);
        ProjectEntityCrudKitSettings settingsB = ProjectEntityCrudKitSettings.get(projectId, settings);
        assertEquals(settingsA.hashCode(), settingsB.hashCode());
    }

    @Test
    public void equalsShouldReturnTrueForEqualProjectIdAndEqualSettings() {
        ProjectId projectId = ProjectId.generate();
        EntityCrudKitSettings<?> settings = mock(EntityCrudKitSettings.class);
        ProjectEntityCrudKitSettings settingsA = ProjectEntityCrudKitSettings.get(projectId, settings);
        ProjectEntityCrudKitSettings settingsB = ProjectEntityCrudKitSettings.get(projectId, settings);
        assertEquals(settingsA, settingsB);
    }
}
