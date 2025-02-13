package edu.stanford.protege.webprotege.issues;

import com.github.mustachejava.DefaultMustacheFactory;
import edu.stanford.protege.webprotege.app.ApplicationNameSupplier;
import edu.stanford.protege.webprotege.app.PlaceUrl;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.entity.OWLEntityData;
import edu.stanford.protege.webprotege.filemanager.FileContents;
import edu.stanford.protege.webprotege.templates.TemplateEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory;
import org.semanticweb.owlapi.model.OWLEntity;

import java.io.File;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10 Mar 2017
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CommentNotificationEmailGenerator_TestCase {

    public static final String TEMPLATE_FILE_CONTENTS = "{{project.displayName}} {{comment.createdBy.id}}";

    public static final String USER_NAME = "John Smith";

    public static final String THE_FILE_NAME = "TheFileName";

    private CommentNotificationEmailGenerator generator;

    @Mock
    private FileContents templateFile;

    @Mock
    private TemplateEngine templateEngine;

    @Mock
    private EntityDiscussionThread thread;

    private OWLEntity entity = OWLFunctionalSyntaxFactory.createClass();

    @Mock
    private OWLEntityData entityData;

    @Mock
    private Comment comment;

    private UserId creator = UserId.valueOf(USER_NAME);

    @Mock
    private File file;

    @Mock
    private PlaceUrl placeUrl;

    @Mock
    private ApplicationNameSupplier applicationNameSupplier;

    private ProjectId projectId = ProjectId.generate();

    @BeforeEach
    public void setUp() throws Exception {
        when(templateFile.getContents()).thenReturn(TEMPLATE_FILE_CONTENTS);
        when(applicationNameSupplier.get())
                .thenReturn("TheAppName");
        when(placeUrl.getProjectUrl(projectId))
                .thenReturn("TheProjectUrl");
        when(placeUrl.getEntityUrl(projectId, entity))
                .thenReturn("TheEntityUrl");
        when(entityData.getEntity())
                .thenReturn(entity);

        when(thread.getProjectId())
                .thenReturn(projectId);
        when(thread.getEntity())
                .thenReturn(entity);

        when(comment.getCreatedBy())
                .thenReturn(creator);

        when(entityData.getBrowserText()).thenReturn("TheBrowserText");
        templateEngine = new TemplateEngine(DefaultMustacheFactory::new);
        generator = new CommentNotificationEmailGenerator(templateFile,
                                                          templateEngine,
                                                          applicationNameSupplier,
                                                          placeUrl
        );
    }

    @Test
    public void shouldPopulateTemplate() {
        String populated = populateTemplate();
        assertThat(populated, is("MyProject John Smith"));
    }

    private String populateTemplate() {
        return generator.generateEmailBody("MyProject", entityData, thread, comment);
    }
}
