package edu.stanford.bmir.protege.web.shared.form;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.MockingUtils;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.lang.LangTagFilter;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.Test;

import java.io.IOException;

import static edu.stanford.bmir.protege.web.MockingUtils.mockOWLClass;
import static edu.stanford.bmir.protege.web.MockingUtils.mockOWLClassData;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class GetEntityForms_Serialization_TestCase {


    @Test
    public void shouldSerializeAction() throws IOException {
        var action = GetEntityFormsAction.create(ProjectId.getNil(),
                                                 mockOWLClass(),
                                                 ImmutableSet.of(),
                                                 LangTagFilter.get(ImmutableSet.of()),
                                                 ImmutableSet.of(),
                                                 ImmutableSet.of(),
                                                 ImmutableSet.of());
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = GetEntityFormsResult.create(mockOWLClassData(),
                                                 ImmutableList.of(),
                                                 ImmutableList.of());
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }

}
