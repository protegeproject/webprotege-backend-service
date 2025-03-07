package edu.stanford.protege.webprotege.hierarchy.ordering;

import com.mongodb.client.model.UpdateOneModel;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import org.bson.Document;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ProjectOrderedChildrenRepository {

    void bulkWriteDocuments(List<UpdateOneModel<Document>> listOfUpdateOneModelDocument);

    Set<String> findExistingEntries(List<ProjectOrderedChildren> childrenToCheck);

    Optional<ProjectOrderedChildren> findOrderedChildren(ProjectId projectId, String entityUri);

    Optional<ProjectOrderedChildren> findOrderedChildren(ProjectId projectId, String entityUri, UserId userId);

    void save(ProjectOrderedChildren projectOrderedChildren);

    void insert(ProjectOrderedChildren projectOrderedChildren);

    void delete(ProjectOrderedChildren projectOrderedChildren);

    void update(ProjectOrderedChildren updatedEntry);

    Optional<ProjectOrderedChildren> updateAndGet(ProjectOrderedChildren updatedEntry);

    Optional<ProjectOrderedChildren> insertAndGet(ProjectOrderedChildren projectOrderedChildren);
}
