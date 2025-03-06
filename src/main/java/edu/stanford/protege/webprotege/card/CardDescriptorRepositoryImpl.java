package edu.stanford.protege.webprotege.card;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.model.InsertOneOptions;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.UpdateOptions;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class CardDescriptorRepositoryImpl implements CardDescriptorRepository {

    public static final String COLLECTION_NAME = "CardDescriptors";
    private final MongoTemplate mongoTemplate;

    private final ObjectMapper objectMapper;

    public CardDescriptorRepositoryImpl(MongoTemplate mongoTemplate, ObjectMapper objectMapper) {
        this.mongoTemplate = mongoTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public synchronized void clearCardDescriptors(ProjectId projectId) {
        var collection = mongoTemplate.getCollection(COLLECTION_NAME);
        collection.deleteMany(new Document("_id", projectId.value()));
    }

    @Override
    public synchronized void setCardDescriptors(ProjectId projectId, List<CardDescriptor> cardDescriptors) {
        var collection = mongoTemplate.getCollection(COLLECTION_NAME);
        var record = new ProjectCardDescriptorsRecord(projectId, cardDescriptors);
        var document = objectMapper.convertValue(record, Document.class);
        document.put("_id", projectId.id());
        document.remove("projectId");
        var filter = new Document("_id", projectId.id());
        collection.replaceOne(filter,
                document, new ReplaceOptions().upsert(true));
    }

    @Override
    public List<CardDescriptor> getCardDescriptors(ProjectId projectId) {
        var collection = mongoTemplate.getCollection(COLLECTION_NAME);
        var found = collection.find(new Document("_id", projectId.id())).first();
        if(found == null) {
            return List.of();
        }
        found.remove("_id");
        found.put("projectId", projectId.id());
        var record = objectMapper.convertValue(found, ProjectCardDescriptorsRecord.class);
        return record.cardDescriptors();
    }

    @Override
    public void ensureIndexes() {
        // Nothing to do.  ProjectId is represented as _id and the collection is
        // indexed on _id by default.
    }
}
