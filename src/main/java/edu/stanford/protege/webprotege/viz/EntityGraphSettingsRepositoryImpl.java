package edu.stanford.protege.webprotege.viz;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;
import edu.stanford.protege.webprotege.inject.ApplicationSingleton;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import jakarta.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.protege.webprotege.viz.ProjectUserEntityGraphSettings.PROJECT_ID;
import static edu.stanford.protege.webprotege.viz.ProjectUserEntityGraphSettings.USER_ID;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-06
 */
@ApplicationSingleton
public class EntityGraphSettingsRepositoryImpl implements EntityGraphSettingsRepository {

    private static final String COLLECTION_NAME = "EntityGraphSettings";

    private final MongoTemplate database;

    private final ObjectMapper objectMapper;

    @Inject
    public EntityGraphSettingsRepositoryImpl(MongoTemplate database,
                                             ObjectMapper objectMapper) {
        this.database = checkNotNull(database);
        this.objectMapper = checkNotNull(objectMapper);
    }

    @Override
    public void saveSettings(@Nonnull ProjectUserEntityGraphSettings settings) {
        var document = objectMapper.convertValue(settings, Document.class);
        var filter = getUpsertFilter(settings.projectId(), settings.getUserId()
                                                                   .orElse(null));
        var replaceOptions = new ReplaceOptions().upsert(true);
        getCollection().replaceOne(filter, document, replaceOptions);
    }

    @Nonnull
    @Override
    public ProjectUserEntityGraphSettings getProjectDefaultSettings(@Nonnull ProjectId projectId) {
        var filter = getGetFilter(projectId, null);
        var settings = getProjectEntityGraphSettings(filter);
        if(settings == null) {
            return ProjectUserEntityGraphSettings.get(projectId, null, EntityGraphSettings.getDefault());
        }
        else {
            return settings;
        }
    }

    @Nullable
    private ProjectUserEntityGraphSettings getProjectEntityGraphSettings(Document filter) {
        var found = getCollection().find(filter);
        var firstDocument = found.first();
        if(firstDocument == null) {
            return null;
        }
        return objectMapper.convertValue(firstDocument, ProjectUserEntityGraphSettings.class);
    }

    @Nonnull
    @Override
    public ProjectUserEntityGraphSettings getSettingsForUserOrProjectDefault(@Nonnull ProjectId projectId,
                                                                  @Nullable UserId userId) {
        ProjectUserEntityGraphSettings userSettings = getProjectEntityGraphSettings(getUpsertFilter(projectId, userId));
        if(userSettings != null) {
            return userSettings;
        }
        ProjectUserEntityGraphSettings defaultSettings = getProjectEntityGraphSettings(getGetFilter(projectId, null));
        if(defaultSettings != null) {
            return defaultSettings;
        }
        return ProjectUserEntityGraphSettings.getDefault(projectId, userId);
    }

    public static Document getGetFilter(@Nonnull ProjectId projectId,
                                     @Nullable UserId userId) {
        var doc = new Document(PROJECT_ID, projectId.id());
        if(userId == null) {
            doc.append(USER_ID, null);
        }
        else {
            doc.append(USER_ID, userId.id());
        }
        return doc;
    }

    public static Document getUpsertFilter(@Nonnull ProjectId projectId,
                                           @Nullable UserId userId) {
        var doc = new Document(PROJECT_ID, projectId.id());
        doc.append(USER_ID, userId == null ? null : userId.id());
        return doc;
    }

    @Override
    public void ensureIndexes() {

    }

    public static String getCollectionName() {
        return COLLECTION_NAME;
    }

    public MongoCollection<Document> getCollection() {
        return database.getCollection(COLLECTION_NAME);
    }
}
