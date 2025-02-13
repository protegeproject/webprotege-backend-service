package edu.stanford.protege.webprotege.issues;

import edu.stanford.protege.webprotege.issues.mention.MentionParser;
import edu.stanford.protege.webprotege.common.UserId;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 9 Mar 2017
 */
public class CommentParticipantsExtractor {

    private final MentionParser mentionParser;

    @Inject
    public CommentParticipantsExtractor(@Nonnull MentionParser mentionParser) {
        this.mentionParser = checkNotNull(mentionParser);
    }

    public Collection<UserId> extractParticipants(Comment comment) {
        Set<UserId> participantIds = new HashSet<>();
        participantIds.add(comment.getCreatedBy());
        mentionParser.parseMentions(comment.getBody())
                     .forEach(
                             parsedMention ->
                                     parsedMention.getParsedMention()
                                                  .getMentionedUserId()
                                                  .ifPresent(participantIds::add));
        return participantIds;
    }
}
