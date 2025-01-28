package edu.stanford.protege.webprotege.mail;

import edu.stanford.protege.webprotege.app.ApplicationHostSupplier;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Apr 2017
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class MessageIdGenerator_TestCase {

    private static final String THE_APPLICATION_HOST = "the.application.host";

    private static final String THE_OBJECT_CATEGORY = "TheObjectCategory";

    private final String projectIdString = UUID.randomUUID().toString();

    private final ProjectId projectId = ProjectId.valueOf(projectIdString);

    private MessageIdGenerator generator;

    @Mock
    private ApplicationHostSupplier hostSupplier;

    @BeforeEach
    public void setUp() throws Exception {
        when(hostSupplier.get()).thenReturn(THE_APPLICATION_HOST);
        generator = new MessageIdGenerator(hostSupplier);
    }

    @Test
public void shouldThrowNullPointerExceptionIf_ProjectId_IsNull() {
    assertThrows(NullPointerException.class, () -> { 
        generator.generateProjectMessageId(null, THE_OBJECT_CATEGORY, UUID.randomUUID().toString());
     });
}

    @Test
public void shouldThrowNullPointerExceptionIf_ObjectCategory_IsNull() {
    assertThrows(NullPointerException.class, () -> { 
        generator.generateProjectMessageId(projectId, null, UUID.randomUUID().toString());
     });
}

    @Test
public void shouldThrowNullPointerExceptionIf_ObjectId_IsNull() {
    assertThrows(NullPointerException.class, () -> { 
        generator.generateProjectMessageId(projectId, THE_OBJECT_CATEGORY, null);
     });
}

    @Test
    public void shouldGenerateMessageId() {
        String objectId = UUID.randomUUID().toString();
        MessageId msgId = generator.generateProjectMessageId(projectId, THE_OBJECT_CATEGORY, objectId);
        assertThat(msgId.getId(), is("<projects/" + projectIdString + "/" + THE_OBJECT_CATEGORY + "/" + objectId + "@" + THE_APPLICATION_HOST + ">"));
    }
}
