package edu.stanford.protege.webprotege.persistence;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import edu.stanford.protege.webprotege.app.ApplicationDisposablesManager;
import edu.stanford.protege.webprotege.jackson.ObjectMapperProvider;
import edu.stanford.protege.webprotege.util.DisposableObjectManager;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 6 Oct 2016
 */
public class MongoTestUtils {

    private static final String TEST_DB_NAME = "webprotege-test";



    public static String getTestDbName() {
        return TEST_DB_NAME;
    }

    public static void shouldSerializeUsingObjectMapper(Object object,
                                                        MongoTemplate template,
                                                        String collectionName,
                                                        Supplier<Object> retriever) {
        var objectMapper = new ObjectMapperProvider().get();
        var doc = objectMapper.convertValue(object, Document.class);
        var collection = template.getCollection(collectionName);
        collection.insertOne(doc);
        var retrieved = retriever.get();
        assertThat(retrieved, is(object));
    }
}
