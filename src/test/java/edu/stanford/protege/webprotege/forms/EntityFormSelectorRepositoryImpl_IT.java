package edu.stanford.protege.webprotege.forms;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.MongoTestExtension;
import edu.stanford.protege.webprotege.WebprotegeBackendMonolithApplication;
import edu.stanford.protege.webprotege.criteria.CompositeRootCriteria;
import edu.stanford.protege.webprotege.criteria.HierarchyFilterType;
import edu.stanford.protege.webprotege.criteria.MultiMatchType;
import edu.stanford.protege.webprotege.criteria.SubClassOfCriteria;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.semanticweb.owlapi.model.IRI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.annotation.DirtiesContext;
import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-08
 */
@SpringBootTest
@Import({WebprotegeBackendMonolithApplication.class})
@ExtendWith({MongoTestExtension.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class EntityFormSelectorRepositoryImpl_IT {

    @Autowired
    private EntityFormSelectorRepositoryImpl repository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void shouldSaveFormTrigger() {
        var projectId = ProjectId.valueOf("609767c5-e12a-43b8-beba-b9f250b35a3a");
        var criteria = CompositeRootCriteria.get(
                ImmutableList.of(SubClassOfCriteria.get(new OWLClassImpl(IRI.create("http://www.co-ode.org/ontologies/amino-acid/2006/05/18/amino-acid.owl#SpecificAminoAcid")), HierarchyFilterType.DIRECT)),
                MultiMatchType.ALL
        );
        var theFormId = FormId.get("12345678-1234-1234-1234-12345678abcd");
        var formTrigger = EntityFormSelector.get(projectId,
                                                 criteria,
                                                 FormPurpose.ENTITY_CREATION,
                                                 theFormId);
        repository.save(formTrigger);
        var deserializedFormTrigger = repository.findFormSelectors(projectId).findFirst().orElseThrow();
        assertThat(deserializedFormTrigger, is(formTrigger));
    }

    @AfterEach
    public void tearDown() throws Exception {
        mongoTemplate.getDb().getCollection("FormSelectors").drop();
    }
}
