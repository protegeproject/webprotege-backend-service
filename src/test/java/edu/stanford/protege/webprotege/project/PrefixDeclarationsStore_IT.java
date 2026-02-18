package edu.stanford.protege.webprotege.project;

import com.google.common.collect.ImmutableMap;
import edu.stanford.protege.webprotege.MongoTestExtension;
import edu.stanford.protege.webprotege.WebprotegeBackendMonolithApplication;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.annotation.DirtiesContext;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23 Feb 2018
 */
@SpringBootTest(properties = {"webprotege.rabbitmq.commands-subscribe=false"})
@Import(WebprotegeBackendMonolithApplication.class)
@ExtendWith({MongoTestExtension.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class PrefixDeclarationsStore_IT {

    public static final String COLLECTION_NAME = "PrefixDeclarations";
    
    private final ProjectId projectId = ProjectId.valueOf("12345678-1234-1234-1234-123456789abc");

    @Autowired
    private PrefixDeclarationsStore store;

    @Autowired
    private MongoTemplate mongoTemplate;

    private PrefixDeclarations prefixDeclarations;

    @BeforeEach
    public void setUp() throws Exception {
        ImmutableMap.Builder<String, String> prefixesMap = ImmutableMap.builder();
        prefixesMap.put("a:", "http://ont.org/a/");
        prefixDeclarations = PrefixDeclarations.get(
                projectId,
                prefixesMap.build()
        );
    }

    @Test
    public void shouldSavePrefixes() {
        store.save(prefixDeclarations);
        assertThat(countDocuments(), is(1L));
    }

    private long countDocuments() {
        return mongoTemplate.getCollection(COLLECTION_NAME).countDocuments();
    }

    @Test
    public void shouldNotCreateDuplicates() {
        store.save(prefixDeclarations);
        store.save(prefixDeclarations);
        assertThat(countDocuments(), is(1L));
    }

    @Test
    public void shouldRetrivePrefixes() {
        store.save(prefixDeclarations);
        PrefixDeclarations prefixes = store.find(projectId);
        assertThat(prefixes, is(prefixDeclarations));
    }

    @AfterEach
    public void tearDown() {
        mongoTemplate.getCollection(COLLECTION_NAME).drop();
    }
}
