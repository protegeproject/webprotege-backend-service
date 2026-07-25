package edu.stanford.protege.webprotege.events.outbox;

import edu.stanford.protege.webprotege.common.ProjectId;

import javax.annotation.Nonnull;
import java.util.concurrent.atomic.AtomicLong;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Tracks, per project, the highest revision number whose change-history write is known to be durable.
 *
 * <p>The watermark is a counter initialised to the head revision number when the revision store is loaded
 * (every loaded revision is on disk and therefore durable) and incremented once for each subsequent durable
 * write.  This is safe because the revision store persists changes on a single-threaded FIFO executor, so
 * revision <i>N</i> becoming durable implies every revision {@code <= N} is durable.  The saved-hook that
 * drives {@link #advance()} carries no revision number, which is why counting invocations is sufficient.</p>
 */
public class DurableRevisionWatermark {

    private final ProjectId projectId;

    private final AtomicLong durableRevision;

    public DurableRevisionWatermark(@Nonnull ProjectId projectId, long headRevisionAtLoad) {
        this.projectId = checkNotNull(projectId);
        this.durableRevision = new AtomicLong(headRevisionAtLoad);
    }

    @Nonnull
    public ProjectId getProjectId() {
        return projectId;
    }

    /**
     * Advances the watermark by one durable revision.  Invoked from the revision store's saved-hook.
     */
    public void advance() {
        durableRevision.incrementAndGet();
    }

    /**
     * Gets the highest revision number currently known to be durable.
     */
    public long getDurableRevision() {
        return durableRevision.get();
    }
}
