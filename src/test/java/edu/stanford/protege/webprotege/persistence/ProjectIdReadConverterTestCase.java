package edu.stanford.protege.webprotege.persistence;

import edu.stanford.protege.webprotege.common.ProjectId;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static junit.framework.Assert.assertEquals;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/20/13
 */
public class ProjectIdReadConverterTestCase {

    @Test
    public void convertShouldReturnProjectIdWithSuppliedId() {
        ProjectIdReadConverter converter = new ProjectIdReadConverter();
        String suppliedId = UUID.randomUUID().toString();
        ProjectId projectId = converter.convert(suppliedId);
        assertEquals(suppliedId, projectId.id());
    }
}
