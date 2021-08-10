package edu.stanford.protege.webprotege.project;


import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.util.UUIDUtil;
import org.junit.Test;

import java.util.Optional;

import static junit.framework.Assert.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/20/13
 */
public class ProjectId_TestCase {

    @Test
    public void equalsShouldReturnTrueForObjectsWithSameId() {
        String uuid = "0d8f03d4-d9bb-496d-a78c-146868af8265";
        ProjectId projectIdA = ProjectId.valueOf(uuid);
        ProjectId projectIdB = ProjectId.valueOf(uuid);
        assertEquals(projectIdA, projectIdB);
    }

    @Test
    public void equalsShouldReturnFalseForObjectsWithDifferentIds() {
        String uuidA = "0d8f03d4-d9bb-496d-a78c-146868af8265";
        String uuidB = "d16a6ca0-3afc-4af3-8a95-82cdc82f52cc";
        ProjectId projectIdA = ProjectId.valueOf(uuidA);
        ProjectId projectIdB = ProjectId.valueOf(uuidB);
        assertFalse(projectIdA.equals(projectIdB));
    }

    @Test
    public void equalsNullReturnsFalse() {
        String uuid = "0d8f03d4-d9bb-496d-a78c-146868af8265";
        ProjectId projectId = ProjectId.valueOf(uuid);
        assertNotNull(projectId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void malformedUUIDThrowsProjectIdFormatException() {
        String malformedId = "wrong";
        ProjectId.valueOf(malformedId);
    }

    @Test
    public void getIdReturnsSuppliedValue() {
        String uuid = "0d8f03d4-d9bb-496d-a78c-146868af8265";
        ProjectId projectId = ProjectId.valueOf(uuid);
        assertEquals(uuid, projectId.id());
    }

    @Test
    public void getRegExpReturnsCorrectExpression() {
        String regExp = UUIDUtil.getIdRegExp().toString();
        assertEquals("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}", regExp);
    }

    @Test(expected = NullPointerException.class)
    public void getThrowsNullPointerForNullArgument() {
        ProjectId.valueOf(null);
    }
}
