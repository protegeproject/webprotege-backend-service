package edu.stanford.protege.webprotege.watches;

import edu.stanford.protege.webprotege.jackson.ObjectMapperProvider;
import edu.stanford.protege.webprotege.common.UserId;
import org.junit.Test;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.IRI;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-05-19
 *
 * Test that the Watch documents stored by Morphia can be read by Jackson
 */
public class Watch_JsonDocument_Regression_TestCase {

    
    private final static String document = "{\"_id\" : \"ObjectId(\\\"60a54871daeba784da91904d\\\")\",\"entity\" : {\"type\" : \"Class\",\"iri\" : \"http://the.ontology/ClsA\"},\"projectId\" : \"b6fc02d5-392f-415a-898d-2062010ecf04\",\"userId\" : \"The User\",\"type\" : \"ENTITY\"}\n";

    @Test
    public void shouldDeserializeDocument() throws IOException {
        var objectMapper = new ObjectMapperProvider().get();
        var watch = objectMapper.readValue(document, Watch.class);
        assertThat(watch.getUserId(), is(UserId.getUserId("The User")));
        assertThat(watch.getEntity().getEntityType(), is(EntityType.CLASS));
        assertThat(watch.getEntity().getIRI(), is(IRI.create("http://the.ontology/ClsA")));
        assertThat(watch.getType(), is(WatchType.ENTITY));
    }
}
