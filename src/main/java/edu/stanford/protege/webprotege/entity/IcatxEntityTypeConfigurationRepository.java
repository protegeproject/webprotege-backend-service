package edu.stanford.protege.webprotege.entity;


import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.bson.Document;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static edu.stanford.protege.webprotege.issues.EntityDiscussionThread.PROJECT_ID;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
public class IcatxEntityTypeConfigurationRepository {
    private final MongoTemplate mongoTemplate;


    public IcatxEntityTypeConfigurationRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Cacheable(value = "entityTypeConfigurations", key = "#projectId")
    public List<IcatxEntityTypeConfiguration> findAllByProjectId(ProjectId projectId) {
        var query = query(where(PROJECT_ID).is(projectId));

        return mongoTemplate.find(query, IcatxEntityTypeConfiguration.class);
    }
}
