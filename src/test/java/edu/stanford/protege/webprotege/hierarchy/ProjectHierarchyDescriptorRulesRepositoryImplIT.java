package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.MongoTestExtension;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {"webprotege.rabbitmq.commands-subscribe=false"})
@ExtendWith(MongoTestExtension.class)
class ProjectHierarchyDescriptorRulesRepositoryImplIT {

    public static final String COLLECTION_NAME = "ProjectHierarchyDescriptorRules";

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ProjectHierarchyDescriptorRulesRepositoryImpl repository;

    private ProjectId projectId;

    private ProjectHierarchyDescriptorRules projectRules;

    @BeforeEach
    void setUp() {
        projectId = ProjectId.generate();
        var rules = new ArrayList<HierarchyDescriptorRule>();
        projectRules = new ProjectHierarchyDescriptorRules(projectId, rules);

    }

    @Test
    void shouldSaveProjectRules() {
        repository.save(projectRules);
        var savedRules = mongoTemplate.getCollection(COLLECTION_NAME).find().iterator().next();
        assertThat(savedRules).isNotNull();
        assertThat(savedRules.get("_id")).isEqualTo(projectId.id());
    }

    @Test
    void shouldFindProjectRules() {
        repository.save(projectRules);
        var found = repository.find(projectId);
        assertThat(found).contains(projectRules);
    }

    @Test
    public void shouldNotFindProjectRules() {
        var found = repository.find(projectId);
        assertThat(found).isEmpty();
    }

    @Test
    void shouldDeleteProjectRules() {
        repository.save(projectRules);
        var collection = mongoTemplate.getCollection(COLLECTION_NAME);
        assertThat(collection.countDocuments()).isEqualTo(1);
        repository.delete(projectId);
        assertThat(collection.countDocuments()).isEqualTo(0);
    }
}