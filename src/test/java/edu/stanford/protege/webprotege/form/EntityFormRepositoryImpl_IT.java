package edu.stanford.protege.webprotege.form;

import com.mongodb.client.MongoDatabase;
import edu.stanford.protege.webprotege.form.field.*;
import edu.stanford.protege.webprotege.jackson.ObjectMapperProvider;
import edu.stanford.protege.webprotege.persistence.MongoTestUtils;
import edu.stanford.protege.webprotege.lang.LanguageMap;
import edu.stanford.protege.webprotege.project.ProjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import uk.ac.manchester.cs.owl.owlapi.OWLObjectPropertyImpl;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-01
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class EntityFormRepositoryImpl_IT {

    @Autowired
    private EntityFormRepositoryImpl impl;

    @Autowired
    private MongoTemplate mongoTemplate;

    private ProjectId projectId;


    @Before
    public void setUp() {
        projectId = ProjectId.get(UUID.randomUUID().toString());
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void shouldSaveFormDescriptor() {

        var languageMap = LanguageMap.builder()
                                     .put("en", "Water")
                                     .put("de", "Wasser")
                                     .put("en-US", "Water")
                                     .build();
        var formId = FormId.generate();
        var formDescriptor = FormDescriptor.builder(formId)
                      .addDescriptor(FormFieldDescriptor.get(
                              FormFieldId.get(UUID.randomUUID().toString()),
                              OwlPropertyBinding.get(new OWLObjectPropertyImpl(OWLRDFVocabulary.RDFS_COMMENT.getIRI()),
                                                     null),
                              languageMap,
                              FieldRun.START,
                              FormFieldDeprecationStrategy.DELETE_VALUES,
                              new TextControlDescriptor(
                                      LanguageMap.of("en", "Enter brand name"),
                                      StringType.SIMPLE_STRING,
                                      LineMode.SINGLE_LINE,
                                      "Pattern",
                                      LanguageMap.of("en", "There's an error")
                              ),
                              Repeatability.NON_REPEATABLE,
                              Optionality.OPTIONAL,
                              true,
                              ExpansionState.COLLAPSED,
                              LanguageMap.of("en", "Help text")
                      ))
                      .build();

        impl.saveFormDescriptor(projectId,
                                formDescriptor);
        var deserializedFormDescriptor = impl.findFormDescriptor(projectId, formId);
        assertThat(deserializedFormDescriptor.isPresent(), is(true));
        assertThat(deserializedFormDescriptor.get(), is(formDescriptor));
    }

    @Test
    public void shouldDeleteFormDescriptor() {
        var formId = FormId.generate();
        var formIdOther = FormId.generate();

        impl.saveFormDescriptor(projectId, FormDescriptor.builder(formId).build());
        impl.saveFormDescriptor(projectId, FormDescriptor.builder(formIdOther).build());

        var formCount = impl.findFormDescriptors(projectId).count();
        assertThat(formCount, is(2L));

        assertThat(impl.findFormDescriptor(projectId, formId).isPresent(), is(true));

        impl.deleteFormDescriptor(projectId, formId);

        var formCountAfterDelete = impl.findFormDescriptors(projectId).count();
        assertThat(formCountAfterDelete, is(1L));
        assertThat(impl.findFormDescriptor(projectId, formId).isPresent(), is(false));
    }
}
