package edu.stanford.bmir.protege.web.server.dispatch.actions;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.dispatch.Action;
import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.entity.CreateNamedIndividualsAction;
import edu.stanford.bmir.protege.web.server.entity.CreateNamedIndividualsResult;
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
public class CreateNamedIndividuals_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = CreateNamedIndividualsAction.create(ProjectId.getNil(),
                                                         "i\nj",
                                                         "en", of());
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = CreateNamedIndividualsResult.create(ProjectId.getNil(),
                                                         EventList.create(EventTag.get(2), ImmutableList.of(), EventTag.get(2)),
                                                         ImmutableSet.of());
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}
