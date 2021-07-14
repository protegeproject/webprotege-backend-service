package edu.stanford.protege.webprotege.perspective;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.Result;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-03
 */
@AutoValue

@JsonTypeName("ResetPerspectives")
public abstract class ResetPerspectivesResult implements Result {

    @JsonCreator
    public static ResetPerspectivesResult create() {
        return new AutoValue_ResetPerspectivesResult();
    }
}
