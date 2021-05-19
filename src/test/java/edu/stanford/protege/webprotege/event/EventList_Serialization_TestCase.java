package edu.stanford.protege.webprotege.event;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.MockingUtils;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;

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
