package edu.stanford.protege.webprotege.crud.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;
import edu.stanford.protege.webprotege.inject.ProjectSingleton;
import edu.stanford.protege.webprotege.project.ProjectId;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/08/2013
 * <p>
 * An interface to a repository for storing {@link ProjectEntityCrudKitSettings}.
 * </p>
 */
@ProjectSingleton
public class ProjectEntityCrudKitSettingsRepository {

    public static final String COLLECTION_NAME = "EntityCrudKitSettings";

    @Nonnull
    private final MongoCollection<Document> collection;

    @Nonnull
    private final ObjectMapper objectMapper;

    @Inject
    public ProjectEntityCrudKitSettingsRepository(@Nonnull MongoTemplate database,
                                                  @Nonnull ObjectMapper objectMapper) {
        this.collection = checkNotNull(database).getCollection(COLLECTION_NAME);
        this.objectMapper = objectMapper;
    }

    @Nonnull
    public Optional<ProjectEntityCrudKitSettings> findOne(@Nonnull ProjectId projectId) {
        return Optional.ofNullable(collection.find(new Document("_id", projectId.getId()))
                                             .limit(1).first())
                       .map(d -> objectMapper.convertValue(d, ProjectEntityCrudKitSettings.class));
    }

    public void save(@Nonnull ProjectEntityCrudKitSettings settings) {
        collection.replaceOne(new Document("_id", settings.getProjectId().getId()),
                              objectMapper.convertValue(settings, Document.class),
                              new ReplaceOptions().upsert(true));
    }
}
