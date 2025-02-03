package edu.stanford.protege.webprotege.hierarchy;

import com.mongodb.client.MongoCollection;
import edu.stanford.protege.webprotege.MongoTestExtension;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {"webprotege.rabbitmq.commands-subscribe=false"})
@ExtendWith(MongoTestExtension.class)
class HierarchyDescriptorRulesRepositoryImplIT {

    public static final String COLLECTION_NAME = "HierarchyDescriptorRules";

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private HierarchyDescriptorRulesRepositoryImpl repository;

    private ProjectId projectId;

    private ProjectHierarchyDescriptorRules projectRules;

    @BeforeEach
    void setUp() {
        projectId = ProjectId.generate();
        var rules = new ArrayList<HierarchyDescriptorRule>();
        projectRules = new ProjectHierarchyDescriptorRules(projectId, rules);


        var collection = mongoTemplate.getCollection(COLLECTION_NAME);
        collection.drop();
    }

    @Test
    void shouldSaveProjectRules() {
        repository.save(projectRules);
        var collection = mongoTemplate.getCollection(COLLECTION_NAME);
        var savedRules = collection.find().iterator().next();
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