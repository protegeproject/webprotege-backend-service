package edu.stanford.bmir.protege.web.server.crud;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.crud.gen.GeneratedAnnotationsSettings;
import edu.stanford.bmir.protege.web.server.crud.supplied.SuppliedNameSuffixSettings;
import edu.stanford.bmir.protege.web.server.dispatch.Action;
import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.server.project.ProjectId;
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
