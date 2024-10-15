package edu.stanford.protege.webprotege.hierarchy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;

public class NamedHierarchyRepository {

    private static final String COLLECTION_NAME = "NamedHierarchies";
    public static final String PROJECT_ID = "projectId";
    public static final String NAMED_HIERARCHY = "namedHierarchy";

    private final ObjectMapper mapper;

    private final MongoTemplate mongoTemplate;

    public NamedHierarchyRepository(ObjectMapper mapper, MongoTemplate mongoTemplate) {
        this.mapper = mapper;
        this.mongoTemplate = mongoTemplate;
    }

    public List<NamedHierarchyRecord> findByProjectIdAndNamedHierarchyHierarchyDescriptor(ProjectId projectId, HierarchyDescriptor hierarchyDescriptor) {
        var criteria = Criteria.where(PROJECT_ID).is(projectId.id())
                .and(NAMED_HIERARCHY).is(hierarchyDescriptor);
        var found = mongoTemplate.find(Query.query(criteria), JsonNode.class);
        return found.stream()
                .map(f -> mapper.convertValue(f, NamedHierarchyRecord.class))
                .toList();
    }

    public List<NamedHierarchy> find(ProjectId projectId) {
        var query = new Document(PROJECT_ID, projectId.id());
        var matched =  mongoTemplate.getCollection(COLLECTION_NAME).find(query, Document.class);
        var result = new ArrayList<NamedHierarchy>();
        matched.forEach(m -> {
            var record = mapper.convertValue(m, NamedHierarchyRecord.class);
            result.add(record.namedHierarchy());
        });
        return result;
    }

    public void delete(ProjectId projectId, NamedHierarchy namedHierarchy) {

    }

    public void save(ProjectId projectId, NamedHierarchy namedHierarchy) {

        var projectIdFilter = Filters.eq(PROJECT_ID, projectId.id());
        var hierarchyIdFilter = Filters.eq("namedHierarchy.hierarchyId", namedHierarchy.hierarchyId().getId());
        var combinedFilter = Filters.and(projectIdFilter, hierarchyIdFilter);

        var record = new NamedHierarchyRecord(projectId, namedHierarchy);
        var doc = mapper.convertValue(record, Document.class);

        var collection = mongoTemplate.getCollection(COLLECTION_NAME);

        var plainFound = collection.find();
        plainFound.forEach(d -> System.err.println("PF: " + d));


        var found = collection.find(projectIdFilter);
        System.err.println(found);
        found.forEach(f -> System.err.println(f));
        System.err.println(found.first());;

        collection.replaceOne(combinedFilter, doc, new ReplaceOptions().upsert(true));


    }
}
