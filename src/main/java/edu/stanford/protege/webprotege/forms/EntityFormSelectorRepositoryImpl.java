package edu.stanford.protege.webprotege.forms;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-08
 */
public class EntityFormSelectorRepositoryImpl implements EntityFormSelectorRepository {

    public static final String COLLECTION_NAME = "FormSelectors";

    private static final String PROJECT_ID = "projectId";

    private static final String FORM_ID = "formId";

    @Nonnull
    private final MongoTemplate database;

    @Nonnull
    private final ObjectMapper objectMapper;

    @Inject
    public EntityFormSelectorRepositoryImpl(@Nonnull MongoTemplate database,
                                            @Nonnull ObjectMapper objectMapper) {
        this.database = checkNotNull(database);
        this.objectMapper = checkNotNull(objectMapper);
    }

    @Override
    public void ensureIndexes() {

    }

    @Override
    public Stream<EntityFormSelector> findFormSelectors(@Nonnull ProjectId projectId) {
        var collection = database.getCollection(COLLECTION_NAME);
        var filter = new Document(PROJECT_ID, projectId.id());
        List<EntityFormSelector> resultList = new ArrayList<>();
        collection.find(filter).forEach((Consumer<Document>) doc -> resultList.add(toEntityFormSelector(doc)));
        return resultList.stream();
    }

    private EntityFormSelector toEntityFormSelector(Document doc) {
        return objectMapper.convertValue(doc, EntityFormSelector.class);
    }

    @Override
    public void save(EntityFormSelector entityFormSelector) {
        var triggerDocument = objectMapper.convertValue(entityFormSelector, Document.class);
        var collection = database.getCollection(COLLECTION_NAME);
        var projectIdFilter = Filters.eq(PROJECT_ID, entityFormSelector.getProjectId().id());
        var formIdFilter = Filters.eq(FORM_ID, entityFormSelector.getFormId().getId());
        var filter = Filters.and(projectIdFilter, formIdFilter);
        collection.findOneAndReplace(filter, triggerDocument, new FindOneAndReplaceOptions().upsert(true));
    }
}
