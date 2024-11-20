package edu.stanford.protege.webprotege.icd.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.stanford.protege.webprotege.common.UserId;

import java.time.*;

public record EntityComment(@JsonProperty("createdBy") UserId userId,
                            @JsonProperty("createdAt") ZonedDateTime createdAt,
                            @JsonProperty("updatedAt") ZonedDateTime updatedAt,
                            @JsonProperty("body") String body) {

    public static EntityComment create(UserId userId,
                                       ZonedDateTime createdAt,
                                       ZonedDateTime updatedAt,
                                       String body) {
        return new EntityComment(userId, createdAt, updatedAt, body);
    }

}
