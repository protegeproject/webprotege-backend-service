package edu.stanford.protege.webprotege.place;

import edu.stanford.protege.webprotege.DataFactory;
import edu.stanford.protege.webprotege.app.*;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.perspective.EntityTypePerspectiveMapper;
import edu.stanford.protege.webprotege.perspective.PerspectiveId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10 Mar 2017
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PlaceUrl_TestCase {

    public static final String THE_APPLICATION_HOST = "the.application.host";

    public static final String THE_APPLICATION_PATH = "/the/application/path";

    public static final String THE_APPLICATION_NAME = "the.application.name";

    public static final String EXPECTED_URL_BASE = "https://the.application.host/the/application/path";

    private PlaceUrl placeUrl;

    private ProjectId projectId = ProjectId.valueOf(UUID.randomUUID().toString());

    private IRI entityIri = IRI.create("http://the.ontology/entity");

    private EntityType<OWLClass> entityType = EntityType.CLASS;

    private OWLEntity entity = DataFactory.getOWLEntity(entityType, entityIri);

    @Mock
    private EntityTypePerspectiveMapper typeMapper;

    @Mock
    private ApplicationHostSupplier hostProvider;

    @Mock
    private ApplicationPortSupplier portProvider;

    @Mock
    private ApplicationPathSupplier pathProvider;

    @Mock
    private ApplicationNameSupplier appNameProvider;

    @Mock
    private ApplicationSchemeSupplier schemeProvider;

    @BeforeEach
    public void setUp() throws Exception {
        when(typeMapper.getPerspectiveId(ArgumentMatchers.any())).thenReturn(PerspectiveId.get("12345678-1234-1234-1234-123456789abc"));
        when(typeMapper.getDefaultPerspectiveId()).thenReturn(PerspectiveId.get("aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee"));

        when(hostProvider.get()).thenReturn(THE_APPLICATION_HOST);
        when(portProvider.get()).thenReturn(Optional.empty());
        when(pathProvider.get()).thenReturn(THE_APPLICATION_PATH);
        when(appNameProvider.get()).thenReturn(THE_APPLICATION_NAME);
        when(schemeProvider.get()).thenReturn(ApplicationScheme.HTTPS);

        placeUrl = new PlaceUrl(schemeProvider,
                                hostProvider,
                                portProvider,
                                pathProvider,
                                appNameProvider,
                                typeMapper);
    }

    @Test
    public void shouldBuildApplicationUrl() {
        String url = placeUrl.getApplicationUrl();
        assertThat(url, is(EXPECTED_URL_BASE));
    }

    @Test
    public void shouldBuildApplicationAnchor() {
        String anchor = placeUrl.getApplicationAnchor();
        assertThat(anchor, is("<a href=\"the.application.name\">" + EXPECTED_URL_BASE + "</a>"));
    }

    @Test
    public void shouldBuildProjectUrl() {
        String url = placeUrl.getProjectUrl(projectId);
        assertThat(url, is(EXPECTED_URL_BASE + "#projects/"
                                   + projectId.id()
                                   + "/perspectives/aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee"));
    }

    @Test
    public void shouldBuildEntityUrl() {

        String url = placeUrl.getEntityUrl(projectId, entity);
        assertThat(url, is(EXPECTED_URL_BASE + "#projects/"
                                   + projectId.id()
                                   + "/perspectives/12345678-1234-1234-1234-123456789abc?selection=Class(%3C" + entityIri.toString() + "%3E)"));
    }
}
