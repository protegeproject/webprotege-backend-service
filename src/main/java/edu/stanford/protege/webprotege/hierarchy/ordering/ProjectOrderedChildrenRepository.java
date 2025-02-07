package edu.stanford.protege.webprotege.hierarchy.ordering;

import com.mongodb.client.model.InsertOneModel;
import org.bson.Document;

import java.util.List;

public interface ProjectOrderedChildrenRepository {

    void bulkWriteDocuments(List<InsertOneModel<Document>> listOfInsertOneModelDocument);
}
