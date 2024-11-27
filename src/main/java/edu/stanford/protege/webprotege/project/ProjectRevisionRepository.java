package edu.stanford.protege.webprotege.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.inject.ApplicationSingleton;
import edu.stanford.protege.webprotege.persistence.Repository;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;



@ApplicationSingleton
public class ProjectRevisionRepository implements Repository {

    public static final String CHANGE_REQUEST_ID = "changeRequestId";
    public static final String PROJECTS_REVISIONS = "ProjectsRevisions";
    public static final String PROJECT_ID = "projectId";

    private final MongoCollection<Document> collection;
    private final ObjectMapper objectMapper;

    @Inject
    public ProjectRevisionRepository(MongoTemplate mongoTemplate, ObjectMapper objectMapper) {

        this.collection = mongoTemplate.getCollection(PROJECTS_REVISIONS);

        this.objectMapper = objectMapper;
    }

    @Override
    public void ensureIndexes() {

    }

    public void save(@Nonnull ProjectRevision projectRevision) {
        var document = objectMapper.convertValue(projectRevision, Document.class);
        collection.insertOne(document);
    }

    private static Document withProjectId(@Nonnull ProjectId projectId) {
        return new Document(PROJECT_ID, projectId.id());
    }

    public List<ProjectRevision> findByChangeRequest(ProjectId projectId, ChangeRequestId changeRequestId) {
        ArrayList<ProjectRevision> result = new ArrayList<>();
        collection.find(withProjectIdAndWithChangeRequestId(projectId, changeRequestId))
                .map(d -> objectMapper.convertValue(d, ProjectRevision.class)).into(result);
        return result;
    }

    private static Document withProjectIdAndWithChangeRequestId(@Nonnull ProjectId projectId,
                                                                @Nonnull ChangeRequestId changeRequestId) {
        return new Document(PROJECT_ID, projectId.id()).append(CHANGE_REQUEST_ID, changeRequestId.id());
    }
}
