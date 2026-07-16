package edu.stanford.protege.webprotege.sharing;

import edu.stanford.protege.webprotege.MockingUtils;
import edu.stanford.protege.webprotege.app.PlaceUrl;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.filemanager.FileContents;
import edu.stanford.protege.webprotege.mail.MessageHeader;
import edu.stanford.protege.webprotege.mail.SendMail;
import edu.stanford.protege.webprotege.project.ProjectDetails;
import edu.stanford.protege.webprotege.project.ProjectDetailsRepository;
import edu.stanford.protege.webprotege.templates.TemplateEngine;
import edu.stanford.protege.webprotege.user.UserDetails;
import edu.stanford.protege.webprotege.user.UserDetailsManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.after;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Covers {@link SharingInvitationEmailer}.  Emails are sent asynchronously, so successful-send
 * assertions use Mockito {@code timeout(...)} and skip assertions use {@code after(...).never()}.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class SharingInvitationEmailer_TestCase {

    @Mock
    private ProjectDetailsRepository projectDetailsRepository;

    @Mock
    private UserDetailsManager userDetailsManager;

    @Mock
    private FileContents templateFile;

    @Mock
    private TemplateEngine templateEngine;

    @Mock
    private PlaceUrl placeUrl;

    @Mock
    private SendMail sendMail;

    private SharingInvitationEmailer emailer;

    private final ProjectId projectId = MockingUtils.mockProjectId();

    private final UserId invitedBy = UserId.valueOf("owner");

    @BeforeEach
    public void setUp() {
        emailer = new SharingInvitationEmailer(projectDetailsRepository,
                                               userDetailsManager,
                                               templateFile,
                                               templateEngine,
                                               placeUrl,
                                               sendMail);
        when(projectDetailsRepository.findOne(any())).thenReturn(Optional.empty());
        when(placeUrl.getApplicationUrl()).thenReturn("http://webprotege.test");
        when(templateFile.exists()).thenReturn(true);
    }

    @AfterEach
    public void tearDown() {
        emailer.shutdown();
    }

    @Test
    public void shouldSendEmailWithTrimmedRecipientAndRenderedBody() {
        when(templateFile.getContents()).thenReturn("template body");
        when(userDetailsManager.getUserDetails(invitedBy)).thenReturn(Optional.empty());
        when(templateEngine.populateTemplate(anyString(), any())).thenReturn("rendered body");

        emailer.sendInvitationEmail(projectId, "  new@x.org  ", invitedBy);

        verify(sendMail, timeout(2000)).sendMail(eq(List.of("new@x.org")), anyString(), eq("rendered body"),
                                                 new MessageHeader[0]);

        // The template is populated with the inviter (falling back to the raw id here), the project
        // display name and the application url.
        ArgumentCaptor<Map<String, Object>> objectsCaptor = ArgumentCaptor.forClass(Map.class);
        verify(templateEngine).populateTemplate(eq("template body"), objectsCaptor.capture());
        Map<String, Object> objects = objectsCaptor.getValue();
        assertThat(objects.get("inviter"), is("owner"));
        assertThat(objects.get("application.url"), is("http://webprotege.test"));
        assertThat(objects.get("project.displayName"), is("a project"));
    }

    @Test
    public void shouldRenderTheInviterDisplayNameWhenAvailable() {
        when(templateFile.getContents()).thenReturn("template body");
        when(userDetailsManager.getUserDetails(invitedBy))
                .thenReturn(Optional.of(UserDetails.getUserDetails(invitedBy, "The Owner", Optional.empty())));
        when(templateEngine.populateTemplate(anyString(), any())).thenReturn("rendered body");

        emailer.sendInvitationEmail(projectId, "new@x.org", invitedBy);

        ArgumentCaptor<Map<String, Object>> objectsCaptor = ArgumentCaptor.forClass(Map.class);
        verify(templateEngine, timeout(2000)).populateTemplate(anyString(), objectsCaptor.capture());
        assertThat(objectsCaptor.getValue().get("inviter"), is("The Owner"));
    }

    @Test
    public void shouldNotSendWhenTemplateBodyIsBlank() {
        when(templateFile.getContents()).thenReturn("   ");

        emailer.sendInvitationEmail(projectId, "new@x.org", invitedBy);

        verify(sendMail, after(500).never()).sendMail(any(), anyString(), anyString(), any(MessageHeader[].class));
    }

    @Test
    public void shouldNotSendWhenTemplateFileDoesNotExist() {
        // Structural guard: an absent template file skips the send (no coupling to error-text wording).
        when(templateFile.exists()).thenReturn(false);

        emailer.sendInvitationEmail(projectId, "new@x.org", invitedBy);

        verify(sendMail, after(500).never()).sendMail(any(), anyString(), anyString(), any(MessageHeader[].class));
    }

    @Test
    public void shouldStripControlCharactersFromTheSubjectAndInviter() {
        ProjectDetails projectDetails = mock(ProjectDetails.class);
        when(projectDetails.getDisplayName()).thenReturn("Evil\r\nProject");
        when(projectDetailsRepository.findOne(any())).thenReturn(Optional.of(projectDetails));
        when(userDetailsManager.getUserDetails(invitedBy))
                .thenReturn(Optional.of(UserDetails.getUserDetails(invitedBy, "Bad\r\nOwner", Optional.empty())));
        when(templateFile.getContents()).thenReturn("template body");
        when(templateEngine.populateTemplate(anyString(), any())).thenReturn("rendered body");

        emailer.sendInvitationEmail(projectId, "new@x.org", invitedBy);

        ArgumentCaptor<String> subjectCaptor = ArgumentCaptor.forClass(String.class);
        verify(sendMail, timeout(2000)).sendMail(any(), subjectCaptor.capture(), anyString(),
                                                 any(MessageHeader[].class));
        String subject = subjectCaptor.getValue();
        assertThat(subject.contains("\r"), is(false));
        assertThat(subject.contains("\n"), is(false));
        assertThat(subject.contains("EvilProject"), is(true));

        // The inviter that flows into the template body is sanitized too.
        ArgumentCaptor<Map<String, Object>> objectsCaptor = ArgumentCaptor.forClass(Map.class);
        verify(templateEngine).populateTemplate(anyString(), objectsCaptor.capture());
        assertThat(objectsCaptor.getValue().get("inviter"), is("BadOwner"));
    }
}
