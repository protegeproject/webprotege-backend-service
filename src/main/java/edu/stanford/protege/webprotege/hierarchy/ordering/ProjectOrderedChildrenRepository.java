package edu.stanford.protege.webprotege.hierarchy.ordering;

import com.mongodb.client.model.UpdateOneModel;
import edu.stanford.protege.webprotege.common.*;
import org.bson.Document;

import java.util.*;

public interface ProjectOrderedChildrenRepository {

    void bulkWriteDocuments(List<UpdateOneModel<Document>> listOfUpdateOneModelDocument);

    Set<String> findExistingEntries(List<ProjectOrderedChildren> childrenToCheck);

    Optional<ProjectOrderedChildren> findOrderedChildren(ProjectId projectId, String entityUri);

    Optional<ProjectOrderedChildren> findOrderedChildren(ProjectId projectId, String entityUri, UserId userId);

    void save(ProjectOrderedChildren projectOrderedChildren);

    void insert(ProjectOrderedChildren projectOrderedChildren);

    void delete(ProjectOrderedChildren projectOrderedChildren);

    void update(ProjectOrderedChildren updatedEntry);
}
