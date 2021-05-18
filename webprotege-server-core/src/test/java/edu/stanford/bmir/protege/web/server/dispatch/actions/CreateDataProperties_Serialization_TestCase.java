package edu.stanford.bmir.protege.web.server.dispatch.actions;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.dispatch.Action;
import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.entity.CreateDataPropertiesAction;
import edu.stanford.bmir.protege.web.server.entity.CreateDataPropertiesResult;
import edu.stanford.bmir.protege.web.server.event.EventList;
import edu.stanford.bmir.protege.web.server.event.EventTag;
import edu.stanford.bmir.protege.web.server.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.server.project.ProjectId;
import org.junit.Test;

import java.io.IOException;

import static com.google.common.collect.ImmutableSet.of;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-06
 */
public class CreateDataProperties_Serialization_TestCase {
    
    @Test
    public void shouldSerializeAction() throws IOException {
        var action = CreateDataPropertiesAction.create(ProjectId.getNil(),
                                                       "P\nQ",
                                                       "en", of());
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = CreateDataPropertiesResult.create(ProjectId.getNil(),
                                                       ImmutableSet.of(),
                                                       EventList.create(EventTag.get(2), ImmutableList.of(), EventTag.get(2)));
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}
