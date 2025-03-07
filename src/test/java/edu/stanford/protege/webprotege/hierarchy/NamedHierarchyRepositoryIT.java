package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.MongoTestExtension;
import edu.stanford.protege.webprotege.WebprotegeBackendMonolithApplication;
import edu.stanford.protege.webprotege.common.LanguageMap;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {"webprotege.rabbitmq.commands-subscribe=false"})
@Import({WebprotegeBackendMonolithApplication.class})
@ExtendWith({SpringExtension.class, MongoTestExtension.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
class NamedHierarchyRepositoryIT {

    @Autowired
    private NamedHierarchyRepository repository;

    private ProjectId projectId;
    private NamedHierarchy namedHierarchy, otherNamedHierarchy;

    @BeforeEach
    void setUp() {
        projectId = ProjectId.generate();
        namedHierarchy = new NamedHierarchy(
                HierarchyId.get("first"),
                LanguageMap.of("en", "Hello"),
                LanguageMap.of("en", "World"),
                ClassHierarchyDescriptor.create()
        );
        otherNamedHierarchy = new NamedHierarchy(
                HierarchyId.get("other"),
                LanguageMap.of("en", "Hello"),
                LanguageMap.of("en", "World"),
                ClassHierarchyDescriptor.create()
        );
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void shouldFindByProjectId() {
        repository.setNamedHierarchies(projectId, List.of(namedHierarchy));
        var found = repository.findNamedHierarchies(projectId);
        assertThat(found).hasSize(1);
        assertThat(found.get(0).hierarchyId()).isEqualTo(HierarchyId.get("first"));
    }


    @Test
    public void shouldNotSaveDuplicates() {
        repository.setNamedHierarchies(projectId, List.of(namedHierarchy));
        repository.setNamedHierarchies(projectId, List.of(otherNamedHierarchy));
        var found = repository.findNamedHierarchies(projectId);
        assertThat(found).hasSize(1);
        assertThat(found.get(0).hierarchyId()).isEqualTo(HierarchyId.get("other"));
    }
}