package edu.stanford.protege.webprotege.hierarchy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.model.*;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;

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

    public List<NamedHierarchy> findNamedHierarchies(ProjectId projectId) {
        var query = new Document(PROJECT_ID, projectId.id());
        var matched =  mongoTemplate.getCollection(COLLECTION_NAME).find(query, Document.class);
        var result = new ArrayList<NamedHierarchy>();
        matched.forEach(m -> {
            var record = mapper.convertValue(m, NamedHierarchyRecord.class);
            result.add(record.namedHierarchy());
        });
        return result;
    }

    public void setNamedHierarchies(ProjectId projectId, List<NamedHierarchy> namedHierarchies) {
        var collection = mongoTemplate.getCollection(COLLECTION_NAME);
        var projectIdFilter = Filters.eq(PROJECT_ID, projectId.id());
        var insertOps = namedHierarchies.stream()
                .map(h -> new NamedHierarchyRecord(projectId, h))
                        .map(h -> mapper.convertValue(h, Document.class))
                                .map(InsertOneModel::new)
                                        .toList();
        var allOps = new ArrayList<WriteModel<Document>>();
        allOps.add(new DeleteManyModel<>(projectIdFilter));
        allOps.addAll(insertOps);
        collection.bulkWrite(allOps);
    }
}
