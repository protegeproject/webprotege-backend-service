package edu.stanford.protege.webprotege.hierarchy.ordering;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.UpdateOptions;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.hierarchy.ordering.dtos.OrderedChildren;
import edu.stanford.protege.webprotege.locking.ReadWriteLockService;
import org.bson.Document;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import static edu.stanford.protege.webprotege.hierarchy.ordering.EntityChildrenOrdering.*;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Service
public class ProjectOrderedChildrenServiceImpl implements ProjectOrderedChildrenService {

    private final ObjectMapper objectMapper;
    private final ProjectOrderedChildrenRepository repository;
    private final ReadWriteLockService readWriteLock;

    public ProjectOrderedChildrenServiceImpl(ObjectMapper objectMapper,
                                             ProjectOrderedChildrenRepository repository, ReadWriteLockService readWriteLock) {
        this.objectMapper = objectMapper;
        this.repository = repository;
        this.readWriteLock = readWriteLock;
    }

    @Override
    public Consumer<List<OrderedChildren>> createBatchProcessorForImportingPaginatedOrderedChildren(ProjectId projectId) {
        return page -> {
            if (isNotEmpty(page)) {

                var orderingSettings = new HashSet<>(createProjectOrderedChildren(page, projectId, null));

                importMultipleProjectOrderedChildren(orderingSettings);

            }
        };
    }

    @Override
    public void importMultipleProjectOrderedChildren(Set<EntityChildrenOrdering> projectOrderedChildrenToBeSaved) {
        if (projectOrderedChildrenToBeSaved.isEmpty()) {
            return;
        }

        readWriteLock.executeWriteLock(() -> {
            Set<String> existingEntries = repository.findExistingEntries(new ArrayList<>(projectOrderedChildrenToBeSaved));

            List<UpdateOneModel<Document>> operations = projectOrderedChildrenToBeSaved.stream()
                    .filter(orderedChildren -> {
                        String fullKey = orderedChildren.entityUri() + "|" + orderedChildren.projectId().id();
                        return !existingEntries.contains(fullKey);
                    })
                    .map(orderedChildren -> {
                        Document doc = objectMapper.convertValue(orderedChildren, Document.class);
                        return new UpdateOneModel<Document>(
                                Filters.and(
                                        Filters.eq(ENTITY_URI, orderedChildren.entityUri()),
                                        Filters.eq(PROJECT_ID, orderedChildren.projectId().id())
                                ),
                                new Document("$setOnInsert", doc),
                                new UpdateOptions().upsert(true)
                        );
                    })
                    .toList();

            if (!operations.isEmpty()) {
                repository.bulkWriteDocuments(operations);
            }
        });
    }

    @Override
    public Set<EntityChildrenOrdering> createProjectOrderedChildren(List<OrderedChildren> orderedChildren, ProjectId projectId, UserId userId) {
        return ProjectOrderedChildrenMapper.mapToProjectOrderedChildren(orderedChildren, projectId, userId);
    }
}
