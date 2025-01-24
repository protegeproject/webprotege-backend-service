package edu.stanford.protege.webprotege.access;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.protege.webprotege.authorization.ActionId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2 Feb 2018
 */
public class ActionId_Json_TestCase {

    private static final String ID = "TheActionId";

    private ActionId actionId;

    @BeforeEach
    public void setUp() throws Exception {
        actionId = new ActionId(ID);
    }

    @Test
    public void shouldSerializeJson() throws Exception {
        String result = new ObjectMapper().writeValueAsString(actionId);
        assertThat(result, is("\"" + ID + "\""));
    }

    @Test
    public void shouldDeserializeJson() throws Exception {
        ActionId readActionId = new ObjectMapper()
                .readerFor(ActionId.class)
                .readValue("\"" + ID + "\"");
        assertThat(readActionId, is(actionId));
    }
}
