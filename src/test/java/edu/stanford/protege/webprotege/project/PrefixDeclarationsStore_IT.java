package edu.stanford.protege.webprotege.project;

import com.google.common.collect.ImmutableMap;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import edu.stanford.protege.webprotege.jackson.ObjectMapperProvider;
import edu.stanford.protege.webprotege.persistence.MongoTestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static edu.stanford.protege.webprotege.persistence.MongoTestUtils.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23 Feb 2018
 */
public class PrefixDeclarationsStore_IT {

    public static final String COLLECTION_NAME = "PrefixDeclarations";
    
    private final ProjectId projectId = ProjectId.get("12345678-1234-1234-1234-123456789abc");

    private PrefixDeclarationsStore store;

    private PrefixDeclarations prefixDeclarations;

    private MongoClient client;

    private MongoDatabase database;

    @Before
    public void setUp() throws Exception {
        client = createMongoClient();
        database = client.getDatabase(MongoTestUtils.getTestDbName());
        var objectMapper = new ObjectMapperProvider().get();
        store = new PrefixDeclarationsStore(objectMapper, database);
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
        return database.getCollection(COLLECTION_NAME).countDocuments();
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
        database.drop();
        client.close();
    }
}