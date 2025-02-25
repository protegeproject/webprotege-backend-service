package edu.stanford.protege.webprotege.access;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.protege.webprotege.authorization.RoleId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2 Feb 2018
 */
public class RoleId_Json_TestCase {

    private static final String ID = "TheRoleId";

    private RoleId roleId;

    @BeforeEach
    public void setUp() throws Exception {
        roleId = new RoleId(ID);
    }

    @Test
    public void shouldSerializeJson() throws Exception {
        String result = new ObjectMapper().writeValueAsString(roleId);
        assertThat(result, is("\"" + ID + "\""));
    }

    @Test
    public void shouldDeserializeJson() throws Exception {
        RoleId readRoleId = new ObjectMapper()
                .readerFor(RoleId.class)
                .readValue("\"" + ID + "\"");
        assertThat(readRoleId, is(roleId));
    }
}
