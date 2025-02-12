package edu.stanford.protege.webprotege.hierarchy.ordering;

import com.mongodb.client.model.UpdateOneModel;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.bson.Document;

import java.util.*;

public interface ProjectOrderedChildrenRepository {

    void bulkWriteDocuments(List<UpdateOneModel<Document>> listOfUpdateOneModelDocument);

    Set<String> findExistingEntries(List<EntityChildrenOrdering> childrenToCheck);

    Optional<EntityChildrenOrdering> findOrderedChildren(ProjectId projectId, String entityUri);

    void save(EntityChildrenOrdering projectOrderedChildren);

    void delete(EntityChildrenOrdering projectOrderedChildren);

    void update(EntityChildrenOrdering updatedEntry);
}
