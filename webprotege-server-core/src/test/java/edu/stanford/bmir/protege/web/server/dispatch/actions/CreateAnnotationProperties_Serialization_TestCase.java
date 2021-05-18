package edu.stanford.bmir.protege.web.server.dispatch.actions;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.dispatch.Action;
import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.entity.CreateAnnotationPropertiesAction;
import edu.stanford.bmir.protege.web.server.entity.CreateAnnotationPropertiesResult;
import edu.stanford.bmir.protege.web.server.event.EventList;
import edu.stanford.bmir.protege.web.server.event.EventTag;
import edu.stanford.bmir.protege.web.server.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.server.project.ProjectId;
import org.junit.Test;

import java.io.IOException;

import static com.google.common.collect.ImmutableSet.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-06
 */
public class CreateAnnotationProperties_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = CreateAnnotationPropertiesAction.create(ProjectId.getNil(),
                                                             "A\nB",
                                                             "en", of());
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = CreateAnnotationPropertiesResult.create(ProjectId.getNil(),
                                                             ImmutableSet.of(),
                                                             EventList.create(EventTag.get(2), ImmutableList.of(), EventTag.get(2)));
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}
