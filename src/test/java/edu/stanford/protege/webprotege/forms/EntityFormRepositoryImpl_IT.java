package edu.stanford.protege.webprotege.forms;

import edu.stanford.protege.webprotege.MongoTestExtension;
import edu.stanford.protege.webprotege.WebprotegeBackendMonolithApplication;
import edu.stanford.protege.webprotege.forms.field.*;
import edu.stanford.protege.webprotege.common.LanguageMap;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.annotation.DirtiesContext;
import uk.ac.manchester.cs.owl.owlapi.OWLObjectPropertyImpl;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-01
 */
@SpringBootTest
@Import({WebprotegeBackendMonolithApplication.class})
@ExtendWith({MongoTestExtension.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class EntityFormRepositoryImpl_IT {

    @Autowired
    private EntityFormRepositoryImpl impl;

    @Autowired
    private MongoTemplate mongoTemplate;

    private ProjectId projectId;


    @BeforeEach
    public void setUp() {
        projectId = ProjectId.valueOf(UUID.randomUUID().toString());
    }

    @AfterEach
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
