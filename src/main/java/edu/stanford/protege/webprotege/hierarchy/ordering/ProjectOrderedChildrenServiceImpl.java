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
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static edu.stanford.protege.webprotege.hierarchy.ordering.ProjectOrderedChildren.*;
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
                var siblingsOrderingsToBeSaved = page.stream()
                        .flatMap(orderedChildren -> createProjectOrderedChildren(orderedChildren, projectId, null).stream())
                        .collect(Collectors.toSet());

                importMultipleProjectOrderedChildren(siblingsOrderingsToBeSaved);

            }
        };
    }

    @Override
    public void importMultipleProjectOrderedChildren(Set<ProjectOrderedChildren> projectOrderedChildrenToBeSaved) {
        if (projectOrderedChildrenToBeSaved.isEmpty()) {
            return;
        }

        readWriteLock.executeWriteLock(() -> {
            Set<String> existingEntries = repository.findExistingEntries(new ArrayList<>(projectOrderedChildrenToBeSaved));

            List<UpdateOneModel<Document>> operations = projectOrderedChildrenToBeSaved.stream()
                    .filter(orderedChildren -> {
                        String fullKey = orderedChildren.parentUri() + "|" + orderedChildren.entityUri() + "|" + orderedChildren.projectId().id();
                        return !existingEntries.contains(fullKey);
                    })
                    .map(orderedChildren -> {
                        Document doc = objectMapper.convertValue(orderedChildren, Document.class);
                        return new UpdateOneModel<Document>(
                                Filters.and(
                                        Filters.eq(PARENT_URI, orderedChildren.parentUri()),
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
    public Set<ProjectOrderedChildren> createProjectOrderedChildren(OrderedChildren orderedChildren, ProjectId projectId, UserId userId) {
        return ProjectOrderedChildrenMapper.mapToProjectOrderedChildren(orderedChildren, projectId, userId);
    }
}
