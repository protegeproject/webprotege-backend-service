package edu.stanford.protege.webprotege.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.model.ReplaceOptions;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.protege.webprotege.project.PrefixDeclarations.PROJECT_ID;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Feb 2018
 */
public class PrefixDeclarationsStore {

    private static final String PREFIX_DECLARATIONS = "PrefixDeclarations";

    @Nonnull
    private final ObjectMapper objectMapper;

    @Nonnull
    private final MongoTemplate database;

    @Inject
    public PrefixDeclarationsStore(@Nonnull ObjectMapper objectMapper,
                                   @Nonnull MongoTemplate database) {
        this.objectMapper = checkNotNull(objectMapper);
        this.database = checkNotNull(database);
    }

    public void save(@Nonnull PrefixDeclarations prefixDeclarations) {
        checkNotNull(prefixDeclarations);
        var collection = database.getCollection(PREFIX_DECLARATIONS);
        var doc = objectMapper.convertValue(prefixDeclarations, Document.class);
        collection.replaceOne(new Document(PROJECT_ID, prefixDeclarations.getProjectId().id()),
                              doc,
                              new ReplaceOptions().upsert(true));
    }

    @Nonnull
    public PrefixDeclarations find(@Nonnull ProjectId projectId) {
        var collection = database.getCollection(PREFIX_DECLARATIONS);
        var doc = collection.find(new Document(PROJECT_ID, projectId.id()))
                  .first();
        if(doc == null) {
            return PrefixDeclarations.get(projectId);
        }
        else {
            return objectMapper.convertValue(doc, PrefixDeclarations.class);
        }
    }
}
