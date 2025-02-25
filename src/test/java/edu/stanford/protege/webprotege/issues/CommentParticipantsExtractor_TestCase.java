package edu.stanford.protege.webprotege.issues;

import edu.stanford.protege.webprotege.MockingUtils;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.issues.mention.MentionParser;
import edu.stanford.protege.webprotege.issues.mention.ParsedMention;
import edu.stanford.protege.webprotege.issues.mention.UserIdMention;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 9 Mar 2017
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CommentParticipantsExtractor_TestCase {

    public static final String USER_COMMENT = "User Comment";

    private CommentParticipantsExtractor extractor;

    @Mock
    private MentionParser mentionParser;

    @Mock
    private ParsedMention parsedMention;

    @Mock
    private UserIdMention userMention;

    private UserId creatorId = MockingUtils.mockUserId(), mentionedUserId = MockingUtils.mockUserId();

    @Mock
    private Comment comment;

    @BeforeEach
    public void setUp() throws Exception {
        var parsedMentions = Collections.singletonList(parsedMention);
        when(mentionParser.parseMentions(USER_COMMENT)).thenReturn(parsedMentions);

        when(parsedMention.getParsedMention()).thenReturn(userMention);
        when(userMention.getMentionedUserId()).thenReturn(Optional.of(mentionedUserId));

        when(comment.getBody()).thenReturn(USER_COMMENT);
        when(comment.getCreatedBy()).thenReturn(creatorId);
        extractor = new CommentParticipantsExtractor(mentionParser);
    }

    @Test
    public void shouldExtractCreator() {
        assertThat(extractor.extractParticipants(comment), hasItem(creatorId));
    }

    @Test
    public void shouldExtractMentionedUser() {
        assertThat(extractor.extractParticipants(comment), hasItem(mentionedUserId));
    }

}
