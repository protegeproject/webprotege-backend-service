package edu.stanford.protege.webprotege.events.outbox;

import edu.stanford.protege.webprotege.common.ProjectId;

import javax.annotation.Nonnull;
import java.util.OptionalLong;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * An application-wide registry of the per-project {@link DurableRevisionWatermark}s.  Each project's watermark
 * lives in the project's Spring context and registers itself here so that the application-level
 * {@link EventOutboxRelay} can consult it when deciding whether gated rows may be published.
 *
 * <p>A project's watermark is only present in the instance that has the project loaded.  Under the
 * single-homed-project assumption that is the only instance that writes that project's outbox rows, so the
 * relay finds the watermark it needs.</p>
 */
public class DurableRevisionWatermarkRegistry {

    private final ConcurrentMap<ProjectId, DurableRevisionWatermark> watermarks = new ConcurrentHashMap<>();

    public void register(@Nonnull DurableRevisionWatermark watermark) {
        checkNotNull(watermark);
        watermarks.put(watermark.getProjectId(), watermark);
    }

    public void deregister(@Nonnull ProjectId projectId) {
        watermarks.remove(checkNotNull(projectId));
    }

    /**
     * Gets the highest durable revision for the given project, or empty if this instance does not have the
     * project loaded (and therefore cannot vouch for the durability of its gated rows).
     */
    @Nonnull
    public OptionalLong getDurableRevision(@Nonnull ProjectId projectId) {
        var watermark = watermarks.get(checkNotNull(projectId));
        return watermark == null ? OptionalLong.empty() : OptionalLong.of(watermark.getDurableRevision());
    }
}
