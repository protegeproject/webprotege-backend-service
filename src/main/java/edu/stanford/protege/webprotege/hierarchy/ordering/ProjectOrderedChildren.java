package edu.stanford.protege.webprotege.hierarchy.ordering;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.annotation.Nullable;
import java.util.List;


@Document(collection = ProjectOrderedChildren.ORDERED_CHILDREN_COLLECTION)
@CompoundIndex(name = "unique_parent_entity_project",
        def = "{'"+ ProjectOrderedChildren.ENTITY_URI+ "': 1, " +
        "'"+ ProjectOrderedChildren.PROJECT_ID+"': 1}",
        unique = true)
public record ProjectOrderedChildren(
        @Indexed(name = ENTITY_URI+"_idx") @JsonProperty(ENTITY_URI) String entityUri,
        @Indexed(name = PROJECT_ID+"_idx") @JsonProperty(PROJECT_ID) ProjectId projectId,
        @JsonProperty(CHILDREN) List<String> children,
        @Nullable @JsonProperty(USER_ID) String userId
) {
    public static final String ORDERED_CHILDREN_COLLECTION = "ProjectOrderedChildren";
    public static final String PROJECT_ID = "projectId";
    public static final String ENTITY_URI = "entityUri";
    public static final String USER_ID = "userId";
    public static final String CHILDREN = "children";
}

