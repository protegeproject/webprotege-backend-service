package edu.stanford.protege.webprotege.form;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.MockingUtils;
import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import edu.stanford.protege.webprotege.match.criteria.CompositeRootCriteria;
import edu.stanford.protege.webprotege.match.criteria.MultiMatchType;
import edu.stanford.protege.webprotege.project.ProjectId;
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
