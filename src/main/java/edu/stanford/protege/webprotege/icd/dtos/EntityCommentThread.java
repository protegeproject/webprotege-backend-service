package edu.stanford.protege.webprotege.icd.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.stanford.protege.webprotege.common.ProjectId;

import java.util.List;

public record EntityCommentThread(@JsonProperty("projectId") ProjectId projectId,
                                  @JsonProperty("entityIRI") String entityIRI,
                                  @JsonProperty("status") String status,
                                  @JsonProperty("comments") List<EntityComment> entityComments) {

    public static EntityCommentThread create(ProjectId projectId,
                                             String entityIRI,
                                             String status,
                                             List<EntityComment> entityComments) {
        return new EntityCommentThread(projectId, entityIRI, status, entityComments);
    }

}
