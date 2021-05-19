package edu.stanford.protege.webprotege.frame;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.Result;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28 Jul 16
 */
@AutoValue
@JsonTypeName("GetClassFrame")
public abstract class GetClassFrameResult implements Result {

    @JsonCreator
    public static GetClassFrameResult get(@JsonProperty("frame") ClassFrame classFrame) {
        return new AutoValue_GetClassFrameResult(classFrame);
    }

    public abstract ClassFrame getFrame();
}
