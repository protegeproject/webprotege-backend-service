package edu.stanford.bmir.protege.web.server.event;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.MockingUtils;
import edu.stanford.bmir.protege.web.server.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;

import static edu.stanford.bmir.protege.web.MockingUtils.*;
import static edu.stanford.bmir.protege.web.MockingUtils.mockUserId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-15
 */
public class EventList_Serialization_TestCase {

    @Test
    public void shouldSerializeEvent() throws IOException {
        var events = ImmutableList.of(new ClassFrameChangedEvent(MockingUtils.mockOWLClass(),
                                                                 MockingUtils.mockProjectId(),
                                                                 MockingUtils.mockUserId()));
        var eventList = EventList.create(EventTag.get(2), events, EventTag.get(3));
        JsonSerializationTestUtil.testSerialization(eventList, EventList.class);
    }
}
