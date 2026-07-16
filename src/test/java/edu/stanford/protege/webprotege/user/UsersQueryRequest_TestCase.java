package edu.stanford.protege.webprotege.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class UsersQueryRequest_TestCase {

    @Test
    public void shouldSerializeWithTheJsonFieldNamesTheUserManagementServiceBindsTo() throws Exception {
        var json = new ObjectMapper().readTree(
                new ObjectMapper().writeValueAsString(new UsersQueryRequest("jdoe@x.org", true)));
        assertThat(json.get("completionText").asText(), is("jdoe@x.org"));
        assertThat(json.get("exactMatch").asBoolean(), is(true));
    }

    @Test
    public void shouldDeserializeFromTheWireFieldNames() throws Exception {
        var request = new ObjectMapper().readValue(
                "{\"completionText\":\"jdoe@x.org\",\"exactMatch\":false}", UsersQueryRequest.class);
        assertThat(request.completionText(), is("jdoe@x.org"));
        assertThat(request.exactMatch(), is(false));
    }
}
