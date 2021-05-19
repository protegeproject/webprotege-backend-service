package edu.stanford.protege.webprotege.dispatch.actions;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.entity.CreateObjectPropertiesAction;
import edu.stanford.protege.webprotege.entity.CreateObjectPropertiesResult;
import edu.stanford.protege.webprotege.event.EventList;
import edu.stanford.protege.webprotege.event.EventTag;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import edu.stanford.protege.webprotege.project.ProjectId;
import org.junit.Test;

import java.io.IOException;

import static com.google.common.collect.ImmutableSet.of;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-06
 */
public class CreateObjectProperties_Serialization_TestCase {


    @Test
    public void shouldSerializeAction() throws IOException {
        var action = CreateObjectPropertiesAction.create(ProjectId.getNil(), "P\nQ", "en", of());
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = CreateObjectPropertiesResult.create(ProjectId.getNil(),
                                                         ImmutableSet.of(),
                                                         EventList.create(EventTag.get(2),
                                                                          ImmutableList.of(),
                                                                          EventTag.get(2)));
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}
