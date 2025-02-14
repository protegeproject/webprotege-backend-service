package edu.stanford.protege.webprotege.issues;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.MockingUtils;
import edu.stanford.protege.webprotege.common.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 9 Mar 2017
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class DiscussionThreadParticipantsExtractor_TestCase {

    private DiscussionThreadParticipantsExtractor extractor;

    @Mock
    private CommentParticipantsExtractor commentsExtractor;

    @Mock
    private EntityDiscussionThread thread;

    @Mock
    private Comment commentA, commentB;

    private UserId participantA = MockingUtils.mockUserId(), participantB = MockingUtils.mockUserId();

    @BeforeEach
    public void setUp() throws Exception {
        when(thread.getComments()).thenReturn(ImmutableList.of(commentA, commentB));
        when(commentsExtractor.extractParticipants(commentA)).thenReturn(ImmutableSet.of(participantA));
        when(commentsExtractor.extractParticipants(commentB)).thenReturn(ImmutableSet.of(participantB));
        extractor = new DiscussionThreadParticipantsExtractor(commentsExtractor);
    }

    @Test
    public void shouldExtractCommentParticipants() {
        Set<UserId> participants = extractor.extractParticipants(thread);
        assertThat(participants, hasItems(participantA, participantB));
    }
}
