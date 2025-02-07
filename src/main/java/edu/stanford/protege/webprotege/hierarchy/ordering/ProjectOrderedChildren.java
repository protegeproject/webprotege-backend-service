package edu.stanford.protege.webprotege.hierarchy.ordering;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.stanford.protege.webprotege.common.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.annotation.Nullable;


@Document(collection = ProjectOrderedChildren.ORDERED_CHILDREN_COLLECTION)
public record ProjectOrderedChildren(@Id @JsonProperty("entityUri") String entityUri,
                                     @Indexed(name = "projectId_idx") @JsonProperty("projectId") ProjectId projectId,
                                     @JsonProperty("parentUri") String parentUri,
                                     @Nullable @JsonProperty("userId") String userId,
                                     @JsonProperty("index") String index) {
    public static final String ORDERED_CHILDREN_COLLECTION = "ProjectOrderedChildren";
}
