package edu.stanford.protege.webprotege.icd.dtos;

import java.util.List;

public record EntityComments(List<EntityCommentThread> commentThreads) {
    public static EntityComments create(List<EntityCommentThread> commentThreads) {
        return new EntityComments(commentThreads);
    }
}
