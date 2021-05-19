package edu.stanford.protege.webprotege.form;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.MockingUtils;
import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import edu.stanford.protege.webprotege.project.ProjectId;
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
