package edu.stanford.protege.webprotege.icd.mappers;

import edu.stanford.protege.webprotege.icd.dtos.*;
import edu.stanford.protege.webprotege.issues.EntityDiscussionThread;

import java.util.List;



public class EntityCommentThreadMapper {
    public static EntityCommentThread mapFromDiscussionThread(EntityDiscussionThread discThread) {
        List<EntityComment> comments = discThread.getComments().stream()
                .map(EntityCommentMapper::mapFromComment)
                .toList();

        return EntityCommentThread.create(discThread.getProjectId(),discThread.getEntity().toStringID(),discThread.getStatus().name(), comments);
    }
}
