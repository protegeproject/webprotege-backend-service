package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.MongoTestExtension;
import edu.stanford.protege.webprotege.common.LanguageMap;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(MongoTestExtension.class)
class NamedHierarchyRepositoryIT {

    @Autowired
    private NamedHierarchyRepository repository;

    private ProjectId projectId;
    private NamedHierarchy namedHierarchy;

    @BeforeEach
    void setUp() {
        projectId = ProjectId.generate();
        namedHierarchy = new NamedHierarchy(
                HierarchyId.get("other"),
                LanguageMap.of("en", "Hello"),
                LanguageMap.of("en", "World"),
                ClassHierarchyDescriptor.create()
        );
        repository.save(projectId, namedHierarchy);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findByProjectIdAndNamedHierarchyHierarchyDescriptor() {
    }

    @Test
    void shouldFindByProjectId() {
        var found = repository.find(projectId);
        assertThat(found).hasSize(1);
    }

    @Test
    public void shouldDeletedHierarchy() {
        repository.delete(projectId, namedHierarchy);
        var found = repository.find(projectId);
        assertThat(found).hasSize(0);
    }

    @Test
    public void shouldNotSaveDuplicates() {
        repository.save(projectId, namedHierarchy);
        var found = repository.find(projectId);
        assertThat(found).hasSize(1);
    }
}