package edu.stanford.bmir.protege.web.server.form;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.MockingUtils;
import edu.stanford.bmir.protege.web.server.dispatch.Action;
import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.server.project.ProjectId;
import org.junit.Test;
import org.semanticweb.owlapi.model.EntityType;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class GetEntityCreationForms_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = GetEntityCreationFormsAction.create(ProjectId.getNil(),
                                                         MockingUtils.mockOWLClass(),
                                                         EntityType.CLASS);
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = GetEntityCreationFormsResult.get(ImmutableList.of());
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }

}
