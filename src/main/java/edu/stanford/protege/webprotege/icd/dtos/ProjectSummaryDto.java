package edu.stanford.protege.webprotege.icd.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProjectSummaryDto(@JsonProperty("projectId") String projectId,
                                @JsonProperty("title") String title,

                                @JsonProperty("createdAt") long createdAt,
                                @JsonProperty("description") String description) {

    public static ProjectSummaryDto create(String projectId, String title,long createdAt, String description) {
        return new ProjectSummaryDto(projectId, title,createdAt, description);
    }

}
