package edu.stanford.protege.webprotege.icd.mappers;

import edu.stanford.protege.webprotege.icd.dtos.EntityComment;
import edu.stanford.protege.webprotege.issues.Comment;

import java.time.*;

public class EntityCommentMapper {

    public static EntityComment mapFromComment(Comment comment) {
        var createdAtInstant = Instant.ofEpochMilli(comment.getCreatedAt());
        var createdAtUtc = ZonedDateTime.ofInstant(createdAtInstant, ZoneId.systemDefault());
        createdAtUtc = createdAtUtc.withZoneSameInstant(ZoneId.of("UTC"));
        var updatedAtOptional = comment.getUpdatedAt();
        ZonedDateTime updatedAt = null;
        if (updatedAtOptional.isPresent()) {
            var updatedAtInstant = Instant.ofEpochMilli(updatedAtOptional.get());
            updatedAt = ZonedDateTime.ofInstant(updatedAtInstant, ZoneId.systemDefault());
            updatedAt = updatedAt.withZoneSameInstant(ZoneId.of("UTC"));
        }
        return EntityComment.create(comment.getCreatedBy(), createdAtUtc, updatedAt, comment.getBody());
    }
}
