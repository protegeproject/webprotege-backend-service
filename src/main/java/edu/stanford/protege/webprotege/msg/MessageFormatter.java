package edu.stanford.protege.webprotege.msg;

import edu.stanford.protege.webprotege.inject.ProjectSingleton;
import edu.stanford.protege.webprotege.renderer.RenderingManager;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 19 Dec 2017
 */
@ProjectSingleton
public class MessageFormatter {

    @Nonnull
    private final RenderingManager renderingManager;

    @Inject
    public MessageFormatter(@Nonnull RenderingManager renderingManager) {
        this.renderingManager = checkNotNull(renderingManager);
    }

    public String format(@Nonnull String template,
                         @Nonnull Object ... objects) {
        return OWLMessageFormatter.formatMessage(checkNotNull(template), renderingManager, checkNotNull(objects));
    }
}
