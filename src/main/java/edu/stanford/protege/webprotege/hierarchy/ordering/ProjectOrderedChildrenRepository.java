package edu.stanford.protege.webprotege.hierarchy.ordering;

import com.mongodb.client.model.UpdateOneModel;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.bson.Document;

import java.util.List;
import java.util.Set;

public interface ProjectOrderedChildrenRepository {

    void bulkWriteDocuments(List<UpdateOneModel<Document>> listOfUpdateOneModelDocument);

    Set<String> findExistingEntries(List<ProjectOrderedChildren> childrenToCheck);

    List<ProjectOrderedChildren> findOrderedChildren(ProjectId projectId, String parentUri);
}
