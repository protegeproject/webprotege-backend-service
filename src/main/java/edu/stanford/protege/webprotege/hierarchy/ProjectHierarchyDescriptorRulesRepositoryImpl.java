package edu.stanford.protege.webprotege.hierarchy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.InsertOneOptions;
import com.mongodb.client.model.ReplaceOptions;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.bson.BsonDocument;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.ComparisonOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Optional;

public class ProjectHierarchyDescriptorRulesRepositoryImpl implements ProjectHierarchyDescriptorRulesRepository {

    public static final String COLLECTION_NAME = "ProjectHierarchyDescriptorRules";

    private final MongoTemplate mongoTemplate;

    private final ObjectMapper objectMapper;

    public ProjectHierarchyDescriptorRulesRepositoryImpl(MongoTemplate mongoTemplate, ObjectMapper objectMapper) {
        this.mongoTemplate = mongoTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void ensureIndexes() {
        // Nothing to do because projectId translates to _id that is already indexed
    }

    private @NotNull MongoCollection<Document> getCollection() {
        return mongoTemplate.getCollection(COLLECTION_NAME);
    }

    @Override
    public void save(ProjectHierarchyDescriptorRules rules) {
        var doc = objectMapper.convertValue(rules, Document.class);
        var query = Query.query(Criteria.where("_id").is(rules.projectId().id()));
        getCollection().replaceOne(query.getQueryObject(), doc, new ReplaceOptions().upsert(true));
    }

    @Override
    public void delete(ProjectId projectId) {
        var query = Query.query(Criteria.where("_id").is(projectId.id()));
        getCollection().deleteOne(query.getQueryObject());
    }

    @Override
    public Optional<ProjectHierarchyDescriptorRules> find(ProjectId projectId) {
        var query = Query.query(Criteria.where("projectId").is(projectId.id()));
        var found = getCollection().find(query.getQueryObject()).first();
        return Optional.ofNullable(found).map(o -> objectMapper.convertValue(o, ProjectHierarchyDescriptorRules.class));
    }
}
