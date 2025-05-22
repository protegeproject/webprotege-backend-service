package edu.stanford.protege.webprotege.icd.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProjectSummaryDto(@JsonProperty("projectId") String projectId,
                                @JsonProperty("title") String title,

                                @JsonProperty("createdAt") long createdAt,
                                @JsonProperty("description") String description,
                                @JsonProperty("gitRepoBranch") String gitRepoBranch) {

    public static ProjectSummaryDto create(String projectId, String title,long createdAt, String description, String gitRepoBranch) {
        return new ProjectSummaryDto(projectId, title,createdAt, description, gitRepoBranch);
    }

}
