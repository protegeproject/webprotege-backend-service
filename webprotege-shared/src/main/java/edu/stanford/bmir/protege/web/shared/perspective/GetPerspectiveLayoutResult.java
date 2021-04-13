package edu.stanford.bmir.protege.web.shared.perspective;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17/02/16
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("GetPerspectiveLayout")
public abstract class GetPerspectiveLayoutResult implements Result {


    @JsonCreator
    public static GetPerspectiveLayoutResult create(@JsonProperty("layout") PerspectiveLayout perspective) {
        return new AutoValue_GetPerspectiveLayoutResult(perspective);
    }

    public abstract PerspectiveLayout getLayout();
}
