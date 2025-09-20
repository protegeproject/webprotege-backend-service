package edu.stanford.protege.webprotege.project;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A handle returned by ChangeManager.freezeWrites().
 * Releasing (unfreeze or close) is thread-agnostic and idempotent.
 */
public final class ProjectFreezeHandle implements AutoCloseable {

    private final Semaphore gate;
    private final AtomicBoolean closed = new AtomicBoolean(false);

    public ProjectFreezeHandle(Semaphore gate) {
        this.gate = gate;
    }

    @Override
    public void close() {
        unfreeze();
    }

    public void unfreeze() {
        if (closed.compareAndSet(false, true)) {
            gate.release();
        }
    }
}
