package edu.stanford.protege.webprotege.crud;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.crud.gen.GeneratedAnnotationsSettings;
import edu.stanford.protege.webprotege.crud.supplied.SuppliedNameSuffixSettings;
import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import edu.stanford.protege.webprotege.project.ProjectId;
import org.junit.Test;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public abstract class GetEntityCrudKits_Serialization_TestCase {


    @Test
    public void shouldSerializeAction() throws IOException {
        var action = GetEntityCrudKitsAction.create(ProjectId.getNil());
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = GetEntityCrudKitsResult.create(ImmutableList.of(),
                                                    EntityCrudKitSettings.get(
                                                            EntityCrudKitPrefixSettings.get(),
                                                            SuppliedNameSuffixSettings.get(),
                                                            GeneratedAnnotationsSettings.empty()
                                                    ));
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}
