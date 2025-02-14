package edu.stanford.protege.webprotege.icd.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProjectSummaryDto(@JsonProperty("projectId") String projectId,
                                @JsonProperty("title") String title,
                                @JsonProperty("description") String desciption) {

    public static ProjectSummaryDto create(String projectId, String title, String desciption) {
        return new ProjectSummaryDto(projectId, title, desciption);
    }

}
