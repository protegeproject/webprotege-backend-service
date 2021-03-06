package edu.stanford.protege.webprotege.change.matcher;

import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.change.description.StructuredChangeDescription;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2018-12-10
 */
@AutoValue
public abstract class ChangeSummary {

    public static ChangeSummary get(@Nonnull StructuredChangeDescription description) {
        return new AutoValue_ChangeSummary(description);
    }

    @Nonnull
    public abstract StructuredChangeDescription getDescription();
}
