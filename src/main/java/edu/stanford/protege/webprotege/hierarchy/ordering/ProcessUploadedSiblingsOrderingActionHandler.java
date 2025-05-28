package edu.stanford.protege.webprotege.hierarchy.ordering;

import edu.stanford.protege.webprotege.StreamUtils;
import edu.stanford.protege.webprotege.access.*;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.hierarchy.ordering.dtos.OrderedChildren;
import edu.stanford.protege.webprotege.ipc.*;
import edu.stanford.protege.webprotege.locking.ReadWriteLockService;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

import static edu.stanford.protege.webprotege.access.BuiltInCapability.*;


public class ProcessUploadedSiblingsOrderingActionHandler extends AbstractProjectActionHandler<ProcessUploadedSiblingsOrderingAction, ProcessUploadedSiblingsOrderingResponse> {

    private final OrderedChildrenDocumentService orderedChildrenDocumentService;

    private final ProjectOrderedChildrenService projectOrderedChildrenService;

    private final ReadWriteLockService readWriteLock;


    public ProcessUploadedSiblingsOrderingActionHandler(AccessManager accessManager,
                                                        OrderedChildrenDocumentService orderedChildrenDocumentService,
                                                        ProjectOrderedChildrenService projectOrderedChildrenService,
                                                        ReadWriteLockService readWriteLock) {
        super(accessManager);
        this.orderedChildrenDocumentService = orderedChildrenDocumentService;
        this.projectOrderedChildrenService = projectOrderedChildrenService;
        this.readWriteLock = readWriteLock;
    }

    @Nullable
    @Override
    protected BuiltInCapability getRequiredExecutableBuiltInAction(ProcessUploadedSiblingsOrderingAction action) {
        return APPLY_MIGRATION_JSON_FILES;
    }

    @NotNull
    @Override
    public Class<ProcessUploadedSiblingsOrderingAction> getActionClass() {
        return ProcessUploadedSiblingsOrderingAction.class;
    }

    @NotNull
    @Override
    public ProcessUploadedSiblingsOrderingResponse execute(@NotNull ProcessUploadedSiblingsOrderingAction action, @NotNull ExecutionContext executionContext) {
        var stream = orderedChildrenDocumentService.fetchFromDocument(action.uploadedDocumentId().getDocumentId());

        readWriteLock.executeWriteLock(() -> {
            Consumer<List<OrderedChildren>> batchProcessor = projectOrderedChildrenService.createBatchProcessorForImportingPaginatedOrderedChildren(action.projectId(), action.overrideExisting());
            stream.collect(StreamUtils.batchCollector(100, batchProcessor));
        });

        return ProcessUploadedSiblingsOrderingResponse.create();
    }
}
