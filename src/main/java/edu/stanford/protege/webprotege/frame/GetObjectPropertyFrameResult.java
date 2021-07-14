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

@JsonTypeName("GetObjectPropertyFrame")
public abstract class GetObjectPropertyFrameResult implements Result {

    @JsonCreator
    public static GetObjectPropertyFrameResult create(@JsonProperty("frame") ObjectPropertyFrame frame) {
        return new AutoValue_GetObjectPropertyFrameResult(frame);
    }

    public abstract ObjectPropertyFrame getFrame();
}
