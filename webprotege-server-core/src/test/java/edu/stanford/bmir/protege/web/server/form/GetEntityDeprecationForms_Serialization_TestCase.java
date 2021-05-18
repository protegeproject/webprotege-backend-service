package edu.stanford.bmir.protege.web.server.form;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.MockingUtils;
import edu.stanford.bmir.protege.web.server.dispatch.Action;
import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.server.match.criteria.CompositeRootCriteria;
import edu.stanford.bmir.protege.web.server.match.criteria.MultiMatchType;
import edu.stanford.bmir.protege.web.server.project.ProjectId;
import org.junit.Test;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class GetEntityDeprecationForms_Serialization_TestCase {


    @Test
    public void shouldSerializeAction() throws IOException {
        var action = GetEntityDeprecationFormsAction.create(ProjectId.getNil(), MockingUtils.mockOWLClass());
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = GetEntityDeprecationFormsResult.create(ImmutableList.of(), 4, CompositeRootCriteria.get(ImmutableList.of(), MultiMatchType.ALL));
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}
