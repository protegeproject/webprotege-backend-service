package edu.stanford.protege.webprotege.hierarchy.ordering;

import edu.stanford.protege.webprotege.StreamUtils;
import edu.stanford.protege.webprotege.hierarchy.ordering.dtos.OrderedChildren;
import edu.stanford.protege.webprotege.ipc.*;
import edu.stanford.protege.webprotege.locking.ReadWriteLockService;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Consumer;


@WebProtegeHandler
public class ProcessUploadedSiblingsOrderingCommandHandler implements CommandHandler<ProcessUploadedSiblingsOrderingAction, ProcessUploadedSiblingsOrderingResponse> {

    private final OrderedChildrenDocumentService orderedChildrenDocumentService;

    private final ProjectOrderedChildrenService projectOrderedChildrenService;

    private final ReadWriteLockService readWriteLock;


    public ProcessUploadedSiblingsOrderingCommandHandler(OrderedChildrenDocumentService orderedChildrenDocumentService,
                                                         ProjectOrderedChildrenService projectOrderedChildrenService,
                                                         ReadWriteLockService readWriteLock) {

        this.orderedChildrenDocumentService = orderedChildrenDocumentService;
        this.projectOrderedChildrenService = projectOrderedChildrenService;
        this.readWriteLock = readWriteLock;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return ProcessUploadedSiblingsOrderingAction.CHANNEL;
    }

    @Override
    public Class<ProcessUploadedSiblingsOrderingAction> getRequestClass() {
        return ProcessUploadedSiblingsOrderingAction.class;
    }

    @Override
    public Mono<ProcessUploadedSiblingsOrderingResponse> handleRequest(ProcessUploadedSiblingsOrderingAction request,
                                                                       ExecutionContext executionContext) {

        var stream = orderedChildrenDocumentService.fetchFromDocument(request.uploadedDocumentId().getDocumentId());

        readWriteLock.executeWriteLock(() -> {
            Consumer<List<OrderedChildren>> batchProcessor = projectOrderedChildrenService.createBatchProcessorForImportingPaginatedOrderedChildren(request.projectId());
            stream.collect(StreamUtils.batchCollector(100, batchProcessor));
        });

        return Mono.just(ProcessUploadedSiblingsOrderingResponse.create());
    }


}
