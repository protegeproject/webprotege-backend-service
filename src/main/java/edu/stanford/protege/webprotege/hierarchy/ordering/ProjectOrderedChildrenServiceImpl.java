package edu.stanford.protege.webprotege.hierarchy.ordering;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.model.InsertOneModel;
import edu.stanford.protege.webprotege.common.*;
import edu.stanford.protege.webprotege.hierarchy.ordering.dtos.OrderedChildren;
import org.bson.Document;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Service
public class ProjectOrderedChildrenServiceImpl implements ProjectOrderedChildrenService {

    private final ObjectMapper objectMapper;
    private final ProjectOrderedChildrenRepository repository;

    public ProjectOrderedChildrenServiceImpl(ObjectMapper objectMapper,
                                             ProjectOrderedChildrenRepository repository) {
        this.objectMapper = objectMapper;
        this.repository = repository;
    }

    @Override
    public Consumer<List<OrderedChildren>> createBatchProcessorForSavingPaginatedOrderedChildren(ProjectId projectId) {
        return page -> {
            if (isNotEmpty(page)) {
                var siblingsOrderingsToBeSaved = page.stream()
                        .flatMap(orderedChildren -> createProjectOrderedChildren(orderedChildren, projectId, null).stream())
                        .collect(Collectors.toSet());

                saveMultipleProjectOrderedChildren(siblingsOrderingsToBeSaved);

            }
        };
    }

    @Override
    public void saveMultipleProjectOrderedChildren(Set<ProjectOrderedChildren> projectOrderedChildrenToBeSaved) {
        var documents = projectOrderedChildrenToBeSaved.stream()
                .map(history -> new InsertOneModel<>(objectMapper.convertValue(history, Document.class)))
                .toList();
        repository.bulkWriteDocuments(documents);
    }

    @Override
    public Set<ProjectOrderedChildren> createProjectOrderedChildren(OrderedChildren orderedChildren, ProjectId projectId, UserId userId) {
        return ProjectOrderedChildrenMapper.mapToProjectOrderedChildren(orderedChildren, projectId, userId);
    }
}
