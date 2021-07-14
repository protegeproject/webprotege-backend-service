package edu.stanford.protege.webprotege.perspective;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.Result;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28/02/16
 */
@AutoValue

@JsonTypeName("SetPerspectiveLayout")
public abstract class SetPerspectiveLayoutResult implements Result {

    @JsonCreator
    public static SetPerspectiveLayoutResult create() {
        return new AutoValue_SetPerspectiveLayoutResult();
    }
}
