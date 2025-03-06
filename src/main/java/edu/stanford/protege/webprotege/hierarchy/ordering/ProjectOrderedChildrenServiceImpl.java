package edu.stanford.protege.webprotege.hierarchy.ordering;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.UpdateOptions;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.dispatch.actions.SaveEntityChildrenOrderingAction;
import com.mongodb.client.model.*;
import edu.stanford.protege.webprotege.common.*;
import edu.stanford.protege.webprotege.hierarchy.ordering.dtos.OrderedChildren;
import edu.stanford.protege.webprotege.locking.ReadWriteLockService;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static edu.stanford.protege.webprotege.hierarchy.ordering.ProjectOrderedChildren.ENTITY_URI;
import static edu.stanford.protege.webprotege.hierarchy.ordering.ProjectOrderedChildren.PROJECT_ID;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

public class ProjectOrderedChildrenServiceImpl implements ProjectOrderedChildrenService {

    private final ObjectMapper objectMapper;
    private final ProjectOrderedChildrenRepository repository;
    private final ReadWriteLockService readWriteLock;

    public ProjectOrderedChildrenServiceImpl(ObjectMapper objectMapper,
                                             ProjectOrderedChildrenRepository repository,
                                             ReadWriteLockService readWriteLock) {
        this.objectMapper = objectMapper;
        this.repository = repository;
        this.readWriteLock = readWriteLock;
    }

    @Override
    public Consumer<List<OrderedChildren>> createBatchProcessorForImportingPaginatedOrderedChildren(ProjectId projectId, boolean overrideExisting) {
        return page -> {
            if (isNotEmpty(page)) {
                var siblingsOrderingsToBeSaved = page.stream()
                        .map(orderedChildren -> createProjectOrderedChildren(orderedChildren, projectId, null))
                        .collect(Collectors.toSet());

                importMultipleProjectOrderedChildren(siblingsOrderingsToBeSaved, overrideExisting);

            }
        };
    }

    @Override
    public void importMultipleProjectOrderedChildren(Set<ProjectOrderedChildren> projectOrderedChildrenToBeSaved, boolean overrideExisting) {
        if (projectOrderedChildrenToBeSaved.isEmpty()) {
            return;
        }

        readWriteLock.executeWriteLock(() -> {
            List<UpdateOneModel<Document>> operations;
            if (overrideExisting) {
                operations = projectOrderedChildrenToBeSaved.stream()
                        .map(orderedChildren -> mapToUpdateOneModelWithInsertType(orderedChildren, "$set"))
                        .toList();

            } else {
                Set<String> existingEntries = repository.findExistingEntries(new ArrayList<>(projectOrderedChildrenToBeSaved));

                operations = projectOrderedChildrenToBeSaved.stream()
                        .filter(orderedChildren -> {
                            String fullKey = orderedChildren.entityUri() + "|" + orderedChildren.projectId().id();
                            return !existingEntries.contains(fullKey);
                        })
                        .map(orderedChildren -> mapToUpdateOneModelWithInsertType(orderedChildren, "$setOnInsert"))
                        .toList();
            }
            if (!operations.isEmpty()) {
                repository.bulkWriteDocuments(operations);
            }
        });
    }

    @NotNull
    private UpdateOneModel<Document> mapToUpdateOneModelWithInsertType(ProjectOrderedChildren orderedChildren, String insertType) {
        Document doc = objectMapper.convertValue(orderedChildren, Document.class);
        return new UpdateOneModel<>(
                Filters.and(
                        Filters.eq(ENTITY_URI, orderedChildren.entityUri()),
                        Filters.eq(PROJECT_ID, orderedChildren.projectId().id())
                ),
                new Document(insertType, doc),
                new UpdateOptions().upsert(true)
        );
    }


    @Override
    public ProjectOrderedChildren createProjectOrderedChildren(OrderedChildren orderedChildren, ProjectId projectId, UserId userId) {
        return ProjectOrderedChildrenMapper.mapToProjectOrderedChildren(orderedChildren, projectId, userId);
    }

    public void addChildToParent(ProjectId projectId, String parentUri, String newChildUri) {
        readWriteLock.executeWriteLock(() -> {
            Optional<ProjectOrderedChildren> existingEntry = repository.findOrderedChildren(projectId, parentUri);

            if (existingEntry.isPresent()) {
                ProjectOrderedChildren updatedEntry = new ProjectOrderedChildren(
                        existingEntry.get().entityUri(),
                        projectId,
                        new ArrayList<>(existingEntry.get().children()),
                        existingEntry.get().userId()
                );
                updatedEntry.children().add(newChildUri);
                repository.update(updatedEntry);
            } else {
                ProjectOrderedChildren newEntry = new ProjectOrderedChildren(parentUri, projectId, List.of(newChildUri), null);
                repository.insert(newEntry);
            }
        });
    }

    @Override
    public void updateEntity(SaveEntityChildrenOrderingAction action, UserId userId) {
        Optional<ProjectOrderedChildren> entityChildrenOrdering = repository.findOrderedChildren(action.projectId(), action.entityIri().toString());
        if(entityChildrenOrdering.isPresent()) {
            ProjectOrderedChildren orderToBeSaved = entityChildrenOrdering.map(ordering -> new ProjectOrderedChildren(ordering.entityUri(),
                    ordering.projectId(),
                    action.orderedChildren(),
                    ordering.userId())).get();
            repository.update(orderToBeSaved);

        } else {
            ProjectOrderedChildren orderedChildren = new ProjectOrderedChildren(action.entityIri().toString(),
                    action.projectId(),
                    action.orderedChildren(),
                    userId.id());
            repository.insert(orderedChildren);
        }
    }

    public void removeChildFromParent(ProjectId projectId, String parentUri, String childUriToRemove) {
        readWriteLock.executeWriteLock(() -> {
            Optional<ProjectOrderedChildren> existingEntry = repository.findOrderedChildren(projectId, parentUri);

            if (existingEntry.isPresent()) {
                List<String> updatedChildren = new ArrayList<>(existingEntry.get().children());
                boolean removed = updatedChildren.remove(childUriToRemove);

                if (removed) {
                    if (updatedChildren.isEmpty()) {
                        repository.delete(existingEntry.get());
                    } else {
                        ProjectOrderedChildren updatedEntry = new ProjectOrderedChildren(
                                existingEntry.get().entityUri(),
                                projectId,
                                updatedChildren,
                                existingEntry.get().userId()
                        );
                        repository.update(updatedEntry);
                    }
                }
            }
        });
    }
}
