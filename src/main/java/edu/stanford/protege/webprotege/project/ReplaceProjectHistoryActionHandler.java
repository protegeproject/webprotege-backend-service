package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.common.BlobLocation;
import edu.stanford.protege.webprotege.common.EventId;
import edu.stanford.protege.webprotege.common.ProjectEvent;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.event.LargeNumberOfChangesEvent;
import edu.stanford.protege.webprotege.ipc.EventDispatcher;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.storage.MinioFileDownloader;
import org.jetbrains.annotations.NotNull;
import org.semanticweb.binaryowl.BinaryOWLOntologyChangeLog;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.Nonnull;

public class ReplaceProjectHistoryActionHandler extends AbstractProjectActionHandler<ReplaceProjectHistoryRequest, ReplaceProjectHistoryResponse> {

    private static final Logger logger = LoggerFactory.getLogger(ReplaceProjectHistoryActionHandler.class);

    private final ProjectHistoryReplacementSaga replacementSaga;

   public ReplaceProjectHistoryActionHandler(@Nonnull AccessManager accessManager,
                                             @Nonnull ProjectHistoryReplacementSaga replacementSaga) {
        super(accessManager);
        this.replacementSaga = replacementSaga;
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public Class<ReplaceProjectHistoryRequest> getActionClass() {
        return ReplaceProjectHistoryRequest.class;
    }

    @Nonnull
    public ReplaceProjectHistoryResponse execute(@Nonnull ReplaceProjectHistoryRequest action,
                                                 @Nonnull ExecutionContext executionContext) {
        return replacementSaga.run(action);
    }
}
