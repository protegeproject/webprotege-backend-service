package edu.stanford.protege.webprotege.form;

import com.google.common.collect.ImmutableList;
import com.mongodb.client.MongoClient;
import edu.stanford.protege.webprotege.jackson.ObjectMapperProvider;
import edu.stanford.protege.webprotege.persistence.MongoTestUtils;
import edu.stanford.protege.webprotege.match.criteria.CompositeRootCriteria;
import edu.stanford.protege.webprotege.match.criteria.HierarchyFilterType;
import edu.stanford.protege.webprotege.match.criteria.MultiMatchType;
import edu.stanford.protege.webprotege.match.criteria.SubClassOfCriteria;
import edu.stanford.protege.webprotege.project.ProjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.semanticweb.owlapi.model.IRI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-08
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class EntityFormSelectorRepositoryImpl_IT {

    @Autowired
    private EntityFormSelectorRepositoryImpl repository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Before
    public void setUp() {
    }

    @Test
    public void shouldSaveFormTrigger() {
        var projectId = ProjectId.get("609767c5-e12a-43b8-beba-b9f250b35a3a");
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

    @After
    public void tearDown() throws Exception {
        mongoTemplate.getDb().getCollection("FormSelectors").drop();
    }
}
