package edu.stanford.bmir.protege.web.server.form.field;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-06
 */
public interface BoundControlDescriptor {

    @Nonnull
    Optional<OwlBinding> getOwlBinding();

    @Nonnull
    FormControlDescriptor getFormControlDescriptor();
}
