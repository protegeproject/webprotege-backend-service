package edu.stanford.protege.webprotege.crud.persistence;

import edu.stanford.protege.webprotege.crud.EntityCrudKitPrefixSettings;
import edu.stanford.protege.webprotege.crud.EntityCrudKitSettings;
import edu.stanford.protege.webprotege.crud.gen.GeneratedAnnotationsSettings;
import edu.stanford.protege.webprotege.crud.uuid.UuidSuffixSettings;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import edu.stanford.protege.webprotege.project.ProjectId;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-07
 */
public class ProjectEntityCrudKitSettings_SerializationTestCase {


    private final ProjectId projectId = ProjectId.get("12345678-1234-1234-1234-123456789abc");

    private ProjectEntityCrudKitSettings settings;

    @Before
    public void setUp() {
        settings = ProjectEntityCrudKitSettings.get(projectId,
                                                    EntityCrudKitSettings.get(
                                                            EntityCrudKitPrefixSettings.get(),
                                                            UuidSuffixSettings.get(),
                                                            GeneratedAnnotationsSettings.empty()
                                                    ));
    }

    @Test
    public void shouldRoundTrip() throws IOException {
        JsonSerializationTestUtil.testSerialization(settings, ProjectEntityCrudKitSettings.class);
    }
}
