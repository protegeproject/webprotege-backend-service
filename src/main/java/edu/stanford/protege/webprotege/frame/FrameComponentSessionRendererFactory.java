package edu.stanford.protege.webprotege.frame;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-01
 */
public class FrameComponentSessionRendererFactory {

    @Nonnull
    private final FrameComponentRenderer frameComponentRenderer;

    @Inject
    public FrameComponentSessionRendererFactory(@Nonnull FrameComponentRenderer frameComponentRenderer) {
        this.frameComponentRenderer = checkNotNull(frameComponentRenderer);
    }

    @Nonnull
    public FrameComponentSessionRenderer create() {
        return new FrameComponentSessionRenderer(frameComponentRenderer);
    }
}
