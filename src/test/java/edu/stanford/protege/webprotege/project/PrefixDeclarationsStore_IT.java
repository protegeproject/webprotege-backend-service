package edu.stanford.protege.webprotege.project;

import com.google.common.collect.ImmutableMap;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.jackson.ObjectMapperProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23 Feb 2018
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class PrefixDeclarationsStore_IT {

    public static final String COLLECTION_NAME = "PrefixDeclarations";
    
    private final ProjectId projectId = ProjectId.get("12345678-1234-1234-1234-123456789abc");

    @Autowired
    private PrefixDeclarationsStore store;

    @Autowired
    private MongoTemplate mongoTemplate;

    private PrefixDeclarations prefixDeclarations;

    @Before
    public void setUp() throws Exception {
        var objectMapper = new ObjectMapperProvider().get();
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

    @After
    public void tearDown() {
        mongoTemplate.getCollection(COLLECTION_NAME).drop();
    }
}
