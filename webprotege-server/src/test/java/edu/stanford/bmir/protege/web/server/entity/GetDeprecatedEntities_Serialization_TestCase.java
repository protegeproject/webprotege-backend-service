package edu.stanford.bmir.protege.web.server.entity;

import edu.stanford.bmir.protege.web.server.dispatch.Action;
import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.server.pagination.Page;
import edu.stanford.bmir.protege.web.server.pagination.PageRequest;
import edu.stanford.bmir.protege.web.server.project.ProjectId;
import org.junit.Test;
import org.semanticweb.owlapi.model.EntityType;

import java.io.IOException;
import java.util.Collections;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class GetDeprecatedEntities_Serialization_TestCase {


    @Test
    public void shouldSerializeAction() throws IOException {
        var action = GetDeprecatedEntitiesAction.create(ProjectId.getNil(),
                                                        PageRequest.requestFirstPage(),
                                                        Collections.singleton(EntityType.CLASS));
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = GetDeprecatedEntitiesResult.create(Page.emptyPage());
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}
