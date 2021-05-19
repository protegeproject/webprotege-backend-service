package edu.stanford.protege.webprotege.persistence;

import com.mongodb.MongoClient;
import edu.stanford.protege.webprotege.app.ApplicationDisposablesManager;
import edu.stanford.protege.webprotege.color.ColorConverter;
import edu.stanford.protege.webprotege.form.FormIdConverter;
import edu.stanford.protege.webprotege.inject.MongoClientProvider;
import edu.stanford.protege.webprotege.tag.TagIdConverter;
import edu.stanford.protege.webprotege.util.DisposableObjectManager;
import org.mongodb.morphia.Morphia;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 6 Oct 2016
 */
public class MongoTestUtils {

    private static final String TEST_DB_NAME = "webprotege-test";

    public static MongoClient createMongoClient() {
        return new MongoClientProvider(Optional.of("localhost"),
                                       Optional.of(27017),
                                       Optional.empty(),
                                       Optional.empty(), new ApplicationDisposablesManager(new DisposableObjectManager())).get();
    }

    public static Morphia createMorphia() {
        return new MorphiaProvider(
                new UserIdConverter(),
                new OWLEntityConverter(new OWLDataFactoryImpl()),
                new ProjectIdConverter(),
                new ThreadIdConverter(),
                new CommentIdConverter(),
                new FormIdConverter(),
                new TagIdConverter(),
                new ColorConverter()).get();
    }


    public static String getTestDbName() {
        return TEST_DB_NAME;
    }
}
