package edu.stanford.bmir.protege.web.shared.frame;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.GetObjectResult;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28 Jul 16
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("GetClassFrame")
public abstract class GetClassFrameResult implements GetObjectResult<ClassFrame> {


    @JsonCreator
    public static GetClassFrameResult get(@JsonProperty("frame") ClassFrame classFrame) {
        return new AutoValue_GetClassFrameResult(classFrame);
    }

    public abstract ClassFrame getFrame();

    /**
     * Gets the object.
     *
     * @return The object.  Not {@code null}.
     */
    @Override
    public ClassFrame getObject() {
        return getFrame();
    }
}
