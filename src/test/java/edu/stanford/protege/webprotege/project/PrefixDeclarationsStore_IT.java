package edu.stanford.protege.webprotege.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.google.common.collect.ImmutableMap;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23 Feb 2018
 */
@DataMongoTest
public class PrefixDeclarationsStore_IT {

    public static final String COLLECTION_NAME = "PrefixDeclarations";
    
    private final ProjectId projectId = ProjectId.valueOf("12345678-1234-1234-1234-123456789abc");

    private PrefixDeclarationsStore store;

    private ObjectMapper objectMapper;

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
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jdk8Module());
        store = new PrefixDeclarationsStore(objectMapper, mongoTemplate);
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
