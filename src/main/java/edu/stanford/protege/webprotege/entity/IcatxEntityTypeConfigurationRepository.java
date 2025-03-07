package edu.stanford.protege.webprotege.entity;


import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class IcatxEntityTypeConfigurationRepository {
    private final MongoTemplate mongoTemplate;

    public IcatxEntityTypeConfigurationRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    @Cacheable("entityTypeConfigCache")
    public List<IcatxEntityTypeConfiguration> getAllConfigurations() {
        return mongoTemplate.findAll(IcatxEntityTypeConfiguration.class);
    }
}
