package edu.stanford.protege.webprotege.place;

import edu.stanford.protege.webprotege.common.ProjectId;
import org.semanticweb.owlapi.model.IRI;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14/12/15
 */
public class ProjectViewPlaceTokenizer_TestCase {

    private final ProjectId projectId = ProjectId.valueOf("aaaabbbb-cccc-dddd-eeee-ffffffffffffffff");

    private final IRI entityIri = IRI.create("http://stuff.com#A");

    private ProjectViewPlaceTokenizer tokenizer;

    private String token = "projects/" + projectId + "/edit/MyPerspective?selection=Class(<" + entityIri.toString() + ">)";

//    @Before
//    public void setUp() throws Exception {
//        tokenizer = new ProjectViewPlaceTokenizer();
//    }
//
//    @Test
//    public void shouldGetPlaceWithProjectId() {
//        ProjectViewPlace place = tokenizer.getPlace(token);
//        assertThat(place.getProjectId(), is(projectId));
//    }
//
//    @Test
//    public void shouldGetPlaceWithPerspectiveId() {
//        ProjectViewPlace place = tokenizer.getPlace(token);
//        assertThat(place.getPerspectiveId().getId(), is("MyPerspective"));
//    }
//
//    @Test
//    public void shouldGetPlaceWithSelection() {
//        ProjectViewPlace place = tokenizer.getPlace(token);
//        Optional<Item<?>> firstItem = place.getItemSelection().getFirst();
//        assertThat(firstItem.isPresent(), is(true));
//    }
}
