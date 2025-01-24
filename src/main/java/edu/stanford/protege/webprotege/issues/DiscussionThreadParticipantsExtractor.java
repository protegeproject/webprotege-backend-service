package edu.stanford.protege.webprotege.issues;

import edu.stanford.protege.webprotege.common.UserId;

import jakarta.inject.Inject;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 9 Mar 2017
 */
public class DiscussionThreadParticipantsExtractor {

    private final CommentParticipantsExtractor extractor;

    @Inject
    public DiscussionThreadParticipantsExtractor(CommentParticipantsExtractor extractor) {
        this.extractor = extractor;
    }

    public Set<UserId> extractParticipants(EntityDiscussionThread thread) {
        return thread.getComments().stream()
                .flatMap(comment -> extractor.extractParticipants(comment).stream())
                .collect(toSet());
    }
}
